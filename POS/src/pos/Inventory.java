package pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Inventory {

private static TextField photoPath;
private static ImageView productPicture;
private static TextField name;
private static TextField barcode;
private static TextField brand;
private static TextField unitSize;
private static TextField category;
private static TextField unitNumber;
private static TextField originalPrice;
private static TextField salesPrice;
private static File f;
private static Stage window;

	public static void displayAddInventory()
	{ 
		   window = new Stage();
		   
		   window.setTitle("FASS Nova - Register New Product");
		   window.initModality(Modality.APPLICATION_MODAL);
		   window.setMinWidth(250);
		   window.setWidth(650);
		   window.setHeight(500);
		   window.setResizable(false);	
		   
		   Text title = new Text("Register New Product");
		   
		   //setup title properties
		   title.setFill(Color.WHITE);
		   title.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		   
		   //create box to hold title
		   HBox top = new HBox();
		   
		   //set top's alignment
		   top.setAlignment(Pos.TOP_CENTER);
		   top.setPadding(new Insets(10, 10, 2, 10));
		   
		   //add title to HBox
		   top.getChildren().add(title);
		   //setup the layout of the scene
		   BorderPane root = new BorderPane();
		   
		   //create labels and text fields
		   Label nameLb = new Label("Product Name");
		   Label barcodeLb = new Label("Barcode");
		   Label brandLb = new Label("Brand");
		   Label unitSizeLb = new Label("Unit Size");
		   Label categoryLb = new Label("Category");
		   Label unitNumberLb = new Label("Number of Units");
		   Label originalPriceLb = new Label("Original Price per unit");
		   Label salesPriceLb = new Label("Sales Price per unit");
		   
		   //set label colors
		   nameLb.setTextFill(Color.WHITE);
		   barcodeLb.setTextFill(Color.WHITE);
		   brandLb.setTextFill(Color.WHITE);
		   unitSizeLb.setTextFill(Color.WHITE);
		   categoryLb.setTextFill(Color.WHITE);
		   originalPriceLb.setTextFill(Color.WHITE);
		   salesPriceLb.setTextFill(Color.WHITE);
		   nameLb.setTextFill(Color.WHITE);
		   nameLb.setTextFill(Color.WHITE);
		   unitNumberLb.setTextFill(Color.WHITE);
		   
		   //create text fields
	       name = new TextField();
		   barcode = new TextField();
		   brand = new TextField();
		   unitSize = new TextField();
		   category = new TextField();
		   unitNumber = new TextField();
		   originalPrice = new TextField();
		   salesPrice = new TextField();
		   photoPath = new TextField();
		   
		   //create images
		   Image cancelIcon = new Image(Inventory.class.getResourceAsStream("/res/Cancel.png"));
		   Image addProduct = new Image(Inventory.class.getResourceAsStream("/res/Create.png"));
		   Image cameraIcon = new Image(Inventory.class.getResourceAsStream("/res/camera.png"));
		   
		   //create add and cancel buttons
		   Button cancel = new Button("Cancel", new ImageView(cancelIcon));
		   Button add = new Button("Add", new ImageView(addProduct));
		   Button takePhoto = new Button("Take Photo", new ImageView(cameraIcon));
		   Button select = new Button("Select Image");
		   
		   //create the photo icon
		   Image image = new Image(Inventory.class.getResourceAsStream("/res/photo.png"));
		   
		   //create image view for product
		   productPicture = new ImageView(image);
		   
           //implement buttons functionalities
		   select.setOnAction(e -> selectImageFile(photoPath, productPicture));
		   takePhoto.setOnAction(e -> PhotoScreen.displayPhotoScreen(2));
		   cancel.setOnAction(e -> window.close());
		   add.setOnAction(e -> addProduct());
		   
		   //create VBox Layout that will hold image options
		   VBox photoLayout = new VBox();
		   photoLayout.setPadding(new Insets(10, 10, 30, 10));
		   
		   //create a HBox to hold file chooser and text field
		   HBox fileSelection = new HBox();
		   fileSelection.setSpacing(2);
		   
		   //add nodes to the file selection
		   fileSelection.getChildren().addAll(select, photoPath);

		   
		   //add nodes to photoLayout
		   photoLayout.setSpacing(7);
		   photoLayout.getChildren().addAll(productPicture, takePhoto, fileSelection);
		   
		   //create grid pane layout
		   GridPane layout = new GridPane();
		   layout.setAlignment(Pos.CENTER);
		   layout.setVgap(13);
		   
		   //add nodes to the grid pane
		   //layout.add(title, 1, 0);
		   layout.add(nameLb, 0, 1);
		   layout.add(name, 1, 1);
		   layout.add(barcodeLb, 0, 2);
		   layout.add(barcode, 1, 2);
		   layout.add(brandLb, 0, 3);
		   layout.add(brand, 1, 3);
		   layout.add(unitSizeLb, 0, 4);
		   layout.add(unitSize, 1, 4);
		   layout.add(categoryLb, 0, 5);
		   layout.add(category, 1, 5);
		   layout.add(unitNumberLb, 0, 6);
		   layout.add(unitNumber, 1, 6);
		   layout.add(originalPriceLb, 0, 7);
		   layout.add(originalPrice, 1, 7);
		   layout.add(salesPriceLb, 0, 8);
		   layout.add(salesPrice, 1, 8);

		   //set form's layout
		   layout.setPadding(new Insets(10, 15, 30, 10));
		   
		   //create an hbox to hold buttons
		   HBox bottom = new HBox();
		   
		   //set bottom layout's positioning
		   bottom.setSpacing(17);
		   bottom.setAlignment(Pos.BOTTOM_CENTER);
		   bottom.setPadding(new Insets(5, 10, 5, 10));
		   
		   //add buttons to the bottom border
		   bottom.getChildren().addAll(add, cancel);
		   
		   		   
		   //add borders to the root
		   root.setTop(top);
		   root.setLeft(layout);
		   root.setRight(photoLayout);
		   root.setBottom(bottom);
		   
		   //set ids
		   top.setId("top");
		   layout.setId("left");
		   photoLayout.setId("right");
		   bottom.setId("bottom");
		   root.setId("root");
		   
		   //create scene and add layout to the scene
		   Scene scene = new Scene(root);
		   
		   //load resources
		   scene.getStylesheets().add(Inventory.class.getResource("AddInventory.css").toExternalForm());
		   
		   //show window
		   window.setScene(scene);
		   window.showAndWait();  
	}
	
	private static void addProduct() {
		
		String productName = name.getText();
		String productBrand = brand.getText();
		String productSize = unitSize.getText();
	    String productCategory = category.getText();
	    double productUnits = Double.parseDouble(unitNumber.getText());
	    double productPrice = Double.parseDouble(salesPrice.getText());
	    double productOriginal = Double.parseDouble(originalPrice.getText());
        String query = "CALL createProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        String productBarcode = barcode.getText();

		try   {
		   Connection myConn = Session.openDatabase();
		   
		   //prepare statement
		   PreparedStatement ps = myConn.prepareStatement(query);
		   FileInputStream input = new FileInputStream(f);
		   
		   //set parameters
		   ps.setString(1, productName);
		   ps.setString(2, productBarcode);
		   ps.setString(3, productBrand);
		   ps.setString(4, productSize);
		   ps.setString(5, productCategory);
		   ps.setDouble(6, productUnits);
		   ps.setDouble(7, productOriginal);
		   ps.setDouble(8, productPrice);
		   ps.setString(9, Configs.getProperty("CurrentUser"));
		   ps.setString(10, Configs.getProperty("StoreCode"));
		   ps.setBlob(11, input);
		
		   //execute query
		   ps.executeQuery();
		  
		   //report success
		   AlertBox.display("FASS Nova", "Product registered succesfully!");
		   
		   //close the window
		   window.close();
		}
		catch(Exception e)
		{ 
			AlertBox.display("FASS Nova - Error", "Could not add product, try again");
			e.printStackTrace();
		}
	}

	public static void selectImageFile(TextField path, ImageView product)
	{ 
		
		//create image file chooser
	    FileChooser fileChooser = Icon.createImageChooser();
		   
        //create file that will hold the image path
    	File selectedFile = Icon.selectImage(fileChooser);
		   
	    //process the selected file path
  	    if(selectedFile != null)
		{ 
		   path.setText(selectedFile.getAbsolutePath());
		   String imageUrl = "";
		   try {
			  imageUrl = selectedFile.toURI().toURL().toExternalForm();
			  
			  f = selectedFile;
			  
		   } catch (MalformedURLException e) {

			   AlertBox.display("FASS NOVA - Error", "Could not select image");
			   e.printStackTrace();
		   }
		   Image image = new Image(imageUrl);
		   
		   //set image view dimensions
		   product.setFitWidth(200);
		   product.setFitHeight(250);
		   
		   product.setImage(image);
		}	   
		else
		{ 
			AlertBox.display("FASS Nova - Selection Error", "No File Selected"); 			
        }	  		
	}
	
	public static void setPicturePath(File path)
	{ 
	   	
	   f = path;	
	   photoPath.setText(path.getAbsolutePath());
	   String url = ""; 
	   try
	   {
		  url = path.toURI().toURL().toExternalForm(); 
		  Image photo = new Image(url); 
	      productPicture.setImage(photo);
	   }   
	   catch(MalformedURLException e)
	   { 
		   AlertBox.display("FASS Nova - Error", "Could not upload image");
		   e.printStackTrace();
	   }
	   productPicture.setFitHeight(250);
	   productPicture.setFitWidth(200);
	}
	
	/*
	 * Update product information
	 */
	public static void displayProductUpdate(String productName)
	{
	   //stage
	   Stage stage = new Stage();
	   	   
	   //create labels
	   Label namelbl = new Label("Name");
	   Label barcodelbl = new Label("Barcode");
	   Label brandlbl = new Label("Brand");
	   Label unitlbl = new Label("UnitSize");
	   Label categorylbl = new Label("Category");
	   Label originallbl = new Label("Original Price");
	   Label saleslbl = new Label("Sales Price");

	  	
	  	//change font color
	   namelbl.setTextFill(Color.WHITE);
	   barcodelbl.setTextFill(Color.WHITE);
	   brandlbl.setTextFill(Color.WHITE);
	   unitlbl.setTextFill(Color.WHITE);
	   categorylbl.setTextFill(Color.WHITE);
	   originallbl.setTextFill(Color.WHITE);
	   saleslbl.setTextFill(Color.WHITE);

	   //initialize text fields
	   name = new TextField();
	   barcode = new NumericTextField();
	   brand = new TextField();
	   unitSize = new TextField();
	   category = new TextField();
	   salesPrice = new NumericTextField();
	   originalPrice = new NumericTextField();
	   photoPath = new TextField();
	   
	   //set values
	   ObservableList<String> result = Inventory.listProductInfo(productName);
	   name.setText(result.get(0));
	   barcode.setText(result.get(1));
	   unitSize.setText(result.get(2));
	   brand.setText(result.get(3));
	   category.setText(result.get(4));
	   salesPrice.setText(result.get(5));
	   originalPrice.setText(result.get(6));
	   
	   //buttons
	   Button done = new Button("Done", new ImageView(new Image(UserDisplay.class.getResourceAsStream("/res/Apply.png"))));
	   Button update1 = new Button("Update");
	   Button update2 = new Button("Update");
	   Button update3 = new Button("Update");
	   Button update4 = new Button("Update");
	   Button update5 = new Button("Update");
	   Button update6 = new Button("Update");
	   Button update7 = new Button("Update");
	   Button update8 = new Button("Update");
	   Button takePicture = new Button("Take Picture");
	   Button select = new Button("Select", new ImageView(new Image(UserDisplay.class.getResourceAsStream("/res/Apply.png"))));
	   
	   //set on action
	   update1.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(name.getText() != null && !name.getText().isEmpty())
			{
			   //update product name
			   Inventory.updateProductName(productName, name.getText());	
				
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });
 
	   update2.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(barcode.getText() != null && !barcode.getText().isEmpty())
			{
			   //update product barcode	
			   Inventory.updateProductBarcode(productName, barcode.getText());	
			   
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });
	   
	   update3.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(brand.getText() != null && !brand.getText().isEmpty())
			{
			   //update product brand	
			   Inventory.updateProductBrand(productName, brand.getText());	
			   
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });
	   
	   update4.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(unitSize.getText() != null && !unitSize.getText().isEmpty())
			{
			   //update product unit size
			   Inventory.updateProductUnitSize(productName, unitSize.getText());	
			   
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });
	   
	   update5.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(category.getText() != null && !category.getText().isEmpty())
			{
			   //update product category
			   Inventory.updateProductCategory(productName, category.getText());
			   
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });

	   update6.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(originalPrice.getText() != null && !originalPrice.getText().isEmpty())
			{
			   //update product original price
			   Inventory.updateProductOriginalPrice(productName, Double.parseDouble(originalPrice.getText()));
			   
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });
	   
	   update7.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(salesPrice.getText() != null && !salesPrice.getText().isEmpty())
			{
			   //update product sales price
			   Inventory.updateProductSalesPrice(productName, Double.parseDouble(salesPrice.getText()));
			   
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });
	   
	   update8.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(photoPath.getText() != null && !photoPath.getText().isEmpty())
			{
			   //update photo path
			   Inventory.updateProductPhoto(productName);	
			   
			   //display success
			   AlertBox.display("FASS Nova", "Update successful!");	
			}	
			else
			{
			   AlertBox.display("FASS Nova", "Fill in required field");	
			}	
		}
		   
	   });
	   
	   //image view
	   productPicture = new ImageView(ProductList.getProductPicture(productName));
	   productPicture.setFitHeight(250);
	   productPicture.setFitWidth(200);
	   
	   //form layout
	   GridPane left = new GridPane();
	   
	   //setup top
	   left.setHgap(7);
	   left.setVgap(7);
	   left.setPadding(new Insets(10, 10, 10, 10));
	   left.setAlignment(Pos.CENTER);
	   
	   //setup top
	   left.add(namelbl, 0, 0);
	   left.add(name, 1, 0);
	   left.add(update1, 2, 0);
	   left.add(barcodelbl, 0, 1);
	   left.add(barcode, 1, 1);
	   left.add(update2, 2, 1);
	   left.add(brandlbl, 0, 2);
	   left.add(brand, 1, 2);
	   left.add(update3, 2, 2);
	   left.add(unitlbl, 0, 3);
	   left.add(unitSize, 1, 3);
	   left.add(update4, 2, 3);
	   left.add(categorylbl, 0, 4);
	   left.add(category, 1, 4);
	   left.add(update5, 2, 4);
	   left.add(originallbl, 0, 5);
	   left.add(originalPrice, 1, 5);
	   left.add(update6, 2, 5);
	   left.add(saleslbl, 0, 6);
	   left.add(salesPrice, 1, 6);
	   left.add(update7, 2, 6);
	   
	   //set on action
	   done.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
           
		   //close main screen
		   MainScreen.closeStage();
		   
		   //close
		   stage.close();
		   
		   //back to main screen
		   PaymentScreen.backToMainScreen(stage, 2);
		}
		   
	   });
	   select.setOnAction(e -> selectImageFile(photoPath, productPicture));
	   takePicture.setOnAction(e -> PhotoScreen.displayPhotoScreen(2));
	   
	   //right layout
	   VBox right = new VBox();
	   right.setSpacing(7);
	   right.setAlignment(Pos.CENTER);
	   
	   //photo layout
	   HBox photoLayout = new HBox();
	   photoLayout.setSpacing(7);
	   photoLayout.getChildren().addAll(update8, photoPath);
	   
	   //button
	   HBox topPhotoLayout = new HBox();
	   topPhotoLayout.setSpacing(7);
	   topPhotoLayout.getChildren().addAll(select, takePicture);
	   
	   //add nodes to right
	   right.getChildren().addAll(productPicture, topPhotoLayout, photoLayout);
	   
	   //bottom
	   VBox bottom = new VBox();
	   
	   //setup bottom
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setSpacing(7);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(done);
	   
	   //root
	   BorderPane root = new BorderPane();
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //setup root
	   root.setLeft(left);
	   root.setBottom(bottom);
	   root.setRight(right);
	   	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(UserDisplay.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Update Product Info");
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(350);
	   stage.centerOnScreen();
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Update user product name
	 */
	private static void updateProductName(String name, String newName)
	{
	   String query = "CALL updateProductName(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, newName);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}

	/*
	 * Update user product barcode
	 */
	private static void updateProductBarcode(String name, String barcode)
	{
	   String query = "CALL updateBarcode(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, barcode);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Update user product name
	 */
	private static void updateProductUnitSize(String name, String unit)
	{
	   String query = "CALL updateUnitSize(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, unit);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Update user product brand
	 */
	private static void updateProductBrand(String name, String brand)
	{
	   String query = "CALL updateBrand(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, brand);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Update user product name
	 */
	private static void updateProductCategory(String name, String category)
	{
	   String query = "CALL updateProductCategory(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, category);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Update user product sales price
	 */
	private static void updateProductSalesPrice(String name, double price)
	{
	   String query = "CALL updateSalesPrice(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setDouble(2, price);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Update user product original price
	 */
	private static void updateProductOriginalPrice(String name, double price)
	{
	   String query = "CALL updateOriginalPrice(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setDouble(2, price);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Update user product name
	 */
	private static void updateProductPhoto(String name)
	{
	   String query = "CALL updateProductPhoto(?,?,?)";
	   
	   try
	   {
		  FileInputStream input = new FileInputStream(f); 
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setBlob(2, input);
		  ps.setString(3, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * List product info
	 */
	private static ObservableList<String> listProductInfo(String name)
	{
	   String query = "CALL listProductInfo(?,?)";
	   ObservableList<String> result = FXCollections.observableArrayList();
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ResultSet rs = ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     result.add(rs.getString(1));
		     result.add(rs.getString(2));	  
		     result.add(rs.getString(3));	  
		     result.add(rs.getString(4));	  
		     result.add(rs.getString(5));	  
		     result.add(rs.getString(6));	  
		     result.add(rs.getString(7));	  
		  }	  
		  
		  //close
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return result;
	}
}
