package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DamagedInventory {

   private static Logger logger = Logger.getLogger(DamagedInventory.class);	
	
   public static void displayDamagedInventory(String name, String unit)
   {
	   //stage
	   Stage stage = new Stage();
	   
	   //root layout
	   BorderPane root = new BorderPane();
	   
	   //text fields
	   NumericTextField quantity = new NumericTextField();
	   
	   //labels
	   Label quantitylbl = new Label("Type in number of units to be deducted");
	   Label title = new Label(name);
	   
	   //set fill
	   quantitylbl.setTextFill(Color.WHITE);
	   title.setTextFill(Color.WHITE);
	   
	   //top layout
	   FlowPane top = new FlowPane();
	   
	   //setup top
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 10, 10));

	   //add nodes to top
	   top.getChildren().add(title);
	   
	   //center
	   VBox center = new VBox();
	   
	   //setup center
	   center.setAlignment(Pos.CENTER);
	   center.setPadding(new Insets(10, 10, 10, 10));
	   center.setSpacing(7);
	   
	   //add nodes to center
	   center.getChildren().addAll(quantitylbl, quantity);
	   
	   //imageview
	   Image productImage = ProductList.getProductPicture(name);
	   ImageView productPicture = new ImageView(productImage);
	   
	   //set fit width and height
	   productPicture.setFitWidth(200);
	   productPicture.setFitHeight(200);
	   
	   //buttons
	   Button cancel = new Button("Cancel", new ImageView(new Image(Inventory.class.getResourceAsStream("/res/Cancel.png"))));
	   Button accept = new Button("Accept", new ImageView(new Image(Inventory.class.getResourceAsStream("/res/Apply.png"))));
	   
	   //set on action
	   cancel.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		
		   //stage
		   stage.close();
		   
		   //back to main screen
		   PaymentScreen.backToMainScreen(stage, 2);
		}
		   
	   });
	   accept.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		   if(quantity.getText() != null && !quantity.getText().isEmpty())
		   {
			   
			   int n = Integer.parseInt(quantity.getText());
			   
		 	   if(n > 0)
		 	   {
		 		   //update quantity in the database
		 		   updateAmounts(name, unit, n);
		 		   		 	
		 		   //update units available (decrease)
		 		   Inventory.updateAmount(name, n * (-1));
		 		   
		 		   //display success
		 		   AlertBox.display("FASS Nova", "Success!");
		 		   
		 		   //close the stage
		 		   stage.close();
		 		   
		 		   //go to main screen
		 		   PaymentScreen.backToMainScreen(stage, 2);
		 	   }	
		 	   else
		 	   {
		 		   AlertBox.display("FASS Nova", "Invalid input");   
		 	   }	   
		   }
		   else
		   {
		      AlertBox.display("FASS Nova", "Fill in required fields");	   
		   }	   
		}
		   
	   });
	   
	   //bottom
	   HBox bottom = new HBox();
	   
	   //setup bottom
	   bottom.setSpacing(7);
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, accept);
	   
	   //add nodes to root
	   root.setTop(top);
	   root.setCenter(center);
	   root.setRight(productPicture);
	   root.setBottom(bottom);
	   
	   //setup root
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(Inventory.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Damaged/Expired Inventory");
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(300);
	   stage.centerOnScreen();	   
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();      	   
   }
   
   /*
    * update amounts in the database
    */
   private static void updateAmounts(String name, String unit, int quantity)
   {
	  String query1 = "CALL createDamagedInventory(?,?,?,?,?,?)";
	  String query2 = "SELECT salesPrice from Product WHERE Product.Name = ? AND Product.productStoreCode = ?";
	  Date date = new Date();
	  String date1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
	  
      try
      {
         Connection conn = Session.openDatabase();
         PreparedStatement ps = conn.prepareStatement(query2);
         
         //set parameters
         ps.setString(1, name);
         ps.setString(2, Configs.getProperty("StoreCode"));
         
         //execute
         ResultSet rs = ps.executeQuery();
         
         double price = 0.0;
         
         //process
         while(rs.next())
         {
             price = rs.getDouble(1); 	 
         } 	 
         
         //clear
         ps.clearParameters();
         
         //set second query
         ps = conn.prepareStatement(query1);
         
         //set parameters
         ps.setString(1, name);
         ps.setString(2, unit);
         ps.setDouble(3, price);
         ps.setString(4, date1);
         ps.setInt(5, quantity);
         ps.setString(6, Configs.getProperty("StoreCode"));
         
         //execute
         ps.executeQuery();
         
         //close
         ps.close();
         conn.close();
      }
      catch(Exception e)
      {
         logger.error("Could not update damaged inventory", e);	  
      }
   }
}
