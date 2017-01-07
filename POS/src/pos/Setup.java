package pos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mysql.jdbc.PreparedStatement;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.CallableStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Setup {

	public static void initSetup()
	{ 
	   //setup the database	
	   welcomeScreen();   	
	   
	}
	
	/*
	 *  Display the database setup screen
	 */
	public static void welcomeScreen()
	{ 
	   Stage stage = new Stage();	
		
	   String url = "";
	   Text text = new Text("Welcome to FASS Nova");
	   
	   text.setFont(new Font("Courier", 28));
	   text.setFill(Color.WHITE);
	   
	   Text subtitle = new Text("A Point of Sale Software in Java");
	   subtitle.setFont(new Font("Courier", 20));
	   subtitle.setFill(Color.WHITE);
	   
	   Button next = new Button("Next", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go forward.png"))));
	   
	   //implement action
	   next.setOnAction(new EventHandler<ActionEvent>()
	   {

		   @Override
		   public void handle(ActionEvent event) {
			  // TODO Auto-generated method stub
		      stage.close();
		      
		      setupPrinter();
		   } 
		   
	   });
	   
	   //root layout
	   VBox root = new VBox();
	   //setup root
	   root.setAlignment(Pos.CENTER);
	   root.setPadding(new Insets(10, 10, 10, 10));
	   root.setSpacing(7);
	   
	   root.getChildren().addAll(text, subtitle, next);
	   
	   root.setId("border");
	   
	   //load stylesheet
	   root.getStylesheets().add(Setup.class.getResource("MainScreen.css").toExternalForm());
	   
	   //create scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.setMinWidth(330);
	   stage.setMinHeight(300);
	   stage.centerOnScreen();
	   stage.setTitle("FASS Nova - Setup - Welcome Screen");
	   
	   stage.setScene(scene);
	   stage.show();
	}

	
	public static void setupPrinter()
	{
		//create stage
		Stage stage = new Stage();
		
		//title
		Text title = new Text("Select Receipt Printer");
		
		//setup title
		title.setFont(new Font("Courier Sans", 23));
		title.setFill(Color.WHITE);
		
	   //setup nodes
		Label printerlbl = new Label("Printer Name");
		//TextField printer = new TextField();
		
		printerlbl.setTextFill(Color.WHITE);
		
		//create array list
		PrinterService p = new PrinterService();
		
		ObservableList<String> printerList = FXCollections.observableArrayList();
		printerList = p.getPrinterList();
		
		//Create ListView
		ListView<String> list = new ListView<String>();
		
		//setup columns
	    list.setPrefSize(200, 250);
	    list.setEditable(false);
	    list.setOrientation(Orientation.VERTICAL);
	    
	    //setup items
	    list.setItems(printerList);
	    
	    //root layout
	    VBox root = new VBox();
	    root.setSpacing(7);
	    
	    //VBox 
	    VBox top = new VBox();
	    top.setSpacing(5);
	    top.setAlignment(Pos.CENTER);
	    top.setPadding(new Insets(10, 10, 10, 10));
	    
	    //add children to top
	    top.getChildren().addAll(title, list);
	    
	    //buttons
	    Button next = new Button("Next", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go forward.png"))));
	    Button previous = new Button("Previous", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go back.png"))));	    
	    
	    //bottom layout
	    FlowPane center = new FlowPane();
	    center.setHgap(5);
	    center.setAlignment(Pos.CENTER);
	    
	    //add nodes to center
	    center.getChildren().addAll(printerlbl);
	    
	    //set id
	    root.setId("border");
	    
	    //implement actions
	    next.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   // TODO Auto-generated method stub
				
				//close stage
				stage.close();
				
			   //setup selected printer	
			   validatePrinter(list.getSelectionModel().getSelectedItem());
			   
			   if(list.getSelectionModel().getSelectedItem() != null && 
					   !list.getSelectionModel().getSelectedItem().isEmpty())
			   {	   
			      //go to next screen
			      setupStoreInfo();
			   } 
			}   
	    	
	    });
	    previous.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   // TODO Auto-generated method stub
				stage.close();
				
				welcomeScreen();
			} 
	    	
	    });
	    //load stylesheet
	    root.getStylesheets().add(Setup.class.getResource("MainScreen.css").toExternalForm());
	    
	    //bottom layout
	    FlowPane bottom = new FlowPane();
	    bottom.setHgap(5);
	    bottom.setAlignment(Pos.TOP_CENTER);
	    bottom.setPadding(new Insets(10, 10, 10, 10));
	    
	    //add nodes to bottom
	    bottom.getChildren().addAll(previous, next);
	    
	    //add nodes to root
	    root.getChildren().addAll(top, center, bottom);
	    
	    Scene scene = new Scene(root);
	    
	    //create and setup stage
	    stage.centerOnScreen();
	    stage.setTitle("FASS Nova - Select Printer");
	    stage.setMinWidth(330);
	    stage.setScene(scene);
	    
	    //show the stage
	    stage.show();
	}

	private static void validatePrinter(String printer) {
	   
		if(printer != null && !printer.isEmpty())
		{	
		   Configs.saveProperty("Printer", printer);
		}
		else
		{ 
			setupPrinter();
			
			AlertBox.display("FASS Nova", "Select a printer!");
		}	
	}
	
	/*
	 * Display the options to configure the store information
	 */
	public static void setupStoreInfo()
	{ 
		//stage
		Stage stage = new Stage();
		
		//text
		Text title = new Text("Setup The Store Information");
		title.setFill(Color.WHITE);
        title.setFont(new Font("Courier Sans", 28));
		
	   //root layout
	   GridPane root = new GridPane();
	   VBox layout = new VBox();
	   layout.setAlignment(Pos.CENTER);
	   layout.setId("border");
	   
	   //construct nodes
	   TextField name = new TextField();
	   NumericTextField number = new NumericTextField();
	   TextField code = new TextField();
	   NumericTextField phone = new NumericTextField();
	   TextField street = new TextField();
	   TextField state = new TextField();
	   TextField city = new TextField();
	   TextField country = new TextField();
	   NumericTextField zip = new NumericTextField();
	   TextField manager = new TextField();
	   
	   //set properties if any
	   name.setText(Configs.getProperty("StoreName"));
	   number.setText(Configs.getProperty("StoreNumber"));
	   code.setText(Configs.getProperty("StoreCode"));
	   phone.setText(Configs.getProperty("PhoneNumber"));
	   street.setText(Configs.getProperty("StreetAddress"));
	   state.setText(Configs.getProperty("State"));
	   country.setText(Configs.getProperty("Country"));
	   zip.setText(Configs.getProperty("ZipCode"));
	   manager.setText(Configs.getProperty("Manager"));
	   city.setText(Configs.getProperty("City"));
	   
	   Label namelbl = new Label("Store Name");
	   Label numberlbl = new Label("Store Number");
	   Label codelbl = new Label("Store Code");
	   Label phonelbl = new Label("Phone Number");
	   Label streetlbl = new Label("Street Address");
	   Label statelbl = new Label("State");
	   Label citylbl = new Label("City");
	   Label countrylbl = new Label("Country");
	   Label ziplbl = new Label("Zip Code");
	   Label managerlbl = new Label("Manager");
	   
	   //set spacing
	   root.setHgap(7);
	   root.setVgap(8);
	   
	   //set label color
	   namelbl.setTextFill(Color.WHITE);
	   codelbl.setTextFill(Color.WHITE);
	   numberlbl.setTextFill(Color.WHITE);
	   phonelbl.setTextFill(Color.WHITE);
	   streetlbl.setTextFill(Color.WHITE);
	   citylbl.setTextFill(Color.WHITE);
	   statelbl.setTextFill(Color.WHITE);
	   countrylbl.setTextFill(Color.WHITE);
	   ziplbl.setTextFill(Color.WHITE);
	   managerlbl.setTextFill(Color.WHITE);
	   
	   //buttons
	   Button next = new Button("Next", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go forward.png"))));
	   Button previous = new Button("Previous", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go back.png"))));	   
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setSpacing(6);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.getChildren().addAll(previous, next);
	   
	   //setup alignment
	   root.setAlignment(Pos.CENTER);
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //setup grid pane
	   root.add(namelbl, 0, 0);
	   root.add(name, 1, 0);
	   root.add(numberlbl, 0, 1);
	   root.add(number, 1, 1);
	   root.add(codelbl, 0, 2);
	   root.add(code, 1, 2);
	   root.add(phonelbl, 0, 3);
	   root.add(phone, 1, 3);
	   root.add(streetlbl, 0, 4);
	   root.add(street, 1, 4);
	   root.add(citylbl, 0, 5);
	   root.add(city, 1, 5);
	   root.add(statelbl, 0, 6);
	   root.add(state, 1, 6);
	   root.add(countrylbl, 0, 7);
	   root.add(country, 1, 7);
	   root.add(ziplbl, 0, 8);
	   root.add(zip, 1, 8);
	   root.add(managerlbl, 0, 9);
	   root.add(manager, 1, 9);
	   
	   next.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   // TODO Auto-generated method stub
				
				//close stage
				stage.close();
				
				//check if any text field is empty
				if(!name.getText().isEmpty() && !code.getText().isEmpty() && 
						!number.getText().isEmpty() && !street.getText().isEmpty()
						&& !state.getText().isEmpty() && !country.getText().isEmpty()
						&& !zip.getText().isEmpty() && !manager.getText().isEmpty())
				{	
				   if(!storeExists(code.getText(), Integer.parseInt(number.getText())))
				   {	
			          //save properties
					   Configs.saveProperty("StoreName", name.getText());
					   Configs.saveProperty("StoreCode", code.getText());
					   Configs.saveProperty("StoreNumber", number.getText());
					   Configs.saveProperty("PhoneNumber", phone.getText());
					   Configs.saveProperty("StreetAddress", street.getText());
					   Configs.saveProperty("State", state.getText());
					   Configs.saveProperty("City", city.getText());
					   Configs.saveProperty("Country", country.getText());
					   Configs.saveProperty("ZipCode", zip.getText());
					   Configs.saveProperty("Manager", manager.getText());
				
				      //go into the next screen
					   setupStoreInfo2();
				   }
			 	   else
				   { 
					  AlertBox.display("FASS Nova - Error", "Store already exists");
					  
					  //display screen again
		              setupStoreInfo();
				   }	
			   }
			   else
			   { 
				  AlertBox.display("FASS Nova - Error", "Please fill in all required fields");
				 
				  //display screen again
				  setupStoreInfo();
			   }	   
			}	  	
	    });
	    previous.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   // TODO Auto-generated method stub

		         //save properties
				 Configs.saveProperty("StoreName", name.getText());
			     Configs.saveProperty("StoreCode", code.getText());
			     Configs.saveProperty("StoreNumber", number.getText());
			     Configs.saveProperty("StreetAddress", street.getText());
				 Configs.saveProperty("PhoneNumber", phone.getText());
				 Configs.saveProperty("State", state.getText());			     
			     Configs.saveProperty("City", city.getText());
			     Configs.saveProperty("Country", country.getText());
			     Configs.saveProperty("ZipCode", zip.getText());
			     Configs.saveProperty("Manager", manager.getText());				
				
			     //close the stage
			     stage.close();
			     
				 //go back to previous screen  
				 setupPrinter();
			} 
	    	
	    });	   
	    
	   //add nodes to layout
	    layout.getChildren().addAll(title, root, bottom);
	    
	   //get stylesheet
	   layout.getStylesheets().add(Setup.class.getResource("MainScreen.css").toExternalForm());
	   
	   //create new scene
	   Scene scene = new Scene(layout);
	   
	   //setup stage
	   stage.setMinHeight(500);
	   stage.setMinWidth(500);
	   stage.setTitle("FASS Nova - Setup Store Info");
	   stage.centerOnScreen();
	   stage.setScene(scene);
	   stage.show();
	}
	
	/*
	 * 
	 * Check if the store already exists in the database
	 * 
	 */
    public static boolean storeExists(String code, int number)
    { 
    	String query = "SELECT Name from Store WHERE Store.storeCode = ? " + "AND " + 
                         "Store.storeNumber = ?";
    	try
    	{ 
    	   Connection conn = Session.openDatabase();
    	   java.sql.PreparedStatement ps = conn.prepareStatement(query);
    	   
    	   //set parameter
    	   ps.setString(1, code);
    	   ps.setInt(2, number);
    	   
    	   //execute query
    	   ResultSet rs = ps.executeQuery();
    	   
    	   int count = 0;
    	   while(rs.next())
    	   { 
    	   	  count++; 
    	   }	   
    	   
    	   if(count > 0)
    	   { 
    	      return true;	   
    	   }
    	   else
    	   { 
    		  return false; 
    	   }	   
    	   
    	}
    	catch(Exception e)
    	{ 
    	   e.printStackTrace(); 	
    	}
    	
    	return true;
    }
    
    /*
     *  Setup the rest of the store info
     */
    public static void setupStoreInfo2()
    { 
    	//the stage
    	Stage stage = new Stage();
    	
    	//top layout
    	FlowPane top = new FlowPane();
    	top.setAlignment(Pos.CENTER);
    	top.setPadding(new Insets(10, 10, 30, 10));    	
    	
    	//root layout
    	BorderPane root = new BorderPane();
    	
    	//set title
    	Text title = new Text("Setup The Store Information");
    	title.setFill(Color.WHITE);
    	title.setFont(new Font("Courier Sans", 28));
    	
    	//add title to top
    	top.getChildren().add(title);
    	
    	//setup nodes
    	TextField slogan = new TextField();
    	TextField logo = new TextField();
    	TextField greeting = new TextField();
    	TextField destination = new TextField();
    	CheckBox cb = new CheckBox("Enable Services");
    	Label sloganlbl = new Label("Slogan");
    	Label logolbl = new Label("Logo");
    	Label greetinglbl = new Label("Greeting");
    	Label destinationlbl = new Label("Destination Folder"); 
    	
    	//set text color
    	sloganlbl.setTextFill(Color.WHITE);
    	logolbl.setTextFill(Color.WHITE);
    	greetinglbl.setTextFill(Color.WHITE);
        destinationlbl.setTextFill(Color.WHITE);
    	
    	cb.setSelected(false);
    	cb.setTextFill(Color.WHITE);
    	
    	//buttons
  	   Button next = new Button("Next", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go forward.png"))));
 	   Button select = new Button("Select Logo", new ImageView(new Image(Setup.class.getResourceAsStream("/res/camera.png")))); 	   
 	   Button selectDestination = new Button("Select Directory", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Briefcase.png"))));
 	   
 	   //make button a little smaller
 	   selectDestination.setMaxHeight(4);
 	   
 	   //image views
 	   ImageView logoPicture = new ImageView(new Image(Setup.class.getResourceAsStream("/res/userPicture.png")));
 	   logoPicture.setFitHeight(100);
 	   logoPicture.setFitWidth(100);
 	   
	   //implement actions
 	   next.setOnAction(new EventHandler<ActionEvent>() {
		   @Override
		   public void handle(ActionEvent event) {
              
			  //save settings
			  Configs.saveProperty("DestinationFolder", destination.getText());
			  Configs.saveProperty("Greeting", greeting.getText());
			  Configs.saveProperty("Slogan", slogan.getText());
			   
			  //check box selection
			  int selection = 0;
			  if(cb.isSelected())
			  { 
				  selection = 1;
				  Configs.saveProperty("ServicesEnabled", "true");
			  }	  
			  else
			  { 
				  Configs.saveProperty("ServicesEnables", "fale");				  
			  }	  
			  
			  //create the new store in the database
			  File file = new File(logo.getText());
			  String query = "CALL createStore(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			  
			  try
			  { 
			     Connection conn = Session.openDatabase();
			     java.sql.PreparedStatement ps = conn.prepareStatement(query);
			     
			     //set parameters
			     ps.setString(1, Configs.getProperty("StoreName"));
			     ps.setInt(2, Integer.parseInt(Configs.getProperty("StoreNumber")));
			     ps.setString(3, Configs.getProperty("PhoneNumber"));
			     ps.setString(4, Configs.getProperty("StoreCode"));
			     ps.setString(5, Configs.getProperty("StreetAddress"));
			     ps.setString(6, Configs.getProperty("City"));
			     ps.setString(7, Configs.getProperty("State"));
			     ps.setString(8, Configs.getProperty("Country"));
			     ps.setInt(9, Integer.parseInt(Configs.getProperty("ZipCode")));
			     ps.setString(10, Configs.getProperty("Manager"));
			     ps.setString(11, Configs.getProperty("Slogan"));
			     ps.setString(12, Configs.getProperty("Greeting"));
			     ps.setInt(13, selection);
			     ps.setBlob(14, new FileInputStream(file));

			     //execute query
			     ps.executeQuery();
			     
			     //display success
			     AlertBox.display("FASS Nova", "Store has been created successfully");
			     
			  }
			  catch(Exception e)
			  { 
			     e.printStackTrace();
			  }
			 
			  stage.close();
			  
			  //go to next screen
			  UserDisplay.addUserDisplay();
			  
			  //after done, setup the register
			  setupRegister();
		   } 
		    		   
 	   });
 	   
 	   select.setOnAction( new EventHandler<ActionEvent>()   {     

		@Override
		public void handle(ActionEvent event) {
		 	  //create image file chooser
		 	  FileChooser fileChooser = Icon.createImageChooser();
		 		   
		      //create file that will hold the image path
		      File selectedFile = Icon.selectImage(fileChooser);
		 		   
		 	  //process the selected file path
		  	  if(selectedFile != null)
		       { 
		 		 logo.setText(selectedFile.getAbsolutePath());
		 		 String imageUrl = "";
		 		   try {
		 		      imageUrl = selectedFile.toURI().toURL().toExternalForm();
		 		      Image image = new Image(imageUrl);  
		 			  
		 		      //set image
		 			  logoPicture.setImage(image);
		 			  
		 			  //set the file
		 			  File file = selectedFile;
		 		      
		 		   } catch (MalformedURLException e) {
		 		      // TODO Auto-generated catch block
		 			  AlertBox.display("FASS NOVA - Error", "Could not select image");
		 			  e.printStackTrace();
		 		   }
		 		   
		 		   //set image view dimensions
		 		   logoPicture.setFitWidth(100);
		 		   logoPicture.setFitHeight(100);	 		   
		 		}	   
		 		else
		 		{ 
		 			AlertBox.display("FASS Nova - Selection Error", "No File Selected"); 			
		        }				
		  } 
 		   
 	   }); 	   
 	   
 	   selectDestination.setOnAction(new EventHandler<ActionEvent>() {

		   @Override
		   public void handle(ActionEvent event) {
			 	 //create  directory chooser
			 	 DirectoryChooser directoryChooser = Icon.buildDirectory();
			 		   
			      //create file that will hold the image path
			      File selectedFile = Icon.selectDirectory(directoryChooser);
			 		   
			 	  //process the selected file path
			  	  if(selectedFile != null)
			      { 
			 		 destination.setText(selectedFile.getAbsolutePath());  			 		   
			 	  }	   
			 	  else
			 	  { 
			 	  	 AlertBox.display("FASS Nova - Selection Error", "No Directory Selected"); 			
			      }	           			
		  } 
 		   
 	   });
 	   
 	   //center layout
 	   GridPane center = new GridPane();
 	   center.setAlignment(Pos.CENTER);
 	   center.setPadding(new Insets(20, 20, 20, 20));
 	   center.setHgap(8);
 	   center.setVgap(8);
 	   
 	   //set center
 	   center.add(sloganlbl, 0, 0);
 	   center.add(slogan, 1, 0);
 	   center.add(greetinglbl, 0, 1);
 	   center.add(greeting, 1, 1);
 	   center.add(destinationlbl, 0, 2);
 	   center.add(destination, 1, 2);
 	   center.add(selectDestination, 2, 2);
 	   center.add(cb, 0, 3);
 	   
 	   //set right layout
 	   VBox right = new VBox();
 	   right.setSpacing(5);		
 	   right.setAlignment(Pos.CENTER);
 	   
 	   //file path layout
 	   HBox layout = new HBox();
 	   layout.setSpacing(5);
 	   layout.setAlignment(Pos.CENTER); 	   
 	   layout.getChildren().addAll(logo, select);
 	   right.getChildren().addAll(logoPicture, select);
 	   
 	   //center layout
 	   VBox centerLayout = new VBox();
 	   centerLayout.getChildren().addAll(right, center);
 	   
 	   //bottom Layout
 	   FlowPane bottom = new FlowPane();
 	   bottom.setAlignment(Pos.CENTER);
 	   bottom.setPadding(new Insets(20, 20, 20, 20));
 	   bottom.getChildren().add(next);
 	   
 	   //set root
 	   root.setTop(top);
 	   root.setCenter(centerLayout);
 	   root.setBottom(bottom);
 	   
    	//set id
    	root.setId("border");
    	
    	root.getStylesheets().add(Setup.class.getResource("MainScreen.css").toExternalForm());
    	
    	//create scene
    	Scene scene = new Scene(root);
    	
    	//setup stage
    	stage.setMinHeight(500);
    	stage.setMinWidth(400);
    	
    	
    	stage.setTitle("FASS Nova - Setup Store Info");
    	stage.centerOnScreen();
    	stage.setScene(scene);
    	
    	stage.show();
    }
    
    /*
     *  Create a new register   
     */
    public static void setupRegister()
    { 
        //Stage
    	Stage stage = new Stage();
    	
    	//setup nodes
    	Label idlbl = new Label("Register Number");
    	Label taxRatelbl = new Label("Tax Rate");
    	Label storeCodelbl = new Label("Store Code");
    	TextField storeCode = new TextField();
    	NumericTextField id = new NumericTextField();
    	NumericTextField taxRate = new NumericTextField();
    	
    	//set editable false
    	storeCode.setEditable(false);
    	storeCode.setText(Configs.getProperty("StoreCode"));
    	
    	//setup labels
    	idlbl.setTextFill(Color.WHITE);
    	taxRatelbl.setTextFill(Color.WHITE);
    	storeCodelbl.setTextFill(Color.WHITE);
    	
    	//title
    	Text title = new Text("Setup the register");
    	title.setFill(Color.WHITE);
    	title.setFont(new Font("Courier Sans", 28));
    	
    	//top layout
    	FlowPane top = new FlowPane();
    	top.setAlignment(Pos.CENTER);
    	top.setPadding(new Insets(10, 10, 20, 10));
        top.getChildren().add(title);
    	
    	//form layout
    	GridPane grid = new GridPane();
    	grid.setAlignment(Pos.CENTER);
    	grid.setPadding(new Insets(20, 20, 20, 20));
    	grid.setHgap(8);
    	grid.setVgap(8);
    	
    	//setup form
    	grid.add(idlbl, 0, 0);
    	grid.add(id, 1, 0);
    	grid.add(taxRatelbl, 0, 1);
    	grid.add(taxRate, 1, 1);
    	grid.add(storeCodelbl, 0, 2);
    	grid.add(storeCode, 1, 2);
    	
    	//button
    	Button next = new Button("Next", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go forward.png"))));
    	
    	//implement action
    	next.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   
				//save properties
				Configs.saveProperty("TaxRate", taxRate.getText());
				Configs.saveProperty("Register", id.getText());
				
				//create new register in database
				String query  = "CALL createRegister(?,?,?,?)";
				
				try
				{ 
				   //create connection and prepared statement
					Connection conn = Session.openDatabase();
					PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
					
					//set parameters
					ps.setInt(1, Integer.parseInt(id.getText()));
					ps.setString(2, storeCode.getText());
					ps.setString(3, null);
					ps.setString(4, null);
					
					//execute query
					ps.execute();
					
					//display success
					AlertBox.display("FASS Nova", "Register setup successfully!");
					
					//close current stage
					stage.close();
					
					//display last setup screen
					successScreen();
				}
				catch(Exception e)
				{ 
				   AlertBox.display("FASS Nova", "An error has occurred!");	
				   e.printStackTrace();	
				}
			} 
    		
    	});
    	
    	//bottom layout
    	HBox bottom = new HBox();
    	bottom.setAlignment(Pos.CENTER);
    	bottom.setPadding(new Insets(10, 10, 10, 10));
    	
    	//add button
    	bottom.getChildren().add(next);
    	
    	//root layout
    	VBox root = new VBox();
    	
    	//set id
    	root.setId("border");
    	
    	//add nodes to root
    	root.getChildren().addAll(top, grid, bottom);
    	
    	//get stylesheets
    	root.getStylesheets().add(Setup.class.getResource("MainScreen.css").toExternalForm());
    	
    	//setup the scene
    	Scene scene = new Scene(root);
    	
    	//setup stage
    	stage.setTitle("FASS Nova - Setup Register");
    	stage.setMinWidth(300);
    	stage.centerOnScreen();
    	stage.setScene(scene);
    	stage.show();
    	
    }
    
    /*
     * Display success screen
     * FASS Nova has been configured correctly
     */
    public static void successScreen()
    { 
       Stage stage = new Stage();
       
       //byte array
       byte[] imageArray = null;
       Image image = null;
       
       //title
       Text title = new Text("FASS Nova has been setup successfully");
       title.setFont(new Font("Courier Sans", 28));
       title.setFill(Color.WHITE);
       
       //subtitle
       Text subtitle = new Text("Thank you for purchasing and installing FASS Nova");
       subtitle.setFont(new Font("Courier Sans", 22));
       subtitle.setFill(Color.WHITE);
       
       //retrieve logo from the database
       String query = "SELECT Logo FROM Store WHERE Store.storeCode = ?";
       
       try
       { 
    	  Connection conn = Session.openDatabase();
    	  PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
    	  
    	  //set parameters
    	  ps.setString(1, Configs.getProperty("StoreCode"));
    	  
    	  //execute query
    	  ResultSet rs = ps.executeQuery();
    	  
    	  //process
    	  while(rs.next())
    	  { 
    	     imageArray= rs.getBytes(1);	  
    	  }	  
    	  
    	  InputStream in = new ByteArrayInputStream(imageArray);    	  
    	  BufferedImage bufferedImage = ImageIO.read(in);    	  
    	  image = SwingFXUtils.toFXImage(bufferedImage, null);
    	  
    	  conn.close();
       }
       catch(Exception e)
       { 
          e.printStackTrace();	   
       }
       
       //set image view
       ImageView imageview = new ImageView(image);
       imageview.setFitHeight(200);
       imageview.setFitWidth(200);
       
       //button
       Button next = new Button("Next", new ImageView(new Image(Setup.class.getResourceAsStream("/res/Go forward.png"))));
       
       //implement action
       next.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
           //close the stage
			stage.close();
			
			//go into the login screen
			Login.displayLogin();
		} });
       
       VBox root = new VBox();
       root.setAlignment(Pos.CENTER);
       root.setPadding(new Insets(20, 20, 20, 20));
       root.setSpacing(7);
       
       //set id
       root.setId("border");
       
       //get stylesheets
       root.getStylesheets().add(Setup.class.getResource("MainScreen.css").toExternalForm());
       
       //add nodes to children
       root.getChildren().addAll(title, subtitle, imageview, next);
       
       //setup scene
       Scene scene = new Scene(root);
       
       //setup stage
       stage.setMinWidth(310);
       stage.setTitle("FASS Nova - Setup Complete");
       stage.centerOnScreen();
       stage.setScene(scene);
       
       stage.show();
    }
    
}
