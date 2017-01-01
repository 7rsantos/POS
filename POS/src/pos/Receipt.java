package pos;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.ObservableList;

public class Receipt {
	/*
	 * Build the receipt, compute totals
	 * 
	 */
	public static void setupReceipt( String customer)
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
	    
	    //compute time
	    String timeStamp = new SimpleDateFormat("HH:mm").format(date);
	    
	    //get tax dollars and sub total
	    DecimalFormat df = new DecimalFormat("#.##");
	    double total = Product.computeTotal(Double.toString(result), Configs.getProperty("TaxRate") + "%");
	    double taxDollars = result * (Double.parseDouble(Configs.getProperty("TaxRate"))/100);
	   
	    System.out.print(taxDollars);
	    
	    total = Double.parseDouble(df.format(total));
	    //taxDollars  = Double.parseDouble(df.format(taxDollars));
	    
	    //print the receipt
	    printReceipt(products, total, result, taxDollars, customer, format, timeStamp, count);
	}
	
	/*
	 * Print the receipt
	 * @param products the list of the products and quantities
	 * @param total the total for the receipt
	 * @param customer the name of the customer
	 */
	public static void printReceipt(ObservableList<Product> products, double total, double subtotal, 
			double taxDollars, String customer, String date, String timeStamp,
			int count) 
	{
		
		// TODO Auto-generated method stub
		PrinterService printerService = new PrinterService();
		
		System.out.println(printerService.getPrinters());
        String transaction = "1.1.17.1";
		
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
		printerService.printString(Configs.getProperty("Printer"), " \n" + "Cashier: " + "\t\t" + "Bill \n\n");

        //print items header
		printerService.printString(Configs.getProperty("Printer"), "\t" + "Item" + "\t\t\t" + "Item Total \n");
		printerService.printString(Configs.getProperty("Printer"), "************************************************" + "\n");

		//print items
		printerService.printString(Configs.getProperty("Printer"), "How Soccer Explains the world?" + "\t\t" + "$ 19.99 \n");
		printerService.printString(Configs.getProperty("Printer"), "Pristine Water"+ "                " + "\t\t" + "$ 1.99 \n");
		
		
		//subtotals, tax, and total
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t\t" + "Subtotal: " + "\t $" + subtotal + " \n");
		printerService.printString(Configs.getProperty("Printer"), "\t\t\t" + "Tax 9.25%:" + "\t $" + setPrecision(taxDollars) + "\n");
		printerService.printString(Configs.getProperty("Printer"), "\t\t\t" + "Total:  " + "\t $" + total);

        //payment method and change
		printerService.printString(Configs.getProperty("Printer"), "\n\n\t\t\t" + "Cash " + "\t\t" + "$ 25.00 \n");
		printerService.printString(Configs.getProperty("Printer"), "\t\t\t" + "Change: " + "\t" + "$ 1.02 \n");		
		
		//items sold
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t\t" + "Items Sold: " + "\t" + count + "\n");
		
        //display greeting		
		printerService.printString(Configs.getProperty("Printer"), "\n\t\t" + Configs.getProperty("Slogan") + "\n");		
		printerService.printString(Configs.getProperty("Printer"), "\n\t" + "    " + Configs.getProperty("Greeting") + "\n\n\n\n");
		
		// cut that paper
		byte[] cut = new byte[]  {0x1b, 0x69};
		
		//open the cash drawer
		byte[] openP = new byte[] {0x1B, 0x70, 0x30, 0x37, 0x79};
 
		printerService.printBytes(Configs.getProperty("Printer"), cut);
		printerService.printBytes(Configs.getProperty("Printer"), openP);
	}
	
	public static double setPrecision(double d) {
	    return (long) (d * 1e2) / 1e2;
	}
}
