package pos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class ProductUtilities {

	private static ObservableList<Product> products;
	private static ArrayList<String> list;
	
	/*
	 * Display transfer cash screen
	 */
	public static void displayTransferProduct(ObservableList<Product> allProducts)
	{
	   //initialize properties	
	   products = FXCollections.observableArrayList();
	   list = new ArrayList<String>();
	   
	   //stage
	   Stage stage = new Stage();
	   
	   //button
	   Button accept = new Button("Accept", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Apply.png"))));
	   Button cancel = new Button("Cancel", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Cancel.png"))));	   
	   
	   //choice box
	   ChoiceBox<String> storeLocations = UserDisplay.getStoreCodes();
	   
	   //set on action
	   cancel.setOnAction(e -> stage.close());
	   accept.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		
			//check if it is same store
			if(!Configs.getProperty("StoreCode").equals(storeLocations.getValue()))
			{
			   //update quantities in the database	
			   transferProducts(storeLocations.getValue());
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Cannot transfer products to the same store");	
			}	
		}
		   
	   });
	   
	   //label
	   Label storelbl = new Label("Store");
	   
	   //setup label
	   storelbl.setFont(new Font("Courier Sans", 14));
	   storelbl.setTextFill(Color.WHITE);
	   
	   //root layout
	   VBox root = new VBox();
	   
	   //setup root
	   root.setAlignment(Pos.CENTER);
	   root.setSpacing(7);
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //top layout
	   GridPane top = new GridPane();
	   top.setAlignment(Pos.CENTER);
	   top.setVgap(7);
	   top.setHgap(7);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to top
	   top.add(storelbl, 0, 0); 
	   top.add(storeLocations, 1, 0);
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.setSpacing(7);
	   
	   //add nodes to bottom layout
	   bottom.getChildren().addAll(cancel, accept);
	   
	   //add nodes to root
	   root.getChildren().addAll(top, bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(ProductUtilities.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Transfer Product");
	   stage.setMinWidth(300);
	   stage.centerOnScreen();
	   stage.initModality(Modality.APPLICATION_MODAL);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.showAndWait();
	}
	
	/*
	 *   
	 */
	
	
	/*
	 * Check if products exists in the other store
	 */
	private static boolean productExists(String name, String storeCode)
	{
	   String query = "SELECT Name from Product WHERE Product.Name = ? AND Product.productStore Code = ?";	
	   try
	   {
		  Connection conn = Session.openDatabase();
		  java.sql.PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, storeCode);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //close
		  conn.close();
		  
		  //process
		  if(rs.next())
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
	 * Create product info
	 */
	private static void addProduct(Product p, String storeCode)
	{
	   ObservableList<String> info = FXCollections.observableArrayList();
	   double originalPrice = 0.0; 
	   double salesPrice = 0.0;
	   byte[] imageArray = null;
	   String query1 = "SELECT Barcode, Brand, Category, originalPrice, salesPrice, Photo from Product WHERE Product.Name = ? AND Product.productStoreCode = ?";
	   String query2 = "CALL createProduct(?,?,?,?,?,?,?,?,?,?,?)";
	   try
	   {
		  Connection conn = Session.openDatabase();
		  java.sql.PreparedStatement ps = conn.prepareStatement(query1);
		  
		  //set parameters of first query
		  ps.setString(1, p.getName());
		  ps.setString(2, storeCode);
		  
		  //execute first query
		  ResultSet rs = ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     info.add(rs.getString(1));
		     info.add(rs.getString(2));
		     info.add(rs.getString(3));
		     originalPrice = rs.getDouble(4);
		     salesPrice = rs.getDouble(5);
		     imageArray = rs.getBytes(6);
		  }	  
		  
		  //convert array to file input stream
		  InputStream input = new ByteArrayInputStream(imageArray);
		  
		  //execute second query
		  ps.clearParameters();
		  
		  //set ps
		  ps = conn.prepareStatement(query2);
		  
		  //set parameters
		  ps.setString(1, p.getName());
		  ps.setString(2, info.get(0));
		  ps.setString(3, info.get(1));
		  ps.setString(4, p.getUnitSize());
		  ps.setString(5, info.get(2));
		  ps.setInt(6, p.getQuantity());
		  ps.setDouble(7, originalPrice);
		  ps.setDouble(8, salesPrice);
		  ps.setString(9, storeCode);
		  ps.setString(10, Configs.getProperty("CurrentUser"));
		  ps.setBlob(11, input);
		  
		  //execute query
		  ps.execute();
		  
		  //close the connection
		  conn.close();
				  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Update quantities in the database
	 */
	private static void transferProducts(String storeCode)
	{
	   String query1 = "CALL decreaseProductQuantity(?,?,?)";
	   String query2 = "CALL increaseProductQuantity(?,?,?)";
	   try
	   {
		  Connection conn = Session.openDatabase();
		  
		  //execute first query
		  PreparedStatement ps = conn.prepareStatement(query1);
		  
		  //set parameters
		  for(Product p : products)
		  {
		     if(ProductUtilities.productExists(p.getName(), storeCode))
		     {
		    	//set parameters 
		    	ps.setString(1, p.getName());
		    	ps.setInt(2, p.getQuantity());
		    	ps.setString(3, storeCode);
		    	 
		        //increase other store's product quantity
		    	ps.executeUpdate();
		    	 
		    	//clear parameters
		    	ps.clearParameters();
		    	 
		    	//decrease store's product quantity
		    	PreparedStatement pst = conn.prepareStatement(query2);
		    	
		    	//set parameters
		    	pst.setString(1, p.getName());
		    	pst.setInt(2, p.getQuantity());
		    	pst.setString(3, Configs.getProperty("StoreCode"));
		    	
		    	//execute update
		    	pst.executeUpdate();
		    	
		    	//display success
		    	AlertBox.display("FASS Nova", "Products transferred successfully");
		     }	 
		     else
		     {
		        ProductUtilities.addProduct(p, storeCode);	 
		     } 	 
		  }	  
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Put ticket on hold
	 */
}
