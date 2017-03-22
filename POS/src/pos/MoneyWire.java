package pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MoneyWire {
   
   private static ImageView logoPicture;
   private static TextField photo;
   private static File file;
   
   //Display the first screen
   public static void displaySendReceiveScreen()
   {
	  //stage
	  Stage stage = new Stage();
	  
	  //root
	  HBox root = new HBox();
	  
	  //setup root
	  root.setAlignment(Pos.CENTER);
	  root.setPadding(new Insets(20, 20, 20, 20));
	  root.setSpacing(7);
	  
	  //buttons
	  Button send = new Button("Send Money",new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/send.png"))));
	  Button receive = new Button("Receive Money",new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/receive.png"))));
	  
	  //set on action
	  send.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   //close the stage
		   stage.close();
		   
		   //display next screen
		   MoneyWire.displayMoneyWireCompanies(1);
		}
	  });
	  receive.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   //close the stage
		   stage.close();
		   
		   //display next screen
		   MoneyWire.displayMoneyWireCompanies(2);
		}
	  });
	  
	  
	  //add nodes to children
	  root.getChildren().addAll(send, receive);
	  
	  //set id
	  root.setId("border");
	  
	  //load style sheets
	  root.getStylesheets().add(MoneyWire.class.getResource("MainScreen.css").toExternalForm());
	  
	  //scene
	  Scene scene = new Scene(root);
	  
	  //setup stage
	  stage.setTitle("FASS Nova - Money Wire");
	  stage.setMinWidth(300);
	  stage.initModality(Modality.APPLICATION_MODAL);
	  stage.centerOnScreen();
	  
	  //set scene
	  stage.setScene(scene);
	  
	  //show
	  stage.showAndWait();
   }
   
   /*
    * Create new money wire company
    */
   private static void createMoneyWireCompany(String name, String address, String city, 
		                String state, String country, int zipCode, String manager, String phone, String createdBy,
		                int caller)
   {
	  String query = "CALL createSRCompany(?,?,?,?,?,?,?,?,?,?)";  
	  try
	  {
	     Connection conn = Session.openDatabase();
	     PreparedStatement ps = conn.prepareStatement(query);
	     
	     //logo
	     FileInputStream logo = new FileInputStream(file);
	     
	     //set parameters
	     ps.setString(1, name);
	     ps.setString(2, address);
	     ps.setString(3, city);
	     ps.setString(4, state);
	     ps.setString(5, country);
	     ps.setInt(6, zipCode);
	     ps.setString(7, manager);
	     ps.setString(8, phone);
	     ps.setString(9, createdBy);
	     ps.setBlob(10, logo);
	     
	     //execute
	     ps.executeQuery();
	     
	     //go to next screen
	     MoneyWire.displayMoneyWireCompanies(caller);
	     
	     //close 
	     conn.close();
	  }
	  catch(Exception e)
	  {
	     e.printStackTrace();	  
	  }
   }
   
   /*
    * Create money wire service
    */
   public static void createMoneyWireService(int wireNo, String company, String receiver, String sender, double amount, String ticketno,
		                                     String serviceType, String destination)
   {
	  String query = "CALL createSRMoney(?,?,?,?,?,?,?,?)";	  
	  try
	  {
	     Connection conn = Session.openDatabase();
	     PreparedStatement ps = conn.prepareStatement(query);
	     
	     //set parameters
	     ps.setInt(1, wireNo);
	     ps.setString(2, company);
	     ps.setString(3, receiver);
	     ps.setString(4, sender);
	     ps.setDouble(5, amount);
	     ps.setString(6, ticketno);
	     ps.setString(7, serviceType);
	     ps.setString(8, destination);
	     
	     //execute
	     ps.executeQuery();
	     
	     //close
	     conn.close();
	  }
	  catch(Exception e)
	  {
	     e.printStackTrace();	  
	  }
   }
   
   /*
    * Get company name
    */
   public static ObservableList<String> getCompanyNames()
   {
	  ObservableList<String> result = FXCollections.observableArrayList();
	  String query = "CALL listSRCompany(?)";
	  
	  try
	  {
	     Connection conn = Session.openDatabase();
	     PreparedStatement ps = conn.prepareStatement(query);
	     
	     //set parameters
	     ps.setString(1, Configs.getProperty("StoreCode"));
	     
	     //execute query
	     ResultSet rs = ps.executeQuery();
	     
	     //process the result set
	     while(rs.next())
	     {
	        result.add(rs.getString(1));	 
	     } 	 
	     
	     //connection
	     conn.close();
	  }
	  catch(Exception e)
	  {
	     e.printStackTrace();	  
	  }
	  
	  return result;
   }
   
   /*
    *  Get this company's logo
    */
   private static Image getMoneyWireCompanyLogo(String company)
   {
		  String query = "CALL getSRCompanyLogo(?)";
		  Image image = null;
		  byte[] imageArray = null;
		  
		  try
		  {
		     Connection conn = Session.openDatabase();
		     PreparedStatement ps = conn.prepareStatement(query);
		     
		     //set parameters
		     ps.setString(1, company);
		     
		     //execute query
		     ResultSet rs = ps.executeQuery();
		     
		     //process the result set
		     while(rs.next())
		     {
		        imageArray = rs.getBytes(1);    	 
		     } 	 
		     
		     //get the image
		     InputStream in = new ByteArrayInputStream(imageArray);    	  
		     BufferedImage bufferedImage = ImageIO.read(in);    	  
		     image = SwingFXUtils.toFXImage(bufferedImage, null);
		     
		     //connection
		     conn.close();
		  }
		  catch(Exception e)
		  {
		     e.printStackTrace();	  
		  }
		  		  
		  return image;	   
   }
   
   /*
    * Display form
    */
   private static void displayMoneyWireForm(int caller, String company)
   {
	  //stage
	  Stage stage = new Stage();
	  
	  //gridpane (form)
	  GridPane center = new GridPane();
	  
	  //text fields
	  NumericTextField wireno = new NumericTextField();
	  TextField sender = new TextField();
	  TextField receiver = new TextField();
	  NumericTextField amount = new NumericTextField();
	  TextField destination = new TextField();
	  
	  //labels
	  Label wirelbl = new Label("Wire No.");
	  Label senderlbl = new Label("Sender");
	  Label receiverlbl = new Label("Receiver");
	  Label amountlbl = new Label("Amount");
	  Label destinationlbl = new Label("Destination");
	  
	  //labels
	  wirelbl.setTextFill(Color.WHITE);
	  senderlbl.setTextFill(Color.WHITE);
	  receiverlbl.setTextFill(Color.WHITE);
	  amountlbl.setTextFill(Color.WHITE);
	  destinationlbl.setTextFill(Color.WHITE);
	  
	  wirelbl.setFont(new Font("Courier Sans", 12));
	  senderlbl.setFont(new Font("Courier Sans", 12));
	  receiverlbl.setFont(new Font("Courier Sans", 12));
	  amountlbl.setFont(new Font("Courier Sans", 12));
	  destinationlbl.setFont(new Font("Courier Sans", 12));
	  
	  //buttons
	  Button previous = new Button("Go Back", new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/Go back.png"))));
	  Button accept = new Button("Accept", new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/Apply.png"))));
	  
	  //set on action
	  previous.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   //close the stage
		   stage.close();
		   
		   //go back
		   MoneyWire.displayMoneyWireCompanies(caller);
		}
		  
	  });
	  
	  accept.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
			
			//check privileges
			if(Integer.parseInt(Configs.getProperty("Privilege")) >= 1)
			{
			   //check amount
			   if(wireno.getText() != null && !wireno.getText().isEmpty() &&
					   wireno.getText() != null && !wireno.getText().isEmpty()
					   && Double.parseDouble(amount.getText()) > 750 && Double.parseDouble(amount.getText()) > 10)	
			   {
				  if (sender.getText() != null && !sender.getText().isEmpty()
					  && receiver.getText() != null && !receiver.getText().isEmpty()
					  && wireno.getText() != null && !wireno.getText().isEmpty()
					  && amount.getText() != null && !amount.getText().isEmpty())
				  {
					  //type
					  String serviceType = "send";
					  
					  //close the stage
					  stage.close();  
					  
					  if(caller == 2)
					  {
					     serviceType = "receive";
					     amount.setText(Double.toString(Double.parseDouble(amount.getText())* 1));
					  }
					  
				      //store items in the temporary storage
					  Configs.saveTempValue("temp20", wireno.getText());
					  Configs.saveTempValue("temp21", sender.getText());
					  Configs.saveTempValue("temp22", receiver.getText());
					  Configs.saveTempValue("temp23", amount.getText());
					  Configs.saveTempValue("temp24", serviceType);
					  Configs.saveTempValue("temp25", company);
					  Configs.saveTempValue("temp26", destination.getText());
					  
					  //add money wire to the table
					  MainScreen.productList.add("MoneyWire");
					  MainScreen.products.add(new Product(company + " " + serviceType, "0", 1, Double.parseDouble(amount.getText())));
					  
					  //set table items
					  MainScreen.setTableItems(MainScreen.products);
					  
					  //add amount
					  MainScreen.Total.setText(Double.toString(Receipt.setPrecision(MainScreen.getTotal() + Double.parseDouble(amount.getText()))));
					  
					  //refresh table
					  MainScreen.table.refresh();
				  } 	
				  else
				  {
				     AlertBox.display("FASS Nova", "Fill in all required fields");	  
				  }	  
			   }
			   else if(amount.getText() != null && !amount.getText().isEmpty()
						  && Double.parseDouble(amount.getText()) < 750 && Double.parseDouble(amount.getText()) > 10
						  && wireno.getText() != null && !wireno.getText().isEmpty())
			   {
		  	      //type
				  String serviceType = "send";
					  
				  //close the stage
				  stage.close();  
					  
				  if(caller == 2)
				  {
				     serviceType = "receive";	  
				     amount.setText(Double.toString(Double.parseDouble(amount.getText())* -1));
				  }
					  
			      //store items in the temporary storage
				  Configs.saveTempValue("temp20", wireno.getText());
				  Configs.saveTempValue("temp21", sender.getText());
				  Configs.saveTempValue("temp22", receiver.getText());
				  Configs.saveTempValue("temp23", amount.getText());					  Configs.saveTempValue("temp24", serviceType);
				  Configs.saveTempValue("temp25", company);
				  Configs.saveTempValue("temp26", destination.getText());
					  
				  //add money wire to the table
				  MainScreen.productList.add("MoneyWire");
				  MainScreen.products.add(new Product(company + " " + serviceType, "0", 1, Double.parseDouble(amount.getText())));
					  
				  //set table items
				  MainScreen.setTableItems(MainScreen.products);
					  
				  //add amount
				  MainScreen.Total.setText(Double.toString(Receipt.setPrecision(MainScreen.getTotal() + Double.parseDouble(amount.getText()))));
					  
				  //refresh table
				  MainScreen.table.refresh();					  
			   }			  
			   else
			   {
				  AlertBox.display("FASS Nova", "Fill in wire number and amount");   
			   }	   
			}
			else
			{
			   AlertBox.display("FASS Nova", "You do not have permission to perform this action");	
			}	
		}
		  
	  });
	  
	  //add nodes to center
	  center.add(wirelbl, 0, 0);
	  center.add(wireno, 1, 0);
	  center.add(senderlbl, 0, 1);
	  center.add(sender, 1, 1);
	  center.add(receiverlbl, 0, 2);
	  center.add(receiver, 1, 2);
	  center.add(amountlbl, 0, 3);
	  center.add(amount, 1, 3);
	  center.add(destinationlbl, 0, 4);
	  center.add(destination, 1, 4);
	  
	  //setup center
	  center.setVgap(7);
	  center.setHgap(7);
	  center.setPadding(new Insets(10, 10, 10, 10));
	  
	  //bottom
	  HBox bottom = new HBox();
	  
	  //setup bottom
	  bottom.setAlignment(Pos.CENTER);
	  bottom.setPadding(new Insets(10, 10, 10, 10));
	  bottom.setSpacing(7);
	  
	  //add nodes to bottom
	  bottom.getChildren().addAll(previous, accept);
	  
	  //root
	  BorderPane root = new BorderPane();
	  
	  //setup root
	  root.setPadding(new Insets(20, 20, 20, 20));
	  
	  //add nodes to root
	  root.setCenter(center);
	  root.setBottom(bottom);
	  
	  //set id
	  root.setId("border");
	  
	  //load style sheets
	  root.getStylesheets().add(MoneyWire.class.getResource("MainScreen.css").toExternalForm());
	  
	  //scene
	  Scene scene = new Scene(root);
	  
	  //setup stage
	  stage.initModality(Modality.APPLICATION_MODAL);
	  if(caller == 1)
	  {	  
	     stage.setTitle("FASS Nova - Send Money");
	  }
	  else
	  {
	     stage.setTitle("FASS Nova - Receive Money");	  
	  }	  
	  
	  stage.setMinWidth(300);
	  stage.centerOnScreen();
	  
	  //set scene
	  stage.setScene(scene);
	  
	  //show
	  stage.show();
   }
   
   /*
    * Display the money wire companies
    */
   private static void displayMoneyWireCompanies(int caller)
   {
	  Stage stage = new Stage();   
	  
	  //add custom menu
	  Menu actions = new Menu("Actions");
	  MenuItem addCompany = new MenuItem("Add Money Wire Company");
	  MenuBar menu = new MenuBar();
	  
	  //setup menu
	  menu.prefWidthProperty().bind(stage.widthProperty());
	  
	  //add menu items to menu
	  actions.getItems().add(addCompany);
	  menu.getMenus().add(actions);
	  
	  //set menu items on action
	  addCompany.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
		   //close the stage
		   stage.close();	
		
		  //go to the next screen
		  addMoneyWireCompany(caller);
		}
		  
	  });
	  
	  //bottom layout
	  FlowPane bottom = new FlowPane();
	  
	  //setup bottom
	  bottom.setHgap(5);
	  bottom.setVgap(5);
	  bottom.setPadding(new Insets(10, 10, 10, 10));
	  
	  //root layout
	  VBox root = new VBox();

	  //setup root
	  root.setSpacing(7);
	  
	  //button array
	  ObservableList<Button> companies = FXCollections.observableArrayList();
	  
	  //get the company names
	  ObservableList<String> companyNames = MoneyWire.getCompanyNames();
	  
	  
	  //fill the array
	  for(int i = 0; i < companyNames.size(); i++)
	  {
		 //create imageview
		  Image image = getMoneyWireCompanyLogo(companyNames.get(i));
		  ImageView imageView = new ImageView(image);
		  
		  //set fit
		  imageView.setFitHeight(150);
		  imageView.setFitWidth(150);
		  
		 //create new button 
	     companies.add(new Button("", imageView));
	     
	     //create local variable
	     final String companyName = companyNames.get(i);
	     
	     //set on action
	     companies.get(i).setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   
				//close the stage
				stage.close();
				
				//go to next screen
				MoneyWire.displayMoneyWireForm(caller, companyName);
			}
	    	 
	     });
	     
	     //add node to bottom
	     bottom.getChildren().add(companies.get(i));
	  }	  
	  
	  //add nodes to root
	  root.getChildren().addAll(menu, bottom);
	  
	  //set id
	  root.setId("border");
	  
	  //style sheets
	  root.getStylesheets().add(MoneyWire.class.getResource("MainScreen.css").toExternalForm());
	  
	  //scene
	  Scene scene = new Scene(root);
	  
	  //setup stage
	  stage.setMinWidth(330);
	  stage.setMinHeight(200);
	  stage.initModality(Modality.APPLICATION_MODAL);
	  stage.centerOnScreen();
	  
	  if(caller == 1)
	  {
	     stage.setTitle("FASS Nova - Send Money");	  
	  }		
	  else
	  {
	     stage.setTitle("FASS Nova - Receive Money"); 	  
	  }	 
	  
	  //set scene
	  stage.setScene(scene);
	  
	  //show
	  stage.show();
   }
   
   /*
    * Create money wire company
    */
   private static void addMoneyWireCompany(int caller)
   {
	  //stage
	  Stage stage = new Stage();
	  
	  //labels
	  Label namelbl = new Label("Name");
	  Label addresslbl = new Label("Street Address");
	  Label citylbl = new Label("City");
	  Label statelbl = new Label("State");
	  Label countrylbl = new Label("Country");
	  Label ziplbl = new Label("Zip Code");
	  Label managerlbl = new Label("Manager");
	  Label phonelbl = new Label("Phone Number");
	  Label logolbl = new Label("Logo");
	  
	  //setup labels
	  namelbl.setTextFill(Color.WHITE);
	  addresslbl.setTextFill(Color.WHITE);
	  citylbl.setTextFill(Color.WHITE);
	  statelbl.setTextFill(Color.WHITE);
	  countrylbl.setTextFill(Color.WHITE);
	  ziplbl.setTextFill(Color.WHITE);
	  managerlbl.setTextFill(Color.WHITE);
	  phonelbl.setTextFill(Color.WHITE);
	  logolbl.setTextFill(Color.WHITE);

	  //set font
	  namelbl.setFont(new Font("Courier Sans", 14));
	  addresslbl.setFont(new Font("Courier Sans", 14));
	  citylbl.setFont(new Font("Courier Sans", 14));
	  statelbl.setFont(new Font("Courier Sans", 14));
	  countrylbl.setFont(new Font("Courier Sans", 14));
	  ziplbl.setFont(new Font("Courier Sans", 14));
	  managerlbl.setFont(new Font("Courier Sans", 14));
	  phonelbl.setFont(new Font("Courier Sans", 14));
	  logolbl.setFont(new Font("Courier Sans", 14));

	  //buttons
	  Button previous = new Button("Previous", new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/Go back.png"))));
	  Button accept = new Button("Accept", new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/Apply.png"))));
	  Button select = new Button("Select Photo", new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/camera.png"))));
	  
	  //text fields
	  TextField name = new TextField();
	  TextField address = new TextField();
	  TextField city = new TextField();
	  TextField state = new TextField();
	  TextField country = new TextField();
	  NumericTextField zip = new NumericTextField();
	  TextField manager = new TextField();
	  NumericTextField phone = new NumericTextField();
	  photo = new TextField();
 	  
	  //image view
	  logoPicture = new ImageView(new Image(MoneyWire.class.getResourceAsStream("/res/photo.png")));
	  logoPicture.setFitWidth(200);
	  logoPicture.setFitHeight(200);
	  
	  //initialize file
	  file = new File("");
	  
	  //set on action
	  previous.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		   
		   //close the stage
		   stage.close();
		   
		   //go back to previous screen
		   MoneyWire.displayMoneyWireCompanies(caller);
		}
		  
	  });
	  select.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			 file = MoneyWire.selectImageFile();	
		}
		  
	  });
	  
	  accept.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
	    
		   //check privilege level
		   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 1)
		   {
		      //check if all fields were filled
			  if(name.getText() != null && !name.getText().isEmpty() && address.getText() != null && !address.getText().isEmpty()
			     && city.getText() != null && !city.getText().isEmpty() && state.getText() != null && !state.getText().isEmpty()
			     && country.getText() != null && !country.getText().isEmpty() && zip.getText() != null && !zip.getText().isEmpty()
			     && manager.getText() != null && !manager.getText().isEmpty() && phone.getText() != null && !phone.getText().isEmpty()
			     && photo.getText() != null && !photo.getText().isEmpty()) 
			  {				  
				 
				 
			     //create money wire company
				 MoneyWire.createMoneyWireCompany(name.getText(), address.getText(), city.getText(), 
						                          state.getText(), country.getText(), Integer.parseInt(zip.getText()), 
						                          manager.getText(), phone.getText(), Configs.getProperty("CurrentUser"), caller); 
			  }	  
			  else
			  {
				 AlertBox.display("FASS Nova", "Please fill in all required fields"); 
			  }	  
		   }	
		   else
		   {
			  AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
		   }	   
		}
		  
	  });
	  
	  //gridpane
	  GridPane left = new GridPane();
	  
	  //setup top
	  left.setVgap(7);
	  left.setHgap(7);
	  left.setAlignment(Pos.CENTER);
	  left.setPadding(new Insets(10, 10, 10, 10));
	  
	  //add nodes to left
	  left.add(namelbl, 0, 0);
	  left.add(name, 1, 0);
	  left.add(addresslbl, 0, 1);
	  left.add(address, 1, 1);
	  left.add(citylbl, 0, 2);
	  left.add(city, 1, 2);
	  left.add(statelbl, 0, 3);
	  left.add(state, 1, 3);
	  left.add(countrylbl, 0, 4);
	  left.add(country, 1, 4);
	  left.add(ziplbl, 0, 5);
	  left.add(zip, 1, 5);
	  left.add(managerlbl, 0, 6);
	  left.add(manager, 1, 6);
	  left.add(phonelbl, 0, 7);
	  left.add(phone, 1, 7);
	  
	  //right layout
	  VBox right = new VBox();
	  
	  //setup right
	  right.setAlignment(Pos.CENTER);
	  right.setPadding(new Insets(10, 10, 10, 10));
	  right.setSpacing(7);
	  
	  //photo path layout
	  HBox photoPath = new HBox();
	  photoPath.setSpacing(7);
	  
	  //add button and text field
	  photoPath.getChildren().addAll(select, photo);
	  
	  //add nodes to right
	  right.getChildren().addAll(logoPicture, photoPath);
	  
	  //bottom layout
	  HBox bottom = new HBox();
	  bottom.setSpacing(7);
	  bottom.setAlignment(Pos.CENTER);
	  bottom.setPadding(new Insets(10, 10, 10, 10));
	  
	  //add nodes to bottom
	  bottom.getChildren().addAll(previous, accept);
	  
	  //root
	  BorderPane root = new BorderPane();
	  
	  //setup root
	  root.setPadding(new Insets(20, 20, 20, 20));
	  
	  //add nodes to root
	  root.setLeft(left);
	  root.setRight(right);
	  root.setBottom(bottom);
	  
	  //set id
	  root.setId("border");
	  
	  //load style sheet
	  root.getStylesheets().add(MoneyWire.class.getResource("MainScreen.css").toExternalForm());
	  
	  //setup stage
	  stage.setTitle("Create Money Wire Company");
	  stage.setMinWidth(300);
	  stage.initModality(Modality.APPLICATION_MODAL);
	  stage.centerOnScreen();
	  
	  //scene
	  Scene scene = new Scene(root);
	  
	  //set scene
	  stage.setScene(scene);
	  
	  //show
	  stage.show();
   }
   
   public static File selectImageFile()
   { 	
	  //create image file chooser
	  FileChooser fileChooser = Icon.createImageChooser();
		   
      //create file that will hold the image path
   	  File selectedFile = Icon.selectImage(fileChooser);
		   
	  //process the selected file path
 	  if(selectedFile != null)
      { 
		 photo.setText(selectedFile.getAbsolutePath());
		 String imageUrl = "";
		   try {
		      imageUrl = selectedFile.toURI().toURL().toExternalForm();
		      Image image = new Image(imageUrl);  
			  
		      //set image
			  logoPicture.setImage(image);
			  
			  //set the file
			  file = selectedFile;
		      
		   } catch (MalformedURLException e) {
			  AlertBox.display("FASS NOVA - Error", "Could not select image");
			  e.printStackTrace();
		   }
		   
		   //set image view dimensions
		   logoPicture.setFitWidth(200);
		   logoPicture.setFitHeight(200);
		   
		}	   
		else
		{ 
			AlertBox.display("FASS Nova - Selection Error", "No File Selected"); 			
        }
 	    return selectedFile;
	}   
   
	
}
