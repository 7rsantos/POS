package pos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.embed.swing.SwingFXUtils;
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
import javax.crypto.*;
import javax.imageio.ImageIO;
import javax.security.*;
import java.util.Base64;

public class Session {

	private static Cipher ecipher;
	private static Cipher dcipher;
	
	public static Connection openDatabase()
	{ 
 	   Connection myConn;
	   try {
		 myConn = DriverManager.getConnection(Configs.getProperty("DatabaseURL"));
	     return myConn;
	   } catch (SQLException e) {
		   
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
			   ps.setString(2, Session.encrypt(pass));
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
	
	public static void passwordValidation(int caller)
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
				    ps.setString(2, Session.encrypt(passWord));
				    ps.setString(3, Configs.getProperty("StoreCode"));
				    
				    //execute query
				    ResultSet rs = ps.executeQuery();
				    
				    while(rs.next())
				    {	
				       if(rs.getString(1).equals("1"))
				       { 
				    	   //close current screen
				    	   stage.close();
				    	
				    	  if(caller ==1)
				    	  {	  
				             //go to the next screen	
					         displayUpdatePasswordScreen();
				    	  }
				    	  if (caller == 2)
				    	  { 
				    		  
				    		  //open the cash drawer
				    		  openCashDrawer();
				    	  }		  
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
	
	/*
	 *  Password encryption using Java 8 inbuilt decoder 64
	 */
	public static String encrypt(String password)
	{
	   	
	   try
	   { 		  
		  String result =  Base64.getUrlEncoder().encodeToString(password.getBytes("utf-8"));	  
		  return result;
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();  
	   }
	   
	   return null;
	}
	
	/*
	 *  Decrypt passwords using Java 8 inbuilt decryption
	 */
	public static String decrypt(String password)
	{ 
	   String result;	
	   try
	   { 
		  result =  java.net.URLDecoder.decode(password, "utf-8");
		  return result;
	   }
	   catch(Exception e)
	   { 
	      e.printStackTrace();	   
	   }
	   return null;	
	}
	
	/*
	 *  Get the current user's picture
	 */
	public static ImageView getUserPicture()
	{ 
		
	    //byte array
	    byte[] imageArray = null;
	    Image image = null;
		
		String query = "SELECT Photo FROM Employee WHERE Employee.Username = ?"
				+ " AND employeeStoreCode = ?";
		
		try
		{ 
		   Connection conn = Session.openDatabase();
		   
		   PreparedStatement ps = conn.prepareStatement(query);
		   
		   //set parameters
		   ps.setString(1, Configs.getProperty("CurrentUser"));
		   ps.setString(2, Configs.getProperty("StoreCode"));
		   
		   //execute query
		   ResultSet rs = ps.executeQuery();
		   
		   while(rs.next())
		   { 
	    	   imageArray= rs.getBytes(1);			     
		   }	   
		   
		   //get image
	       InputStream in = new ByteArrayInputStream(imageArray);    	  
	       BufferedImage bufferedImage = ImageIO.read(in);    	  
	       image = SwingFXUtils.toFXImage(bufferedImage, null);
		   
		   conn.close();
		}
		catch(Exception e)
		{ 
		   e.printStackTrace();  	
		}
		
		ImageView profilePicture = new ImageView(image);
		
		//set fit width and height
		profilePicture.setFitHeight(50);
		profilePicture.setFitWidth(70);
		
		return profilePicture;
	}
	
	/*
	 *  Get current user's first name and initial
	 */
	public static String getUserFirstName()
	{ 
		String query = "Select firstName, lastName from Employee WHERE Employee.username = ?"
				+ " AND Employee.employeeStoreCode = ?";
		
		String result = "";
		
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
			  result = rs.getString(1) + "  " + rs.getString(2).substring(0, 1) + ".";   
		   }	   
		}
		catch(Exception e)
		{ 
		   e.printStackTrace();	
		}
		
		return result;
	}
	
	/*
	 * Open the cash drawer
	 */
	public static void openCashDrawer()
	{ 
	   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)
	   { 
	      //bytes to open cash drawer
		  byte[] open = new byte[] {0x1B, 0x70, 0x30, 0x37, 0x79};	 
		  
		  //open the cash drawer
		  PrinterService.printBytes(Configs.getProperty("Printer"), open);
	   }
	   else
	   { 
		  AlertBox.display("FASS Nova", "You do not have enough privileges to perform this action");   
	   }	   
	}
	
}
