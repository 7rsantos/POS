package pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

public class UserDisplay {

private static Stage window;	
private static TextField firstName;
private static TextField lastName;
private static PasswordField pass;
private static PasswordField confirmPass;
private static TextField user;
private static TextField email;
private static TextField phone;
private static ImageView userPicture;
private static File file;
private static TextField photoPath;  
private static ChoiceBox<Integer> privilegeLevel;
private static ChoiceBox<Integer> dayChoice;
private static ChoiceBox<Integer> monthChoice;
private static ChoiceBox<Integer> yearChoice;
private static ChoiceBox<String> storeLocations;

   /**
    * Display the window to create a new user
    * 	
    */
   public static void addUserDisplay()
   { 
	   //create the stage
	  	window = new Stage();
	  	
	  	//setup the stage
		window.setTitle("FASS Nova - Create New User");
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(250);
	  	
		//create the root layout
		BorderPane root = new BorderPane();
		
		//create images
		Image cancelIcon = new Image(Inventory.class.getResourceAsStream("/res/Cancel.png"));
		Image addUser = new Image(Inventory.class.getResourceAsStream("/res/Create.png"));
		Image cameraIcon = new Image(Inventory.class.getResourceAsStream("/res/camera.png"));
		
	  	//create buttons
	  	Button add = new Button("Add", new ImageView(addUser));
	  	Button cancel = new Button("Cancel", new ImageView(cancelIcon));
	  	Button select = new Button("Select");
	  	//Button picture = new Button("Take Photo", new ImageView(cameraIcon));
	  	
        //implement buttons functionalities
		select.setOnAction(e -> selectImageFile(photoPath, userPicture));
		//picture.setOnAction(e -> PhotoScreen.displayPhotoScreen(1));
		cancel.setOnAction(e -> window.close());
		add.setOnAction(e -> createUser(firstName.getText(), lastName.getText(), user.getText(),
				                   pass.getText(), confirmPass.getText(), privilegeLevel.getValue(),
				                   dayChoice.getValue(), monthChoice.getValue(), 
				                   yearChoice.getValue(), email.getText(), phone.getText(),
				                   photoPath.getText(), storeLocations.getValue()));
	  	
	  	//create bottom section that will hold the buttons
	  	HBox south = new HBox();
	  	south.setPadding(new Insets(5, 10, 5, 10));
	  	south.setSpacing(17);
	  	south.setAlignment(Pos.CENTER);
	  	
	  	//add nodes to bottom
	  	south.getChildren().addAll(add, cancel);
	  	
	  	//HBox that will contain birth date options
	  	HBox birthDate = createBirthDateField(); 
	  	
	  	//create bottom layout
	  	VBox bottom = new VBox();
	  	
	  	//setup bottom
	  	bottom.getChildren().addAll(birthDate, south);
	  	
	  	//setup the title of the screen
		Text title = new Text("Create New User");
		   
		//setup title properties
		title.setFill(Color.WHITE);
		title.setFont(Font.font("Courier New", FontWeight.BOLD, 28));	  	
	  	
		//create box to hold title
		HBox top = new HBox();
		   
		//set top's alignment
		top.setAlignment(Pos.TOP_CENTER);
		top.setPadding(new Insets(5, 10, 1, 10));
		   
		//add title to HBox
		top.getChildren().add(title);
		
		//create layout to hold nodes to the left of the creen
		VBox left = new VBox();
		
		//setup left layout
		left.setSpacing(5);
	  	left.setPadding(new Insets(5, 5, 1, 25));	
			
	  	//create labels
	  	Label first = new Label("First Name");
	  	Label last = new Label("Last Name");
	  	Label username = new Label("Username");
	  	Label password = new Label("Password");
	  	Label confirmPassword = new Label("Confirm Password");
	  	Label emailAddress = new Label("Email");
	  	Label phoneNumber = new Label("Phone Number");
	  	Label privilege = new Label("Privilege Level");
	  	Label code = new Label("Store Code");
	  	
	  	//change font color
	  	first.setTextFill(Color.WHITE);
	  	last.setTextFill(Color.WHITE);
	  	username.setTextFill(Color.WHITE);
	  	password.setTextFill(Color.WHITE);
	  	confirmPassword.setTextFill(Color.WHITE);
	  	emailAddress.setTextFill(Color.WHITE);
	  	phoneNumber.setTextFill(Color.WHITE);
	  	privilege.setTextFill(Color.WHITE);
	  	code.setTextFill(Color.WHITE);
	  	
	  	
	  	//initialize text fields
	  	firstName = new TextField();
	  	lastName = new TextField();
	  	user = new TextField();
	  	pass = new PasswordField();
	  	confirmPass = new PasswordField();
	  	email = new TextField();
	  	phone = new TextField();	
	  	
	  	//choice box to display different privilege levels
	  	privilegeLevel = getPrivilegeBox();
	  	
	  	//create choice box to display store locations
	  	storeLocations = getStoreCodes();
	  	
	  	//button 
	  	Button check = new Button("Check");
	  	Label checklbl = new Label("");
	  	
	  	//set on action
	  	check.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(user.getText() != null && !user.getText().isEmpty())
			   {
			      if(!usernameExists(user.getText()))
	   
			      {
				      //bind property 
				      checklbl.textProperty().unbind();
				      SimpleStringProperty property = new SimpleStringProperty("Available");
				      checklbl.setTextFill(Color.LIGHTGREEN);
				      checklbl.textProperty().bind(property);			    	  
			      }	  
			      else
			      {
			    	 //bind property 
			         checklbl.textProperty().unbind();
			         SimpleStringProperty property = new SimpleStringProperty("Unavailable");
			         checklbl.setTextFill(Color.RED);
			         checklbl.textProperty().bind(property);
			      }	  
			   }	
			   else
			   {
				   AlertBox.display("FASS Nova", "Fill in required fields");   
			   }	   
			}
	  		
	  	});
	  	
	  	//create a grid layout to hold nodes for the form
	  	GridPane layout = new GridPane();
	  	layout.setPadding(new Insets(15, 10, 5, 25));

	  	//set the spacing
	  	layout.setHgap(5);
	  	layout.setVgap(13);
	  	
	  	//setup layout with all nodes
	  	layout.add(first, 0, 0);
	  	layout.add(firstName, 1, 0);
	  	layout.add(last, 0, 1);
	  	layout.add(lastName, 1, 1);
	  	layout.add(username, 0, 2);
	  	layout.add(user, 1, 2);
	  	layout.add(check, 2, 2);
	  	layout.add(password, 0, 3);
	  	layout.add(pass, 1, 3);
	  	layout.add(checklbl, 2, 3);
	  	layout.add(confirmPassword, 0, 4);
	  	layout.add(confirmPass, 1, 4);
	  	layout.add(emailAddress, 0, 5);
	  	layout.add(email, 1, 5);
	  	layout.add(phoneNumber, 0, 6);
	  	layout.add(phone, 1, 6);
	  	layout.add(privilege, 0, 7);
	  	layout.add(privilegeLevel, 1, 7);
	  	layout.add(code, 0, 8);
	  	layout.add(storeLocations, 1, 8);
	  	
	  	//add nodes to left layout
	  	left.getChildren().addAll(layout);
	  	 	
	  	//initialize photo path
	  	photoPath = new TextField();
	  	
		//create the photo icon
		Image image = new Image(Inventory.class.getResourceAsStream("/res/userPicture.png"));
	  	
	  	//initialize image view
	  	userPicture = new ImageView(image);		  	
	  	
		//create VBox Layout that will hold image options
		VBox right = new VBox();
		right.setAlignment(Pos.CENTER);
		right.setPadding(new Insets(1, 10, 30, 10));
		   
		//create a HBox to hold file chooser and text field
		HBox fileSelection = new HBox();
		fileSelection.setSpacing(2);
		   
		//add nodes to the file selection
		fileSelection.getChildren().addAll(select, photoPath);

		   
		//add nodes to photoLayout
		right.setSpacing(7);
		//right.getChildren().addAll(userPicture, picture, fileSelection);  	
		right.getChildren().addAll(userPicture, fileSelection);  	
	  	
	  	//add nodes to the root
	  	root.setTop(top);
	  	root.setLeft(layout);
	  	root.setRight(right);
	  	root.setBottom(bottom);
	  	
	  	//create the scene
	  	Scene scene = new Scene(root);
	  	
	  	//set id
	  	root.setId("root");
	  	
	  	//set icon
		window.getIcons().add(new Image(Login.class.getResourceAsStream("/res/FASSlogo.jpg")));

	  	
		//load resources
		scene.getStylesheets().add(Inventory.class.getResource("AddInventory.css").toExternalForm());
	  	
	  	// add scene to the stage and show
	  	window.setScene(scene);
	  	window.showAndWait();
   }

   private static void createUser(String first, String last, String user, String pass,
		                          String confirmPass, int privilege, int day, int month,
		                          int year, String email, String phone, String photoPath,
		                          String storeCode) 
   {
	  
	  // construct birth date of user
	  String birthDate = year + "-" + month + "-" + day;
	  
	  //query to create user
	  String query = "CALL CreateEmployee(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  
	  if(formCompleted())
	  {	   
	     //check if username exists
	     if(usernameExists(user) == false)
	     {
            if(pass.equals(confirmPass))
            { 
        	   try
        	   { 
        		  Connection conn = Session.openDatabase();
        		  
        		  //create prepare statement
        		  PreparedStatement ps = conn.prepareStatement(query);
        		  
        		  //create file input stream to hold picture
        		  FileInputStream input = new FileInputStream(file); 
        		  
        		  //set parameters
        		  ps.setString(1, user);
        		  ps.setString(2, Session.encrypt(pass));
        		  ps.setString(3, email);
        		  ps.setString(4, phone);
        		  ps.setString(5, first);
        		  ps.setString(6, last);
        		  ps.setBlob(7, input);
        		  ps.setString(8, birthDate);
        		  ps.setInt(9, privilege);
        		  ps.setString(10, storeCode);
        		  
        		  //execute the query
        		  ps.execute();
        		  
        		  System.out.println("INFO: " + ps.getUpdateCount());
        		  
        		  //report success
        		  AlertBox.display("FASS Nova", "User created successfully!");
        		  
        		  //close the window
        		  window.close();
        		  
        	   }
        	   catch (Exception e)
        	   { 
        		  e.printStackTrace();   
        	   }
            } 	 
            else
            { 
        	    AlertBox.display("FASS Nova - Error", "Passwords do not match!");
            } 	 
	     }	
	     else
	     { 
		     AlertBox.display("FASS Nova - Error", "Username already exists");
	     }
	  }
	  else
	  { 
		  AlertBox.display("FASS Nova - Error", "Please fill in all required fields!");
	  }	  
   }
   
   /*
    *  Check if username exists in the database
    */
   private static boolean usernameExists(String user)
   {
	  String query = "SELECT Count(Username) FROM Employee WHERE Employee.Username = ?";
	  boolean exists = false;
	  
	  try
	  {
	     Connection conn = Session.openDatabase();
	     PreparedStatement pst = conn.prepareStatement(query);
	     
	     //set parameter
	     pst.setString(1, user);
	     
	     //execute
	     ResultSet rs = pst.executeQuery();
	     
	     while(rs.next())
	     {
	        if(rs.getInt(1) == 1)
	        {
	           exists = true;	
	        } 	
	     } 	 
	  }
	  catch(Exception e)
	  {
	     e.printStackTrace();	  
	  }
			 
      return exists;
   }
   
   /*
    * Select the image file
    */
   public static void selectImageFile(TextField path, ImageView userPhoto)
   { 
		
	  //create image file chooser
	  FileChooser fileChooser = Icon.createImageChooser();
		   
      //create file that will hold the image path
   	  File selectedFile = Icon.selectImage(fileChooser);
		   
	  //process the selected file path
 	  if(!selectedFile.equals(null))
      { 
		 photoPath.setText(selectedFile.getAbsolutePath());
		 String imageUrl = "";
		   try {
		      imageUrl = selectedFile.toURI().toURL().toExternalForm();
		      Image image = new Image(imageUrl);  
			  
		      //set image
			  userPhoto.setImage(image);
			  
			  //set the file
			  file = selectedFile;
		      
		   } catch (MalformedURLException e) {
			  AlertBox.display("FASS NOVA - Error", "Could not select image");
			  e.printStackTrace();
		   }
		   
		   //set image view dimensions
		   userPhoto.setFitWidth(200);
		   userPhoto.setFitHeight(250);
		   
		}	   
		else
		{ 
			AlertBox.display("FASS Nova - Selection Error", "No File Selected"); 			
        }	  		
	}   
	
	public static void setPicturePath(File path)
	{ 
	   	
	   file = path;	
	   photoPath.setText(path.getAbsolutePath());
	   String url = ""; 
	   try
	   {
		  url = path.toURI().toURL().toExternalForm(); 
		  Image photo = new Image(url); 
	      userPicture.setImage(photo);
	   }   
	   catch(MalformedURLException e)
	   { 
		   AlertBox.display("FASS Nova - Error", "Could not upload image");
		   e.printStackTrace();
	   }
	   userPicture.setFitHeight(250);
	   userPicture.setFitWidth(200);
	}
	
	/**
	   Creates combo boxes for birth date fields 
	   Returns a VBox with the 6 nodes on it
	*/
	private static HBox createBirthDateField()
	{ 
		HBox birthField = new HBox();
		
		//create choice boxes
		dayChoice = new ChoiceBox<Integer>();
		monthChoice = new ChoiceBox<Integer>();
		yearChoice = new ChoiceBox<Integer>();
		
		//set height
		//yearChoice.prefHeightProperty().bind(window.heightProperty());
		monthChoice.setMaxHeight(10);
		dayChoice.setMaxHeight(10);
		
		//create labels
		Label dayLbl = new Label("Day");
		Label monthLbl = new Label("Month");
		Label  yearLbl = new Label("Year");
		
		//change label font colors
		dayLbl.setTextFill(Color.WHITE);
		monthLbl.setTextFill(Color.WHITE);
		yearLbl.setTextFill(Color.WHITE);
		
		//populate day and month choice boxes
		for (int i = 1; i < 31; i++)
		{ 
			dayChoice.getItems().add(i);
			
			if( i < 13)
			{ 
			   monthChoice.getItems().add(i);	
			}	
		}	
		
		// populate month choice box
		for (int i = 1950; i < 2000; i++ )
		{ 
		   yearChoice.getItems().add(i);	
		}	
		
		//set default values
		dayChoice.setValue(1);
		monthChoice.setValue(1);
		yearChoice.setValue(1976);
		
		//populate the hbox
		birthField.getChildren().addAll(dayLbl, dayChoice, monthLbl, monthChoice, 
				yearLbl, yearChoice);
		
		//set the spacing
		birthField.setSpacing(5);
		
		//set padding
		birthField.setPadding(new Insets(1, 10, 3, 25));
		
		birthField.getStylesheets().add(UserDisplay.class.getResource("AddInventory.css").toExternalForm());
		
		//set alignment
		birthField.setAlignment(Pos.TOP_LEFT);
		
		return birthField;
	}
	
	
	/**
	 * Construct a choice box that will display
	 * the different levels of user privileges
	 * 
	 * @return ChoiceBox with allowed privilege levels 
	 */
	private static ChoiceBox<Integer> getPrivilegeBox()
	{ 
		//choice box that will contain privilege levels
		ChoiceBox<Integer> privilegeLevel = new ChoiceBox<Integer>();
		
		//populate the choice box
		privilegeLevel.getItems().add(0);
		privilegeLevel.getItems().add(1);
		privilegeLevel.getItems().add(2);
		privilegeLevel.getItems().add(3);
		
		//set the default values
		privilegeLevel.setValue(0);
		
		return privilegeLevel;
	}
	
	/**
	 * Build a Choice Box that will contain the different locations
	 * of a store
	 * @return Choice Box with store codes
	 */
	public static ChoiceBox<String> getStoreCodes()
	{ 
		ChoiceBox<String> stores = new ChoiceBox<String>();
		String query = "CALL listStoreCodes(?)";		
		try
		{ 
		   Connection conn = Session.openDatabase();
		   
		   //prepare statement
		   PreparedStatement ps = conn.prepareStatement(query);
		   
		   //set parameters
		   ps.setString(1, Configs.getProperty("StoreName"));
		   
		   //execute query
		   ResultSet rs = ps.executeQuery();
		   
		   while(rs.next())
		   { 
			  stores.getItems().add(rs.getString("storeCode"));  
		   }
		   
		   //close the connection
		   conn.close();
		}
		catch(Exception e)
		{ 
		   e.printStackTrace();
		}
		
		return stores;
	}
	
	private static boolean formCompleted()
	{ 
		if (!firstName.getText().isEmpty() || !lastName.getText().isEmpty() ||
		    !pass.getText().isEmpty() || !confirmPass.getText().isEmpty() || 
		    !email.getText().isEmpty() || !phone.getText().isEmpty() || 
		    !user.getText().isEmpty() || !dayChoice.getValue().equals("") ||
		    !monthChoice.getValue().equals("null") || !yearChoice.getValue().equals("null")
		    || !privilegeLevel.getValue().equals("null") || 
		    !storeLocations.getValue().isEmpty())
		    
		{ 
			return true;
		}
		else
		{ 
			return false;
		}	
	}
	
	/*
	 * Update user information
	 */
	public static void displayUserUpdate()
	{
	   //stage
	   Stage stage = new Stage();
	   	   
	   //create labels
	   Label firstlbl = new Label("First Name");
	   Label lastlbl = new Label("Last Name");
	   Label emailAddresslbl = new Label("Email");
	   Label phoneNumberlbl = new Label("Phone Number");

	  	
	  	//change font color
	   firstlbl.setTextFill(Color.WHITE);
	   lastlbl.setTextFill(Color.WHITE);
	   emailAddresslbl.setTextFill(Color.WHITE);
	   phoneNumberlbl.setTextFill(Color.WHITE);
	   
	   //initialize text fields
	   firstName = new TextField();
	   lastName = new TextField();
	   email = new TextField();
	   phone = new TextField();
	   photoPath = new TextField();
	   
	   //set values
	   ObservableList<String> result = UserDisplay.getEmployeeInfo();
	   firstName.setText(result.get(0));
	   lastName.setText(result.get(1));
	   email.setText(result.get(2));
	   phone.setText(result.get(3));
	   
	   //buttons
	   Button done = new Button("Done", new ImageView(new Image(UserDisplay.class.getResourceAsStream("/res/Apply.png"))));
	   Button update1 = new Button("Update");
	   Button update2 = new Button("Update");
	   Button update3 = new Button("Update");
	   Button update4 = new Button("Update");
	   Button update5 = new Button("Update");
	   //Button takePicture = new Button("Take Picture");
	   Button select = new Button("Select", new ImageView(new Image(UserDisplay.class.getResourceAsStream("/res/Apply.png"))));
	   
	   //set on action
	   update1.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//update user info if not null
			if(firstName.getText() != null && !firstName.getText().isEmpty())
			{
			   //update first name
			   UserDisplay.updateFirstName(firstName.getText());	
				
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
			if(lastName.getText() != null && !lastName.getText().isEmpty())
			{
			   //update last name	
			   UserDisplay.updateLastName(lastName.getText());
			   
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
			if(email.getText() != null && !email.getText().isEmpty())
			{
			   //update email address	
			   UserDisplay.updateEmail(email.getText());
			   
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
			if(phone.getText() != null && !phone.getText().isEmpty())
			{
			   //update phone number
			   UserDisplay.updateEmployeePhoneNumber(phone.getText());
			   
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
			if(photoPath.getText() != null && !photoPath.getText().isEmpty())
			{
			   //update photo path
			   UserDisplay.updatePhoto();
			   
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
	   userPicture = Session.getUserPicture();
	   userPicture.setFitHeight(250);
	   userPicture.setFitWidth(200);
	   
	   //form layout
	   GridPane left = new GridPane();
	   
	   //setup top
	   left.setHgap(7);
	   left.setVgap(7);
	   left.setPadding(new Insets(10, 10, 10, 10));
	   left.setAlignment(Pos.CENTER);
	   
	   //setup top
	   left.add(firstlbl, 0, 0);
	   left.add(firstName, 1, 0);
	   left.add(update1, 2, 0);
	   left.add(lastlbl, 0, 1);
	   left.add(lastName, 1, 1);
	   left.add(update2, 2, 1);
	   left.add(emailAddresslbl, 0, 2);
	   left.add(email, 1, 2);
	   left.add(update3, 2, 2);
	   left.add(phoneNumberlbl, 0, 3);
	   left.add(phone, 1, 3);
	   left.add(update4, 2, 3);
	   
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
	   select.setOnAction(e -> selectImageFile(photoPath, userPicture));
	   //takePicture.setOnAction(e -> PhotoScreen.displayPhotoScreen(1));
	   
	   //right layout
	   VBox right = new VBox();
	   right.setSpacing(7);
	   right.setAlignment(Pos.CENTER);
	   
	   //photo layout
	   HBox photoLayout = new HBox();
	   photoLayout.setSpacing(7);
	   photoLayout.getChildren().addAll(update5, photoPath);
	   
	   //button
	   HBox topPhotoLayout = new HBox();
	   topPhotoLayout.setSpacing(7);
	   //topPhotoLayout.getChildren().addAll(select, takePicture);
	   topPhotoLayout.getChildren().addAll(select);
	   
	   //add nodes to right
	   right.getChildren().addAll(userPicture, topPhotoLayout, photoLayout);
	   
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
	   stage.setTitle("FASS Nova - Update User Info");
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(350);
	   stage.centerOnScreen();
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Update user first name
	 */
	private static void updateFirstName(String first)
	{
	   String query = "CALL updateFirstName(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, first);
		  ps.setString(2, Configs.getProperty("CurrentUser"));
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
	 * Update user last name
	 */
	private static void updateLastName(String last)
	{
	   String query = "CALL updateLastName(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, last);
		  ps.setString(2, Configs.getProperty("CurrentUser"));
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
	 * Update user email
	 */
	private static void updateEmail(String email)
	{
	   String query = "CALL updateEmail(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, email);
		  ps.setString(2, Configs.getProperty("CurrentUser"));
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
	 * Update user phone number
	 */
	private static void updateEmployeePhoneNumber(String phone)
	{
	   String query = "CALL updatePhoneNumber(?,?,?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, phone);
		  ps.setString(2, Configs.getProperty("CurrentUser"));
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
	 * Update user photo
	 */
	private static void updatePhoto()
	{
	   String query = "CALL updatePhoto(?,?,?)";
	  
	   try
	   {
		  FileInputStream input = new FileInputStream(file); 
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setBlob(1, input);
		  ps.setString(2, Configs.getProperty("CurrentUser"));
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
	 * Get employee info
	 */
	private static ObservableList<String> getEmployeeInfo()
	{
	   String query = "CALL listEmployeeBasicInfo(?,?)";
	   ObservableList<String> result = FXCollections.observableArrayList();
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, Configs.getProperty("CurrentUser"));
		  ps.setString(2, Configs.getProperty("StoreCode"));
		  
		  //execute
		  ResultSet rs = ps.executeQuery();
		  
		  //process
		  while(rs.next())
		  {
		     result.add(rs.getString(1));   
		     result.add(rs.getString(2));   	  
		     result.add(rs.getString(3));   	  
		     result.add(rs.getString(4));   	  
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
