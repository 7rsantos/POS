package pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

public class RegisterUtilities {

	private static Logger logger = Logger.getLogger(RegisterUtilities.class);
	
	/*
	 *  Increase cash after each cash sale
	 */
	public static void increaseCash(double amount)
	{ 
	   String query1 = "CALL updateExpectedCash(?,?)";
	   double oldAmount = getExpectedCash();
	   	   
	   try
	   { 
	       java.sql.Connection conn = Session.openDatabase();
	       
	       PreparedStatement ps = conn.prepareCall(query1);
	       
	       //set parameters
	       ps.setDouble(1, amount + oldAmount);
	       ps.setInt(2, Integer.parseInt(Configs.getProperty("Register")));
	       
	       //execute query
	       ps.executeUpdate();
	       	       
	       //close the connection
	       ps.close();
	       conn.close();
	       
	       double expected = amount + oldAmount;
	       
	       logger.info(" New Expected cash " + amount + " + " + oldAmount + " =" + expected);
	       
	   }
	   catch(Exception e)
	   { 
		  logger.error("Could not update expected cash");  
	   }
	}
	
	/*
	 * Get the expected cash
	 */
	public static double getExpectedCash()
	{ 
		String query1 = "SELECT expectedCash FROM auditcashhistory WHERE auditCashHistory.registerID = (SELECT Register.`ID` FROM Register WHERE Register.ID = ? AND Register.registerStoreCode = ?) ";
		double expected = 0.0;
		   
	    try
		{ 
	       Connection conn = Session.openDatabase();
		      
 	       PreparedStatement ps = conn.prepareCall(query1);
		      
		   //set parameters
		   ps.setInt(1, Integer.parseInt(Configs.getProperty("Register")));
		   ps.setString(2, Configs.getProperty("StoreCode"));
		      
		   //execute query
		   ResultSet rs = ps.executeQuery();
		      
		   while(rs.next())
		   { 

			   
		      expected = rs.getDouble(1);
		      
		   }
		   
		   rs.close();
		   ps.close();
		   conn.close();
		   
		}
	    catch(Exception e)
	    { 
	       logger.error("Could not get expected cash ", e);	
	    }
	    	    
	    return expected;
	}
	
	/*
	 * Store the totals in the audit cash history
	 */
	public static void auditCash(double total, String notes)
	{ 
	   double expected = 0.0;
	   
	   try
	   {
		  // get expected cash
		   expected = getExpectedCash();
		   
	      //execute query
	      String query = "CALL createAuditCash(?, ?, ?, ?, ?,?)";
	      
	      Connection conn = Session.openDatabase();
	      
	      PreparedStatement ps = conn.prepareCall(query);
	      
	      //set parameters
	      ps.setDouble(1, total);
	      ps.setDouble(2, total);
	      ps.setDouble(3, total - expected);
	      ps.setInt(4, Integer.parseInt(Configs.getProperty("Register")));
	      ps.setString(5, Configs.getProperty("CurrentUser"));
	      ps.setString(6, notes);
	      
	      //execute query
	      ps.execute();
	      
	      //close the connection
	      ps.close();
	      conn.close();
	      
	      logger.info("Audit saved to the database");
	   }
	   catch(Exception e)
	   { 
		  logger.error("Could not create audit in the database", e);   
	   }
	}
	
	/*
	 *  Increase cash after each cash sale
	 */
	public static void transferCash(double amount, String storeCode)
	{ 
	   String query1 = "CALL transferCash(?,?,?)";
	   double oldAmount = RegisterUtilities.getExpectedCash();
	   
	   try
	   { 
	       java.sql.Connection conn = Session.openDatabase();
	       
	       PreparedStatement ps = conn.prepareCall(query1);
	       
	       //set parameters
	       //ps.setString(1, dateFormat);
	       ps.setDouble(1, amount + oldAmount);
	       ps.setInt(2, Integer.parseInt(Configs.getProperty("Register")));
	       ps.setString(3, storeCode);
	       
	       //execute query
	       ps.executeUpdate();
	       	       
	       //close the connection
	       ps.close();
	       conn.close();
	       
	   }
	   catch(Exception e)
	   { 
		  logger.error("Could not transfer cash", e);   
	   }
	}
	
	/*
	 * Display change tax rate screen
	 * 
	 */
	public static void displayTaxRateUpdate()
	{
	   Stage stage = new Stage();
	   
	   //root
	   VBox root = new VBox();
	   
	   //label
	   Label taxlbl = new Label("Tax Rate");
	   
	   //set label
	   taxlbl.setTextFill(Color.WHITE);
	   
	   //text fields
	   NumericTextField tax = new NumericTextField();
	   tax.setText(Configs.getProperty("TaxRate"));
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(RegisterUtilities.class.getResourceAsStream("/res/Apply.png"))));
	   Button cancel = new Button("Cancel", new ImageView(new Image(RegisterUtilities.class.getResourceAsStream("/res/Cancel.png"))));
	   
	   //set on action
	   cancel.setOnAction(e -> stage.close());
	   accept.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   //check if not null
		   if(tax.getText() != null && !tax.getText().isEmpty())
		   {
			   updateTaxRate(tax.getText());   
			   
			   //report success
			   AlertBox.display("FASS Nova", "Tax Rate updated successfully");
			   
			   //close main screen
			   MainScreen.closeStage();
			   
			   //close
			   stage.close();
			   
			   //main screen
			   PaymentScreen.backToMainScreen(stage, 2);
		   }	
		   else
		   {
		      AlertBox.display("FASS Nova", "Fill in required field");	   
		   }	   
		}
		   
	   });
	   
	   //bottom
	   HBox bottom = new HBox();
	   bottom.setSpacing(7);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(accept, cancel);
	   
	   //top
	   HBox top = new HBox();
	   top.setSpacing(7);
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to top
	   top.getChildren().addAll(taxlbl, tax);
	   
	   //setup root
	   root.setSpacing(7);
	   root.setAlignment(Pos.CENTER);
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //add nodes to root
	   root.getChildren().addAll(top, bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(RegisterUtilities.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Update Tax Rate");
	   stage.setMinWidth(300);
	   stage.centerOnScreen();
	   stage.initModality(Modality.APPLICATION_MODAL);
	   
	   //scene
	   stage.setScene(new Scene(root));
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Update tax rate
	 */
	public static void updateTaxRate(String tax)
	{
	   String query = "CALL updateTaxRate(?,?,?)";
	   
	   try
	   {
	      Connection conn = Session.openDatabase();
	      PreparedStatement ps = conn.prepareStatement(query);
	      
	      //set parameters
	      ps.setInt(1, Integer.parseInt(Configs.getProperty("Register")));
	      ps.setDouble(2, Double.parseDouble(tax));
	      ps.setString(3, Configs.getProperty("StoreCode"));
	      
	      //execute
	      ps.executeUpdate();
	      
	      //close
	      ps.close();
	      conn.close();
	      
	      //save locally
	      Configs.saveProperty("TaxRate", tax);
	      
	      //log
	      logger.info("Tax rate was updated sucessfully");
	      
	   }
	   catch(Exception e)
	   {
		  logger.error("Tax could not be updated", e);   
	   }
	}
}
