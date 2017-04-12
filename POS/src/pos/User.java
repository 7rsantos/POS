package pos;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class User {

	public static ObservableList<Employee> listEmployees()
	{ 
		String query = "CALL listEmployees(?)";
		ObservableList<Employee> employees = FXCollections.observableArrayList();
		int i = 1;
		
		if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)
		{	
		   try
		   { 
			   Connection conn = Session.openDatabase();
			   PreparedStatement ps = conn.prepareCall(query);
			   
			   //set parameter
			   ps.setString(1, Configs.getProperty("StoreCode"));
			   
			   //execute query
			   ResultSet rs = ps.executeQuery();
			   
			   while(rs.next())
			   { 
			      employees.add(new Employee(rs.getString(1), rs.getString(2), 
			    		  rs.getString(3), rs.getString(4), rs.getString(5),
			    		  rs.getString(6), rs.getString(7)));
			   }	   		
		   }
		   catch(Exception e)
		   { 
			   e.printStackTrace();
		   }
	    }   
		else
		{
			AlertBox.displayDialog("FASS Nova", "You do not have permission to perform this action");
		}	
		
		return employees;
	}
	
	public static void displayUsers()
	{ 
		//observable list with employee data
		ObservableList<Employee> employees = listEmployees();
		
		//create table
		TableView<Employee> table = Employee.createEmployeeTable();
		
		//menu
		MenuItem deleteUser = new MenuItem("Delete User");
		MenuItem createUser = new MenuItem("Create User");
		Menu actions = new Menu("Actions");
		MenuBar menu = new MenuBar();
		
		//create stage
		Stage window = new Stage();
		
		//set menu
		menu.prefWidthProperty().bind(window.widthProperty());
		
		//add items to menu
		actions.getItems().addAll(deleteUser, createUser);
		
		//add menu to menu bar
		menu.getMenus().add(actions);
		
		//set on action
		createUser.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(Integer.parseInt(Configs.getProperty("Privilege")) == 3)
			   {
			      UserDisplay.addUserDisplay();   	    
			   }	
			   else
			   {
				  AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }	   
			}
			
		});
		
		deleteUser.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(Integer.parseInt(Configs.getProperty("Privilege")) == 3)
			   {
				  if(table.getSelectionModel().getSelectedItem() != null)
				  {	  
			         //delete user
				     User.deleteUser(table.getSelectionModel().getSelectedItem().getUsername());
				  }
				  else
				  {
				     AlertBox.display("FASS Nova", "You do not have permission to perform this action");	  
				  }	  
			   }	
			   else
			   {
				  AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }	   
			}
			
		});
		
		//root layout
		VBox root = new VBox();
		
		//set items
		table.setItems(employees);
		
		//setup root
		root.setSpacing(10);
		root.setAlignment(Pos.TOP_CENTER);
		
		//add nodes to root
		root.getChildren().addAll(menu, table);
		
		//set id
		root.setId("border");
		
		//load stylesheet
		root.getStylesheets().add(User.class.getResource("MainScreen.css").toExternalForm());
		
		//create scene
		Scene list = new Scene(root);
		
		//setup stage
		window.setTitle("FASS Nova - " + Configs.getProperty("StoreName") + " Employee List");
		window.setMinWidth(600);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setResizable(false);
		window.centerOnScreen();
		
		//set scene and show
		window.setScene(list);
		window.showAndWait();
	}
	
	/*
	 * Delete user from database
	 */
	private static void deleteUser(String user)
	{
	   String query = "CALL deleteUser(?,?)";
	   
	   try
	   {
	      Connection conn = Session.openDatabase();
	      PreparedStatement ps = conn.prepareStatement(query);
	      
	      //set parameters
	      ps.setString(1, user);
	      ps.setString(2, Configs.getProperty("StoreCode"));
	      
	      //execute
	      ps.execute();
	      
	      //close
	      conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
}
