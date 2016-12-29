package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import pos.UserDisplay;

public class Session {

	public static Connection openDatabase()
	{ 
 	   Connection myConn;
	   try {
		 myConn = DriverManager.getConnection("jdbc:mysql://Atomic-PC:3306/test?autoReconnect=true&useSSL=false", "root", "cybertronic");
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
		
		//reset observable list
		
		//display the login
		Login.displayLogin();
	}
	
}
