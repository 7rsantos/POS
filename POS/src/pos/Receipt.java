package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import javafx.collections.ObservableList;

public class Receipt {
	/*
	 * Build the receipt, compute totals
	 * 
	 */
	public static void setupReceipt(String customer, String change, String cashReceived, int discount, String paymentMethod, String status,
			                        int caller)
	{ 
		ObservableList<Product> products = PaymentScreen.getList();
		
		double result = 0; 	
		int count = 0;
		
	   //compute item count
		for(Product p : products)
		{ 
		   count = count + p.getQuantity();
	   	   result = result + p.getUnitPrice() * p.getQuantity();		   
		}	
		
		//compute date
		Date date = new Date();
		
	    String format = new SimpleDateFormat("MM/dd/yyyy").format(date);
	    String format2 = new SimpleDateFormat("yyyy-mm-dd").format(date);
	    
	    //compute time
	    String timeStamp = new SimpleDateFormat("HH:mm").format(date);
	    
	    //get tax dollars and sub total
	    DecimalFormat df = new DecimalFormat("#.##");
	    double total = Product.computeTotal(Double.toString(result), Configs.getProperty("TaxRate") + "%");
	    double taxDollars = result * (Double.parseDouble(Configs.getProperty("TaxRate"))/100);
	   	    
	    total = Double.parseDouble(df.format(total));
	    //taxDollars  = Double.parseDouble(df.format(taxDollars));
	   
	    if(!salesHistoryExists(format2))
	    {	
	       //create new sales history
	    	createSalesHistory(date);
	    }

	    
	    //store receipt in the database
	    String transaction = createReceipt(date, total, paymentMethod, discount, status);
	     
	    //store items of the receipt in the database
	    saveItems(products, transaction);
	    
	    if(caller == 1)
	    {	
	       //print the receipt
	       printReceipt(products, total, result, taxDollars, customer, format, timeStamp, count, change, cashReceived,
	    		transaction);
	    }
	 }
	
	/*
	 *  Check if sales history for the day has not been created
	*/
	public static boolean salesHistoryExists(String date)
	{ 
	   String query = "SELECT salesDate FROM salesHistory WHERE salesHistory.registerSales = ?"
	   		+ " AND salesHistory.salesDate = ?";	
	   try
	   { 
	      //open database
		  Connection conn = Session.openDatabase();
		   
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setInt(1, Integer.parseInt(Configs.getProperty("Register")));
		  ps.setString(2, date);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  int count = 0;
		  
		  //check if there where any results
		  while(rs.next())
		  { 
		     count++;	 
		  }	  
		  
		  if(count == 0)
		  { 
		     return false;	  
		  }	  
		  else
		  { 
			 return true; 
		  }	  
		  
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();   
	   }
	   
	   return false;
	}
	
	
	/*
	 *  Create new sales history 
	 */
	public static void createSalesHistory(Date date)
	{ 
	   String df = new SimpleDateFormat("yyyy-MM-dd").format(date);	 
	   String query = "CALL createSalesHistory(?,?)";
	   
	   try
	   { 
		  //open the connection 
		  Connection conn = Session.openDatabase();
		  
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameter
		  ps.setString(1, df);
		  ps.setInt(2, Integer.parseInt(Configs.getProperty("Register")));
		  
		  //execute query
		  ps.executeQuery();
		  
		  //close the connection
		  conn.close();
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();   
	   }
	}
	
	
	/*
	 *  Create receipt
	 */
	private static String createReceipt(Date date, double total, String paymentMethod, int discount,
			String status) 
	{
		
		//date format
		DateFormat df = new SimpleDateFormat("yy");
		DateFormat df2 = new SimpleDateFormat("MM");		
		DateFormat df3 = new SimpleDateFormat("dd");		
		DateFormat df4 = new SimpleDateFormat("yyyy-MM-dd");			
		String year = df.format(date);
		String day = df3.format(date);
		String month = df2.format(date);
		String mysqlDate = df4.format(date);
		
		//query
		String query2 = "CALL createSalesTicket(?,?,?,?,?,?,?)";
		String query1 = "SELECT COUNT('ticketNo') FROM salesTicket WHERE salesTicket.ticketDate= ?";
		String ticketno = Configs.getProperty("StoreNumber") + "." + Configs.getProperty("Register")
		                   + "." + year + "." + month + "." + day + ".";
		                   
		try
		{ 
			Connection conn = Session.openDatabase();
			PreparedStatement ps = conn.prepareStatement(query1);
			
			//set parameters
			ps.setString(1, mysqlDate);
			
			//execute first query
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{ 
				//setup transaction number
			   ticketno = ticketno + Integer.toString(Integer.parseInt(rs.getString(1)) + 1);	
			}	
			
			//go for next query
			PreparedStatement ps2 = conn.prepareStatement(query2);
			
			//set parameters
			ps2.setString(1, ticketno);
			ps2.setDouble(2, total);
			ps2.setString(3, Configs.getProperty("CurrentUser"));
			ps2.setString(4, paymentMethod);
			ps2.setInt(5, 1);
			ps2.setInt(6, discount);
			ps2.setString(7, status);
			
			//execute query
			rs = ps2.executeQuery();

		}
		catch(Exception e)
		{ 
			AlertBox.display("FASS Nova", "An error has occurred");
			e.printStackTrace();
		}
		
		//return ticket number
		return ticketno;
	}
	
	/*
	 *  Save items of a receipt in the database
	 */
    public static void saveItems(ObservableList<Product> products, String ticketno) 
    { 
	
       
       try
       {
          String query = "CALL createItem(?, ?, ?, ?, ?)"; 
    	   
          Connection conn = Session.openDatabase();
          
          PreparedStatement pst = conn.prepareStatement(query);
    		           
    	
          conn.setAutoCommit(false);
 
    	   final int batchSize = 1000;
    	   int count = 0;
    	   
          for(Product p : products)
          {	 
        	   
             //set parameters
             pst.setString(1, p.getName());
             pst.setString(2, ticketno);
             pst.setInt(3, p.getQuantity());
             pst.setDouble(4, p.getUnitPrice());
             pst.setString(5, p.getUnitSize());
              
             //add batch
             pst.addBatch();
             count++;
             pst.clearParameters();
            
             if(count % batchSize == 0)
             {	 
                //execute batch
                pst.executeBatch();
             }   
          }  
          
          //execute query
          pst.executeBatch();
                        
          //close the connection
          pst.close();
          conn.commit();
          conn.close();
       }
       catch(SQLException e)
       { 
    	   e.printStackTrace();   
       }
    }
	
	/*
	 * Print the receipt
	 * @param products the list of the products and quantities
	 * @param total the total for the receipt
	 * @param customer the name of the customer
	 */
	public static void printReceipt(ObservableList<Product> products, double total, double subtotal, 
			double taxDollars, String customer, String date, String timeStamp,
			int count, String change, String cashReceived, String transaction) 
	{
		
		PrinterService printerService = new PrinterService();
		
		//System.out.println(printerService.getPrinters());
        //String transaction = "1.1.17.1";
		
        //String format = String.format("%.2f", taxDollars);
        
		//print the header
		printerService.printString(Configs.getProperty("Printer"), " \n \n \n \t \t" + "   " + Configs.getProperty("StoreName"));
		printerService.printString(Configs.getProperty("Printer"), "\n \t \t" + "   " + "Store # " + Configs.getProperty("StoreNumber"));
		printerService.printString(Configs.getProperty("Printer"), " \n \t \t" + "" +  Configs.getProperty("StreetAddress"));
		printerService.printString(Configs.getProperty("Printer"), " \n \t " +  "     " + Configs.getProperty("City") + " " +
		    Configs.getProperty("State") + " " + Configs.getProperty("ZipCode"));
		printerService.printString(Configs.getProperty("Printer"), " \n \t \t" + " " + Configs.getProperty("PhoneNumber") + "\n\n\n");
		
		//print cashier, sales ticket and date info
		printerService.printString(Configs.getProperty("Printer"), "Transaction Number #: " + "\t" + transaction);
		printerService.printString(Configs.getProperty("Printer"), "\n" + "Manager:" + "\t\t" + Configs.getProperty("Manager"));		
		printerService.printString(Configs.getProperty("Printer"), " \n" + "Date: " + "\t\t\t" + date);
		printerService.printString(Configs.getProperty("Printer"), " \n" + "Time: " + "\t\t\t" + timeStamp);
		printerService.printString(Configs.getProperty("Printer"), " \n" + "Cashier: " + "\t\t" + "Bill");
		printerService.printString(Configs.getProperty("Printer"), " \n" + "Customer: " + "\t\t" + "Bob \n\n");

		
        //print items header
		printerService.printString(Configs.getProperty("Printer"), "\t" + "Item" + "\t\t\t" + "Item Total \n");
		printerService.printString(Configs.getProperty("Printer"), "************************************************" + "\n");

		//print items
		for(Product p : products)
		{ 
		   if(p.getQuantity() > 1)
		   { 		      
		      if(p.getName().length() == 30)
		      {	  
				 printerService.printString(Configs.getProperty("Printer"), p.getName());		    	  
			     printerService.printString(Configs.getProperty("Printer"), "\t\t $ " + setPrecision(p.getPrice()) + "\n");
		      }
		      if(p.getName().length() < 30)
		      {
				 printerService.printString(Configs.getProperty("Printer"), p.getName());		    	  
		    	 int size = 30 - p.getName().length() + 1; 
		    	 String s = "";
		    	 
		    	 for (int i = 0; i < size ; i++)
		    	 { 
		    	     s += " ";  	 
		    	 }	 
		    	 printerService.printString(Configs.getProperty("Printer"), s + "\t\t" + "$ " + p.getPrice() + "\n");
		      }	  
		      else
		      { 
		         String name = p.getName().substring(0, 29);
		         printerService.printString(Configs.getProperty("Printer"), name);
			     printerService.printString(Configs.getProperty("Printer"), "\t\t $ " + p.getPrice() + "\n");		         
		      }	  		      
		      
			  printerService.printString(Configs.getProperty("Printer"), "\t" + p.getQuantity() + " @ " + p.getUnitPrice() + " each \n");
		   }	
		   else
		   { 
			   if(p.getName().length() == 30)
			   {	  
		   	      printerService.printString(Configs.getProperty("Printer"), p.getName());		    	  
				  printerService.printString(Configs.getProperty("Printer"), "\t\t $ " + p.getPrice() + "\n");
			   }
			   if(p.getName().length() < 30)
			   {
		   	       printerService.printString(Configs.getProperty("Printer"), p.getName());		    	  
			       int size = 30 - p.getName().length() + 1; 
			       String s = "";
			    	 
			       for (int i = 0; i < size ; i++)
			       { 
			           s += " ";  	 
			       }	 
			       printerService.printString(Configs.getProperty("Printer"), s + "\t\t" + "$ " + setPrecision(p.getPrice()) + "\n");
			   }	  
			   else
			   { 
		          String name = p.getName().substring(0, 29);
	  	          printerService.printString(Configs.getProperty("Printer"), name);
				  printerService.printString(Configs.getProperty("Printer"), "\t\t $ " + p.getPrice() + "\n");		         
			   }	    
		   }       
		}	
		
		//subtotals, tax, and total
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + "Subtotal: " + "\t $" + subtotal + " \n");
		printerService.printString(Configs.getProperty("Printer"), "\t\t" + "Tax " + Configs.getProperty("TaxRate") + "%:" + "\t $" + setPrecision(taxDollars) + "\n");
		printerService.printString(Configs.getProperty("Printer"), "\t\t" + "Total:  " + "\t $" + total);

        //payment method and change
		printerService.printString(Configs.getProperty("Printer"), "\n\n\t\t" + "Cash " + "\t\t " + cashReceived + " \n");
		printerService.printString(Configs.getProperty("Printer"), "\t\t" + "Change: " + "\t " + change + " \n");		
		
		//items sold
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + "Items Sold: " + "\t" + count + "\n");
		
        //display greeting		
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + Configs.getProperty("Slogan") + "\n");		
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + "" + Configs.getProperty("Greeting") + "\n\n\n\n");
		
		// cut that paper
		byte[] cut = new byte[]  {0x1b, 0x69};
		
		//open the cash drawer
		byte[] openP = new byte[] {0x1B, 0x70, 0x30, 0x37, 0x79};
 
		printerService.printBytes(Configs.getProperty("Printer"), cut);
		printerService.printBytes(Configs.getProperty("Printer"), openP);
	}
	
	/*
	 * Set precision of double to two decimal places
	 */
	public static double setPrecision(double d) {
	    return (long) (d * 1e2) / 1e2;
	}
}
