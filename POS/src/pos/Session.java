package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.*;
import pos.UserDisplay;

public class Session {

	
	public static Connection openDatabase()
	{ 
 	   Connection myConn;
	   try {
		 myConn = DriverManager.getConnection(Configs.getProperty("DatabaseURL"));
	     return myConn;
	   } catch (SQLException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	   }
	  
 	   return null;
	}

	public static void logout(Stage stage) {
		
		//close the stage
		stage.close();
		
		//reset product list
		MainScreen.resetProductList();
		
		//set last login user
		Configs.saveProperty("LastLoginUser", Configs.getProperty("CurrentUser"));		
		
		//set current user to blank
		Configs.saveProperty("CurrentUser", "blank");
		
		//display the login
		Login.displayLogin();
	}
	
	public static void displayUpdatePasswordScreen()
	{ 
		//root layout
		GridPane root = new GridPane();			
		
		//create nodes
		Label pass = new Label("Password");
		Label confirmPass = new Label("Confirm Password");
		PasswordField password = new PasswordField();
		PasswordField confirmPassword = new PasswordField();
		
		Button accept = new Button("Accept", new ImageView(new Image(Session.class.getResourceAsStream("/res/Apply.png"))));
		Button cancel = new Button("Cancel", new ImageView(new Image(Session.class.getResourceAsStream("/res/Cancel.png"))));
		
		//set text fill color
		pass.setTextFill(Color.WHITE);
		confirmPass.setTextFill(Color.WHITE);
		
		//add nodes to the layout
		root.add(pass, 0, 0);
		root.add(password, 1, 0);
		root.add(confirmPass, 0, 1);
		root.add(confirmPassword, 1, 1);
		root.add(accept, 0, 2);
		root.add(cancel, 1, 2);
				
		//set id
		root.setId("border");
		
		//set spacing and padding
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setHgap(5);
		root.setVgap(8);
		
		//load css stylesheet
		root.getStylesheets().add(Session.class.getResource("MainScreen.css").toExternalForm());				
		
		//setup scene
		Scene scene = new Scene(root);
		
		//setup the stage
	    Stage stage = new Stage();
	    
	    //set scene
	    stage.setScene(scene);
	    
	    //set title
	    stage.setTitle("FASS Nova - Update User Password");
	    
		//implement actions
		cancel.setOnAction(e -> stage.close());
		accept.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				//close the stage
				stage.close();
				
				//call update password
				updatePassword(password.getText(), confirmPassword.getText());
			} 
		  	
		});
		
		//setup stage
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.centerOnScreen();
		stage.setMinWidth(350);
		stage.show();
	}
	
	public static void updatePassword(String pass, String confirmPass)
	{ 

		//check if password match
		if(pass.equals(confirmPass) && pass.length() > 0)
		{ 
		   String query = "CALL updatePassword(?, ?, ?)";
		   
		   try
		   { 
			   Connection conn = openDatabase();
			   PreparedStatement ps = conn.prepareStatement(query);
			   
			   //set parameters
			   ps.setString(1, Configs.getProperty("CurrentUser"));
			   ps.setString(2, pass);
			   ps.setString(3, Configs.getProperty("StoreCode"));
			   
			   //execute query
			   int rows = ps.executeUpdate();
			   
			   if(rows > 0)
			   { 
				   AlertBox.display("FASS Nova", "User password updated successfully!"); 
			   }	
			   else
			   { 
			       AlertBox.display("FASS Nova - Error", "An error has occurred, please try again!");	   
			   }	   
		   }
		   catch(Exception e)
		   { 
			   e.printStackTrace();
			   AlertBox.display("FASS Nova - Error", "Could not connect to database");
		   }
		   	   
		}	
		else
		{ 
		   AlertBox.display("FASS Nova - Error", "Passwords do not match!");
		   
		   //display screen again
		   displayUpdatePasswordScreen();
		}	
			
	}
	
	public static void passwordValidation()
	{ 
		//root layout
		GridPane root = new GridPane();			
		
		//create nodes
		Label user = new Label("Username");
		Label pass = new Label("Password");
		TextField username = new TextField();
		PasswordField password = new PasswordField();
		
		Button accept = new Button("Accept", new ImageView(new Image(Session.class.getResourceAsStream("/res/Apply.png"))));
		Button cancel = new Button("Cancel", new ImageView(new Image(Session.class.getResourceAsStream("/res/Cancel.png"))));
		
		//set text fill color
		user.setTextFill(Color.WHITE);
		pass.setTextFill(Color.WHITE);
		
		//set username editable to false
		username.setEditable(false);
		username.setText(Configs.getProperty("CurrentUser"));
		
		//add nodes to the layout
		root.add(user, 0, 0);
		root.add(username, 1, 0);
		root.add(pass, 0, 1);
		root.add(password, 1, 1);
		root.add(accept, 0, 2);
		root.add(cancel, 1, 2);
				
		//set id
		root.setId("border");
		
		//set spacing and padding
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setHgap(5);
		root.setVgap(8);
		
		//load css stylesheet
		root.getStylesheets().add(Session.class.getResource("MainScreen.css").toExternalForm());				
		
		//setup scene
		Scene scene = new Scene(root);
		
		//setup the stage
	    Stage stage = new Stage();
	    
	    //set title
	    stage.setTitle("FASS Nova - Update User Password");
	    
		//implement actions
		cancel.setOnAction(e -> stage.close());
		accept.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				//get input
				String userName = username.getText();
				String passWord = password.getText();
				
				
				//validate input
				String query = "SELECT Login(?,?,?)";
				
				try
				{ 
					Connection conn = openDatabase();
				    PreparedStatement ps =  conn.prepareStatement(query);
				    
				    //set parameters
				    ps.setString(1, userName);
				    ps.setString(2, passWord);
				    ps.setString(3, Configs.getProperty("StoreCode"));
				    
				    //execute query
				    ResultSet rs = ps.executeQuery();
				    
				    while(rs.next())
				    {	
				       if(rs.getString(1).equals("1"))
				       { 
				    	   //close current screen
				    	   stage.close();
				    	
				          //go to the next screen	
					      displayUpdatePasswordScreen();
				       }					    
				       else
				       {
				    	   
				    	   AlertBox.display("FASS Nova", "The password provided is incorrect");
				    	
				       }	
				   }
				}    
				catch(Exception e)
				{ 
				   e.printStackTrace();
				   
				   AlertBox.display("FASS Nova - Error", "An error has occurred");	

				}
				
			}
			
		});
	    
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setMinWidth(310);
	    stage.centerOnScreen();
	    
	    //set scene and show
	    stage.setScene(scene);
	    stage.showAndWait();
	    
	}

	public static void getPrivilegeLevel() {
       
		String query = "SELECT privilege FROM Employee WHERE Username = ? AND employeeStoreCode = ?";
		try
		{ 
		   Connection conn = openDatabase();
		   PreparedStatement ps = conn.prepareStatement(query);
		   
		   //set parameters
		   ps.setString(1, Configs.getProperty("CurrentUser"));
		   ps.setString(2, Configs.getProperty("StoreCode"));
		   
		   //execute query
		   ResultSet rs = ps.executeQuery();
		   
		   while(rs.next())
		   { 
			   Configs.saveProperty("Privilege", rs.getString(1));   
		   }	   
		}		
		catch(Exception e)
		{ 
			e.printStackTrace();
		}
	}
}
