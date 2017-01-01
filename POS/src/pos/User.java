package pos;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
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
			      System.out.print(employees.get(0).getPrivilege());
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
		
		//root layout
		FlowPane root = new FlowPane();
		
		//create table
		TableView<Employee> table = Employee.createEmployeeTable();
		
		//set items
		table.setItems(employees);
		
		//add nodes to root
		root.getChildren().add(table);
		
		//set id
		root.setId("border");
		
		//load stylesheet
		root.getStylesheets().add(User.class.getResource("MainScreen.css").toExternalForm());
		
		//create stage
		Stage window = new Stage();
		
		//create scene
		Scene list = new Scene(root);
		
		//setup stage
		window.setTitle("FASS Nova - " + Configs.getProperty("StoreName") + " Employee List");
		window.setMinWidth(600);
		//window.setHeight(500);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setResizable(false);
		window.centerOnScreen();
		
		//set scene and show
		window.setScene(list);
		window.showAndWait();
	}
}
