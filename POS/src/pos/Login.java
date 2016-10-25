package pos;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;
import java.sql.*;
import java.sql.Connection;


public class Login {

	   public void displayLogin()
	   {
		  //setup the login window 
		  Stage window = new Stage();
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
		  
		  //set action listener
		  loginButton.setOnAction(e -> login(username.getText(), password.getText()));
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
		  login.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
		  
		  //add the scene to window
		  window.setScene(login);
		  
		  //set window size
		  window.setWidth(400);
		  window.setHeight(250);
		  
		  //show the window
		  window.show();
	   }
	   

	public void login(String username, String password)
	   { 
		   if(!username.isEmpty() && !password.isEmpty())
		   { 
			  //Connect to Mysql
			  try {
				  //get a connection to the database
				  Connection myConn = DriverManager.getConnection("jdbc:mysql://Atomic-PC:3306/test", "root", "cybertronic");
				  
				  //Create a statament
				  Statement myStmt = myConn.createStatement();
				  
				  //create a result set
				  ResultSet rs = myStmt.executeQuery("show tables");
				  
				  //proces the result set
				  int i =1;
				  while(rs.next())
				  { 
				     System.out.println(i);
				     i++;
				  }	  
			  }
			  catch(Exception e)
			  { 
				  e.printStackTrace();
			  }
			   
		   }
		   else
		   { 
			   //Create alert box
			   AlertBox.display("Login Error", "Please fill in all required fields");
		   }
	   }
	
}