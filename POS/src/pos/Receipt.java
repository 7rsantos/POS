package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Receipt {
	
	/*
	 * Build the receipt, compute totals
	 * 
	 */
	public static void setupReceipt(String customer, String change, String cashReceived, double discount, String paymentMethod, String status,
			                        int caller, String ticketno)
	{ 
		ObservableList<Product> products = PaymentScreen.getList();
		
		double result = 0; 	
		int count = 0;
		
	   //compute item count
		//for(Product p : products)
		//{ 
		   //count = count + p.getQuantity();
	   	   //result = result + p.getUnitPrice() * p.getQuantity();		   
		//}		
		
		count = products.size();
		result = Product.computeSubTotal(products, Double.toString(discount) + "%");
		
		//apply discount if any
		if(discount > 0)
		{ 
		   result = Receipt.setPrecision(result- (discount/100) * result);
		}	
		
		//compute date
		Date date = new Date();
		
	    String format = new SimpleDateFormat("MM/dd/yyyy").format(date);
	    String format2 = new SimpleDateFormat("yyyy-mm-dd").format(date);
	    
	    //compute time
	    String timeStamp = new SimpleDateFormat("HH:mm").format(date);
	    
	    //get tax dollars and sub total
	    double total = Double.parseDouble(MainScreen.Total.getText());
	    double taxDollars = result * (Double.parseDouble(Configs.getProperty("TaxRate"))/100);
	   	    
	    total = Receipt.setPrecision(total);
	   
	    if(!salesHistoryExists(format2))
	    {	
	       //create new sales history
	    	createSalesHistory(date);
	    }
	    
	    if(status.equals("Incomplete"))
	    {	
	       //set status
	       status = "Completed";
	       
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
	    else
	    {
	       
	       //change status
	    	status = "Completed";
	    	
	       //call this function to handle this case	
	       Receipt.updateTicketInfo(ticketno, total, discount, status, paymentMethod);
	    		       
	       //delete items
	       Receipt.deleteItems(ticketno);
	       
	       //put items in the database
	       Receipt.saveItems(products, ticketno);
	       
	       //print the receipt if caller is 1	
		   if(caller == 1)
		   {	
		      //print the receipt
		      printReceipt(products, total, result, taxDollars, customer, format, timeStamp, count, change, cashReceived,
		    		ticketno);
		   }
	    	
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
		  
		  //close the connection
		  conn.close();
		  
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
	public static String createReceipt(Date date, double total, String paymentMethod, double discount,
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
			ps2.setDouble(6, discount);
			ps2.setString(7, status);
			
			//execute query
			rs = ps2.executeQuery();
			
			conn.close();

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
        	 if(!p.getUnitSize().equals(0) && 
        	    (!p.getName().contains("send") && !p.getName().contains("receive")))  
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
        	 else
        	 {
        	    //store money wires in the database	 
        		MoneyWire.createMoneyWireService(Integer.parseInt(Configs.getTempValue("temp20")), Configs.getTempValue("temp25"), Configs.getTempValue("temp22"), 
        				Configs.getTempValue("temp21"), Double.parseDouble(Configs.getTempValue("temp23")), ticketno, Configs.getTempValue("temp24"), Configs.getTempValue("temp26")); 
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
        
		//print the header
		printerService.printString(Configs.getProperty("Printer"), " \n \n \n \t \t" + "   " + Configs.getProperty("StoreName") 
		+ "\n \t \t" + "   " + "Store # " + Configs.getProperty("StoreNumber") 
		+ " \n \t \t" + "" +  Configs.getProperty("StreetAddress")
		+ " \n \t " +  "     " + Configs.getProperty("City") + " " +
		    Configs.getProperty("State") + " " + Configs.getProperty("ZipCode")
		+ " \n \t \t" + " " + Configs.getProperty("PhoneNumber") + "\n\n\n");
		
		//print cashier, sales ticket and date info
		printerService.printString(Configs.getProperty("Printer"), "Transaction Number #: " + "\t" + transaction
		+ " \n" + "Manager:" + "\t\t" + Configs.getProperty("Manager")		
		+ " \n" + "Date: " + "\t\t\t" + date
		+ " \n" + "Time: " + "\t\t\t" + timeStamp
		+ " \n" + "Cashier: " + "\t\t" + Session.getUserFirstName()
		+ " \n" + "Customer: " + "\t\t" + "Bob \n\n"

		
        //print items header
		+ "\t" + "Item" + "\t\t\t" + "Item Total \n" 
		+ "************************************************" + "\n");

		//print items
		for(Product p : products)
		{ 
		   if(p.getQuantity() > 1)
		   { 		      
		      if(p.getName().length() == 30)
		      {	  
				 printerService.printString(Configs.getProperty("Printer"), p.getName()		    	  
			     + "\t\t $ " + setPrecision(p.getPrice()) + "\n");
		      }
		      else if(p.getName().length() < 30)
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
		         printerService.printString(Configs.getProperty("Printer"), name
			     + "\t\t $ " + p.getPrice() + "\n");	         
		      }	  		      
		      
			  printerService.printString(Configs.getProperty("Printer"), "\t" + p.getQuantity() + " @ " + p.getUnitPrice() + " each \n");
		   }	
		   else
		   { 
			   if(p.getName().length() == 30)
			   {	  
		   	      printerService.printString(Configs.getProperty("Printer"), p.getName()		    	  
				  + "\t\t $ " + p.getPrice() + "\n");
			   }
			   else if(p.getName().length() <= 29)
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
	  	          printerService.printString(Configs.getProperty("Printer"), name
				  + "\t\t $ " + p.getPrice() + "\n");		         
			   }	    
		   }       
		}	
		
		//sub-totals, tax, and total
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + "Subtotal: " + "\t $" + subtotal + " \n"
		+ "\t\t" + "Tax " + Configs.getProperty("TaxRate") + "%:" + "\t $" + setPrecision(taxDollars) + "\n"
		+"\t\t" + "Total:  " + "\t $" + total

        //payment method and change
		+ "\n\n\t\t" + "Cash " + "\t\t " + cashReceived + " \n"
		+ "\t\t" + "Change: " + "\t " + change + " \n");		
		
		//items sold
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + "Items Sold: " + "\t" + count + "\n"
		
        //display greeting		
		+ "\n\t\t" + Configs.getProperty("Slogan") + "\n"		
		+ "\n\t\t" + "" + Configs.getProperty("Greeting") + "\n\n\n\n");
		
		// cut the paper
		byte[] cut = new byte[]  {0x1b, 0x69};
		
		//open the cash drawer
		//byte[] openP = new byte[] {0x1B, 0x70, 0x30, 0x37, 0x79};
 
		PrinterService.printBytes(Configs.getProperty("Printer"), cut);
		//PrinterService.printBytes(Configs.getProperty("Printer"), openP);
	}
	
	/*
	 *  Print refund receipt 
	 */
	public static void printRefundReceipt(String ticketno, ObservableList<Product> products, String total, String subtotal)
	{
		PrinterService printerService = new PrinterService();
		
		//compute date
		Date date = new Date();
		
	    String format = new SimpleDateFormat("MM/dd/yyyy").format(date);
	    
	    //compute time
	    String timeStamp = new SimpleDateFormat("HH:mm").format(date);
        
	    //compute tax dollars
	    double taxDollars = Double.parseDouble(total) - Double.parseDouble(subtotal);
	    
		//print the header
		printerService.printString(Configs.getProperty("Printer"), " \n \n \n \t \t" + "   " + Configs.getProperty("StoreName") 
		+ "\n \t \t" + "   " + "Store # " + Configs.getProperty("StoreNumber") 
		+ " \n \t \t" + "" +  Configs.getProperty("StreetAddress")
		+ " \n \t " +  "     " + Configs.getProperty("City") + " " +
		    Configs.getProperty("State") + " " + Configs.getProperty("ZipCode")
		+ " \n \t \t" + " " + Configs.getProperty("PhoneNumber") + "\n\n\n");
		
		//print cashier, sales ticket and date info
		printerService.printString(Configs.getProperty("Printer"), "Transaction Number #: " + "\t" + ticketno
		+ " \n" + "Manager:" + "\t\t" + Configs.getProperty("Manager")		
		+ " \n" + "Date: " + "\t\t\t" + format
		+ " \n" + "Time: " + "\t\t\t" + timeStamp
		+ " \n" + "Cashier: " + "\t\t" + Session.getUserFirstName()
		+ " \n" + "Customer: " + "\t\t" + "Bob \n\n" + 

		
        //print items header
		"\t" + "Item" + "\t\t\t" + "Item Total \n" 
		+ "************************************************" + "\n");

		//print items
		for(Product p : products)
		{ 
		   if(p.getQuantity() > 1)
		   { 		      
		      if(p.getName().length() == 30)
		      {	  
				 printerService.printString(Configs.getProperty("Printer"), p.getName()		    	  
			     + "\t\t $ " + setPrecision(p.getPrice()) + "\n");
		      }
		      else if(p.getName().length() < 30)
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
		         printerService.printString(Configs.getProperty("Printer"), name
			     + "\t\t $ " + p.getPrice() + "\n");		         
		      }	  		      
		      
			  printerService.printString(Configs.getProperty("Printer"), "\t" + p.getQuantity() + " @ " + p.getUnitPrice() + " each \n");
		   }	
		   else
		   { 
			   if(p.getName().length() == 30)
			   {	  
		   	      printerService.printString(Configs.getProperty("Printer"), p.getName()		    	  
				  +"\t\t $ " + p.getPrice() + "\n");
			   }
			   else if(p.getName().length() <= 29)
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
	  	          printerService.printString(Configs.getProperty("Printer"), name
				  + "\t\t $ " + p.getPrice() + "\n");		         
			   }	    
		   }       
		}	
		
		//sub-totals, tax, and total
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + "Subtotal: " + "\t ($" + subtotal + ") \n"
		+ "\t\t" + "Tax " + Configs.getProperty("TaxRate") + "%:" + "\t ($" + setPrecision(taxDollars) + ") \n"
		+ "\t\t" + "Total:  " + "\t ($" + total + ")");

        //payment method and change
		//printerService.printString(Configs.getProperty("Printer"), "\n\n\t\t" + "Cash " + "\t\t " + cashReceived + " \n");
		//printerService.printString(Configs.getProperty("Printer"), "\t\t" + "Change: " + "\t " + change + " \n");		
		
		//items sold
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + "Items Returned: " + "\t" + products.size() + "\n"
		
        //display greeting		
		+ "\n\t\t" + Configs.getProperty("Slogan") + "\n"	
		+ "\n\t\t" + "" + Configs.getProperty("Greeting") + "\n\n\n\n");
		
		// cut the paper
		byte[] cut = new byte[]  {0x1b, 0x69};
		
		//open the cash drawer
		//byte[] openP = new byte[] {0x1B, 0x70, 0x30, 0x37, 0x79};
 
		PrinterService.printBytes(Configs.getProperty("Printer"), cut);		
	}
	
	/*
	 * Set precision of double to two decimal places
	 */
	public static double setPrecision(double d) {
	    return (long) (d * 1e2) / 1e2;
	}
	
	/*
	 * Update basic info about a on-hold ticket
	 */
	public static void updateTicketInfo(String ticketno, double total, double discount, String status, String paymentMethod)
	{
	   String query = "CALL updateTicketInfo(?,?,?,?,?)";	
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, ticketno);
		  ps.setDouble(2, total);
		  ps.setDouble(3, discount);
		  ps.setString(4, status);
		  ps.setString(5, paymentMethod);
		  
		  //execute update
		  ps.executeUpdate();
		  
		  //close the connection
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Delete items belonging to this ticket
	 */
	private static void deleteItems(String ticketno)
	{
	   String query = "CALL deleteItems(?)";	
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, ticketno);
		  
		  //execute
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
	 * Create on hold ticket
	 */
	public static void createOnHoldTicket(ObservableList<Product> products, double total, String paymentMethod, double discount, String status, int customerID)
	{
	   //date
	   Date date = new Date();
	   
	   //create ticket
       String transaction = createReceipt(date, total, paymentMethod, discount, status);
       
       //check if success
       if(!transaction.isEmpty() && transaction != null)
       {
    	  //save items in the database
    	  saveItems(products, transaction);
    	  
    	  //clear items from main screen
    	  MainScreen.resetProductList();
  
    	  AlertBox.display("FASS Nova", "On-hold ticket created successfully");   
       }	
       else
       {
    	  AlertBox.display("FASS Nova", "Could not create on-hold ticket");   
       }	   

	}
	
	/*
	 * Retrieve on hold tickets
	 */
	public static ObservableList<SalesTicket> retrieveTickets()
	{
	   ObservableList<SalesTicket> result = FXCollections.observableArrayList();
	   String query = "CALL retrieveOnHoldTickets(?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, "On-hold");
		  ps.setString(2, Configs.getProperty("StoreCode"));
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     result.add(new SalesTicket(rs.getString(1), rs.getString(2), rs.getDouble(3), "Pending", "On-hold"));	  
		  }	  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return result;
	}
	
	/*
	 * Display on-hold tickets
	 */
	public static void displayOnHoldTickets()
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //table view
	   TableView<SalesTicket> table = SalesTicket.getSalesTicketTable();
	   
	   //observable list
	   ObservableList<SalesTicket> tickets = retrieveTickets();
	   
	   //set table items
	   table.setItems(tickets);
	   
	   //button
	   Button select = new Button("Select", new ImageView(new Image(SalesHistory.class.getResourceAsStream("/res/Apply.png"))));	   
	   Button cancel = new Button("Cancel", new ImageView(new Image(SalesHistory.class.getResourceAsStream("/res/Cancel.png"))));	   
	   
	   //set on action
	   cancel.setOnAction(e -> stage.close());
	   select.setOnAction(new EventHandler<ActionEvent>() {
		 @Override
		 public void handle(ActionEvent event) {
         
		    if(table.getSelectionModel().getSelectedItem() != null)
		    {
		       String query = "CALL listItems(?)";
		       ObservableList<Product> products = FXCollections.observableArrayList();
		       
		       try
		       {
		    	   Connection conn = Session.openDatabase();
		    	   PreparedStatement ps = conn.prepareStatement(query);
		    	   
		    	   //set parameters
		    	   ps.setString(1, table.getSelectionModel().getSelectedItem().getTicketno());
		    	   
		    	   //execute query
		    	   ResultSet rs = ps.executeQuery();
		    	   
		    	   //process the result set
		    	   while(rs.next())
		    	   {
		    		  products.add(new Product(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));   
		    	   }	    
		    	   
		    	   //clear table items
		    	   MainScreen.resetProductList();
		    	   
		    	   //set table items
		    	   MainScreen.setTableItems(products);
		    	   
		    	   //set status
		    	   MainScreen.status = "On-hold";
		    	   
		    	   //set ticket
		    	   MainScreen.ticketNo = table.getSelectionModel().getSelectedItem().getTicketno();
		    
		    	   //close the stage
		    	   stage.close();
		       }
		       catch(Exception e)
		       {
		    	  e.printStackTrace();   
		       }
		    }	
		    else
		    {
		       AlertBox.display("FASS Nova", "Select a ticket");	
		    }	
		 }
		   
	   });
	   
	   //center layout
	   VBox center = new VBox();
	   center.setAlignment(Pos.CENTER);
	   center.setPadding(new Insets(10, 10, 10, 10));
	   center.setSpacing(7);
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setSpacing(7);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to top
	   center.getChildren().add(table);
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, select);
	   
	   //root
	   BorderPane root = new BorderPane();
	   
	   //setup root
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //set id 
	   root.setId("border");
	   
	   //add nodes to root
	   root.setCenter(center);
	   root.setBottom(bottom);
	   
	   //load style sheets
	   root.getStylesheets().add(Receipt.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup stage
	   stage.setTitle("FASS Nova - On-hold tickets");
	   stage.setMinWidth(350);
	   stage.centerOnScreen();
	   stage.initModality(Modality.APPLICATION_MODAL);
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show and wait
	   stage.showAndWait();
	}
}

