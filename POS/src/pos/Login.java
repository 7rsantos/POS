package pos;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.sql.*;
import java.sql.Connection;

import pos.Session;

public class Login {

public static Stage window;	
	
	   public static void displayLogin()
	   {
		  //setup the login window 
		  window = new Stage();
		  window.setTitle("FASS Nova");
		  window.setResizable(false);
		  window.centerOnScreen();		 
		  
		  //create a border pane and set insets
		  BorderPane border = new BorderPane();		  
		  border.setPadding(new Insets(10,50,50,50));
		  
		  //add Hbox
		  HBox hb = new HBox();
		  hb.setPadding(new Insets(20,20,20,30));

		  
		  //Create the text fields, labels, and buttons
		  Label user = new Label("Username");
		  Label pass = new Label("Password");
		  TextField username = new TextField();
		  PasswordField password = new PasswordField();
		  Button loginButton = new Button("Login");
		  Button clear = new Button("Clear");
				  
		  
		  //Add all nodes to Grid Pane
		  GridPane root = new GridPane();
		  
		  root.setPadding(new Insets(20,20,20,20));
		  root.setHgap(5);
		  root.setVgap(5);

		  
		  root.add(user, 0, 0);
		  root.add(username, 1, 0);
		  root.add(pass, 0, 1);
		  root.add(password, 1, 1);
		  root.add(loginButton, 2, 2);
		  root.add(clear, 1, 2);
		  root.insetsProperty();
		  
		  //debugging
		  username.setText("admin");
		  password.setText("changeme");
		  
		  
		  //set action listener
		  loginButton.setOnAction(e -> login(username.getText(), password.getText(), window));
		  clear.setOnAction(new EventHandler<ActionEvent>() { 
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				username.setText("");
				password.setText("");
			}	  
          });
		  
		  
	      //Reflection for gridPane
		  Reflection r = new Reflection();
		  r.setFraction(0.7f);
		  root.setEffect(r);
		  
          //DropShadow effect
		  DropShadow dropShadow = new DropShadow();
		  dropShadow.setOffsetX(5);
		  dropShadow.setOffsetY(5);
		  
		  //Adding text and DropShadow effect to it
		  Text text = new Text("FASS Nova");
		  text.setFill(Color.WHITE);
		  text.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		  text.setEffect(dropShadow);

		  //Add text to Hbox
		  hb.getChildren().add(text);
		  
		  //add ids to the nodes
		  border.setId("border");
		  root.setId("root");
		  loginButton.setId("login");
		  clear.setId("clear");
		  
		  //Add the Grid Pane and HBox to Border Pane
		  border.setTop(hb);
		  border.setCenter(root);
		  
		  //setup the scene
		  Scene login = new Scene(border);
		  login.getStylesheets().add(Login.class.getResource("login.css").toExternalForm());
		  
		  //add the scene to window
		  window.setScene(login);
		  
		  //set window size
		  window.setWidth(400);
		  window.setHeight(250);
		  
		  //show the window
		  window.show();
	   }
	   

	public static void login(String username, String password, Stage stage)
	   { 
		   if(!username.isEmpty() && !password.isEmpty())
		   { 
			  //Connect to Mysql
			  try {
				  //get a connection to the database
				  Connection myConn = Session.openDatabase();
				  
				  //Create a statement
				  CallableStatement myStmt = myConn.prepareCall("SELECT Login(" + "'" + username + "'" + "," + "'" + password + 
						  "'" + ", " + "'" + Configs.getProperty("StoreCode") + "'" + ")");				  				  
				  
				  //create a result set
				  ResultSet rs = myStmt.executeQuery();
				  
				  
				  
				  //process the result set
				  while (rs.next())
				  {	  
					  
                     if(rs.getString(1).equals("1"))
                     {
                    	 //set current user
                    	 Configs.saveProperty("CurrentUser", username);
                    	 
                    	 //set priority level
                    	 Session.getPrivilegeLevel();
                    	 
                        //MainScreen ms = new MainScreen(stage);
                    	 window.close();
                    	 Scene mainScreen = MainScreen.displayMainScreen(stage);
                    	 stage.setScene(mainScreen);
                    	 stage.show();
                     }	  
                     else
                     { 
                	    AlertBox.display("Login Error", "Wrong username or password");
                     }
				  }  
				  
			  }
			  catch(Exception e)
			  { 
				  e.printStackTrace();
				  AlertBox.display("Connection Error", "Could not connect to the database, check your connection settings.");
			  }
			   
		   }
		   else
		   { 
			   //Create alert box
			   AlertBox.display("Login Error", "Please fill in all required fields");
		   }
	   }
	
}
