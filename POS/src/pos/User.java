package pos;

import java.sql.*;

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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
		MenuItem demote = new MenuItem("Demote/Promote User");
		Menu actions = new Menu("Actions");
		MenuBar menu = new MenuBar();
		
		//create stage
		Stage window = new Stage();
		
		//set menu
		menu.prefWidthProperty().bind(window.widthProperty());
		
		//add items to menu
		actions.getItems().addAll(deleteUser, createUser, demote);
		
		//add menu to menu bar
		menu.getMenus().add(actions);
		
		//set on action
		demote.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(Integer.parseInt(Configs.getProperty("Privilege")) == 3)
			   {
				  if(table.getSelectionModel().getSelectedItem() != null)
				  {	  
			         //promote user
				      updatePrivilegeLevel(table.getSelectionModel().getSelectedItem().getUsername());
				  } 
				  else
				  {
				     AlertBox.display("FASS Nova", "Select a user");	  
				  }	  
			   }	
			   else
			   {
				  AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }   
			}
			
		});
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
	 * Privilege level screen
	 */
	private static void updatePrivilegeLevel(String userName)
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //labels
	   Label userlbl = new Label("User");
	   Label privilegelbl = new Label("Privilege Level");
	   userlbl.setTextFill(Color.WHITE);
	   privilegelbl.setTextFill(Color.WHITE);
	   
	   //text
	   TextField user = new TextField();
	   
	   //set text
	   user.setText(userName);
	   
	   //choice box
	   ChoiceBox<String> options = new ChoiceBox<String>();
	   
	   //levels
	   ObservableList<String> levels = FXCollections.observableArrayList();
	   
	   //add levels 
	   levels.add("0");
	   levels.add("1");
	   levels.add("2");
	   levels.add("3");
	   
	   //add options
	   options.setItems(levels);
	   
	   //set default levels
	   options.setValue("0");
	   
	   //top
	   GridPane top = new GridPane();
	   top.setAlignment(Pos.CENTER);
	   top.setVgap(7);
	   top.setHgap(7);
	   
	   //add nodes to top
	   top.add(userlbl, 0, 0);
	   top.add(user, 1, 0);
	   top.add(privilegelbl, 0, 1);
	   top.add(options, 1, 1);
	   
	   //button
	   Button update = new Button("Update");
	   
	   //set on action
	   update.setOnAction(e -> User.updatePrivilege(userName, Integer.parseInt(options.getValue())));
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setSpacing(7);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().add(update);
	   
	   //root
	   VBox root = new VBox();
	   
	   //setup root
	   root.setPadding(new Insets(10, 10, 10, 10));
	   root.setAlignment(Pos.CENTER);
	   root.setSpacing(10);
	   
	   //add nodes
	   root.getChildren().addAll(top, bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //get style sheets
	   root.getStylesheets().add(User.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Update Privilege Level");
	   stage.setMinWidth(300);
	   stage.centerOnScreen();
	   stage.initModality(Modality.APPLICATION_MODAL);
	   
	   //set scene
	   stage.setScene(new Scene(root));
	   
	   //show 
	   stage.showAndWait();
	}
	
	/*
	 * Update privilege level
	 */
	private static void updatePrivilege(String user, int privilege)
	{
	   String query = "CALL promoteUser(?,?)";	
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, user);
		  ps.setInt(2, privilege);
		  
		  //execute
		  ps.executeUpdate();
		  
		  //close
		  conn.close();
		  
		  //update locally
		  Configs.saveProperty("Privilege", Integer.toString(privilege));
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
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
