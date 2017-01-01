package pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
		   //title.setEffect(dropShadow);		   
		   
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
		   // TODO Auto-generated method stub
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
		   } catch (MalformedURLException e) {
			   // TODO Auto-generated catch block
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
	
}
