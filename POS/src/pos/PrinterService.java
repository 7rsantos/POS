package pos;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
 
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
 
   
public class PrinterService implements Printable {
	
	private static Logger logger = Logger.getLogger(PrinterService.class);
	private static String Name;	
	
	@SuppressWarnings("static-access")
	public PrinterService()
	{ 
		this.Name = "";
	}	
	@SuppressWarnings("static-access")
	public PrinterService(String name)
	{ 
		this.Name = name;
	}
	
	@SuppressWarnings("static-access")
	public String getName()
	{ 
		return this.Name;
	}
	
	public static List<String> getPrinters(){
		
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		
		PrintService printServices[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		
		List<String> printerList = new ArrayList<String>();
		for(PrintService printerService: printServices){
			printerList.add( printerService.getName());
		}
		
		return printerList;
	}

	public ObservableList<String> getPrinterList(){
		
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		
		//lookup printers associated with this computer
		PrintService printServices[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		
		//put each printer name in the list
		ObservableList<String> printerList = FXCollections.observableArrayList();
		for(PrintService printerService: printServices){
			printerList.add( printerService.getName());
		}
		
		return printerList;
	}	
	
	@Override
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}
 
		/*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		/* Now we perform our rendering */
 
		g.setFont(new Font("Roman", 0, 8));
		g.drawString("Hello world !", 0, 10);
 
		return PAGE_EXISTS;
	}
 
	public void printString(String printerName, String text) {
		
		// find the printService of name printerName
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
 
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService service = findPrintService(printerName, printService);
 
		DocPrintJob job = service.createPrintJob();
 
		try {
 
			byte[] bytes;
 
			// important for umlaut chars
			bytes = text.getBytes("CP437");
 
			Doc doc = new SimpleDoc(bytes, flavor, null);
 
			
			job.print(doc, null);
 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("String cannot be printed", e);
		}
 
	}
 
	public static void printBytes(String printerName, byte[] bytes) {
		
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
 
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService service = findPrintService(printerName, printService);
				
        logger.info("Attempting to print using " + service.getName());
		DocPrintJob job = service.createPrintJob();
 
		try {
 
			Doc doc = new SimpleDoc(bytes, flavor, null);
 
			job.print(doc, null);
 
		} catch (Exception e) {
			logger.error("cannot print bytes", e);
		}
	}
	
	private static PrintService findPrintService(String printerName,
			PrintService[] services) {
		for (PrintService service : services) {
			if (service.getName().equalsIgnoreCase(printerName)) {
				return service;
			}
		}
        
		logger.debug("Printer was not found, returning null");
		
		return null;
	}
}
