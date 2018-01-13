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


import org.apache.log4j.Logger;

import java.util.Base64;

public class Session {

	@SuppressWarnings("unused")
	private static Cipher ecipher;
	@SuppressWarnings("unused")
	private static Cipher dcipher;
	private static Logger logger = Logger.getLogger(Session.class);
	
	public static Connection openDatabase()
	{ 
 	   Connection myConn;
	   try {
		   
		 myConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/pos?autoReconnect=true&rewriteBatchedStatements=true&useSSL=false&user=root&password=tienda1228");
		 //System.out.println(myConn.getWarnings());
	     return myConn;
	     
	   } catch (SQLException e) {
		   
		  logger.error("Could not connect to database", e);
	   }
	   
	   logger.info("Connection is null");
	   
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
		root.setPadding(new Insets(20, 20, 20, 20));
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
			   
			   ps.close();
			   conn.close();
		   }
		   catch(Exception e)
		   { 
			   logger.error("Could not update password", e);
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
		root.setPadding(new Insets(30, 30, 30, 30));
		root.setHgap(5);
		root.setVgap(8);
		
		//load css stylesheet
		root.getStylesheets().add(Session.class.getResource("MainScreen.css").toExternalForm());				
		
		//setup scene
		Scene scene = new Scene(root);
		
		//setup the stage
	    Stage stage = new Stage();
	    
	    //set title
	    stage.setTitle("FASS Nova - Password Validation");
	    
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
				String query = "CALL loginUser(?,?,?)";
				
				try
				{ 
					Connection conn = openDatabase();
				    PreparedStatement ps =  conn.prepareStatement(query);
				    
				    //set parameters
				    ps.setString(1, userName);
				    ps.setString(2, encrypt(passWord));
				    ps.setString(3, Configs.getProperty("StoreCode"));
				    
				    //execute query
				    ResultSet rs = ps.executeQuery();
				    
				    while(rs.next())
				    {	
				       boolean userExists = rs.getBoolean(1);	
				       if(userExists)
				       { 
				    	   //close current screen
				    	   stage.close();
				    	
				    	  if(caller ==1)
				    	  {	  
				             //go to the next screen	
					         displayUpdatePasswordScreen();
				    	  }
				    	  else if (caller == 2)
				    	  { 
				    		  
				    		  //open the cash drawer
				    		  openCashDrawer();
				    	  }		  
				    	  else if (caller == 3)
				    	  {
				    	      //update user info
				    		  UserDisplay.displayUserUpdate();
				    	  }	  
				    	  else if (caller == 4)
				    	  {
				    		  //update store info
				    		  Setup.displayStoreUpdate();
				    	  }	  
				    	  else
				    	  {
				    	      //update printer
				    		  Setup.setupPrinter(2);
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
				   logger.error("Could not validate password", e);
				   
				   AlertBox.display("FASS Nova - Error", "An error has occurred");	

				}
				
			}
			
		});
	    
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setMinWidth(350);
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
		   
		   rs.close();
		   ps.close();
		   conn.close();
		}		
		catch(Exception e)
		{ 
			logger.error("Could not get privilege level", e);
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
		  logger.error("Could not encrypt password", e);  
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
	      logger.error("Could not decrypt password", e);	   
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
	       
	       ps.close();
	       rs.close();
		   conn.close();
		   
		}
		catch(Exception e)
		{ 
		   logger.error("Could not get user picture", e); 	
		}
		
		ImageView profilePicture = new ImageView(image);
		
		//set fit width and height
		profilePicture.setFitHeight(70);
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
		   
		   rs.close();
		   ps.close();
		   conn.close();
		   
		}
		catch(Exception e)
		{ 
		   logger.error("Could not get first name", e);	
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
		  //byte[] open = new byte[] {0x1B, 0x70, 0x30, 0x37, 0x79};
		  byte[] openStar = new byte[] {0x1B, 0x7, 0xA, 0x32, 0x7};
		  
		  //open the cash drawer
		  PrinterService.printBytes(Configs.getProperty("Printer"), openStar);
	   }
	   else
	   { 
		  AlertBox.display("FASS Nova", "You do not have enough privileges to perform this action");   
	   }	   
	}
	
}
