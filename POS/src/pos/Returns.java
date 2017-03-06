package pos;

import java.sql.Connection;
import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Returns {

	/*
	 *  Display the search ticket window
	 */
	public static void displayTicketSearchScreen()
	{ 
	   //stage
		Stage stage = new Stage();
		
		//text field
		NumericTextField searchField = new NumericTextField();
		
		searchField.setPromptText("Example: 1.1.03.02.1");
		
		//label
		Label label = new Label("Ticket No");
		Label example = new Label("Example: 1.1.03.03.1");
		example.setTextFill(Color.WHITE);
		label.setTextFill(Color.WHITE);
		example.setFont(new Font("Courier Sans", 12));
		label.setFont(new Font("Courier Sans", 12));	
		
		
		//button
		Button search = new Button("Search", new ImageView(new Image(Returns.class.getResourceAsStream("/res/search2.png"))));
		Button cancel = new Button("Cancel", new ImageView(new Image(Returns.class.getResourceAsStream("/res/Cancel.png"))));
		
		//set on action
		cancel.setOnAction(e -> stage.close());
		search.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
				
				// close the stage
				stage.close();
				
				//search for the ticket
				
				
			}
			
		});
		
		//hbox
		HBox bottom = new HBox();
		
		//setup bottom
		bottom.setAlignment(Pos.CENTER);
		bottom.setSpacing(5);
		
		//add nodes to children
		bottom.getChildren().addAll(search, cancel);
		
		//root layout
		VBox root = new VBox();
		
		//add nodes to root
		root.getChildren().addAll(searchField, example, bottom);
		
		//set spacing
		root.setSpacing(6);
		root.setPadding(new Insets(20, 20, 20, 20));
		
		//set alignment
		root.setAlignment(Pos.CENTER);
		
		//set id
		root.setId("border");
		
		//load style sheets
		root.getStylesheets().add(Returns.class.getResource("MainScreen.css").toExternalForm());
		
		//setup stage
		stage.setTitle("FASS Nova - Search ticket");
		stage.setMinWidth(300);
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		
		//scene
		Scene scene = new Scene(root);
		
		//set scene
		stage.setScene(scene);
		
		//show
		stage.showAndWait();
	}
	
	/*
	 * Search for sales ticket
	 */
	private static void searchTicket(String ticketno)
	{ 
	   String query = "";
	   
	   try
	   { 
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, ticketno);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  if(!rs.next())
		  { 
		     AlertBox.display("FASS Nova", "Could not find ticket");	  
		  }	  
		  else
		  { 
			   while(rs.next())
			   { 
				        
			   }	   
		  }	  
	   }
	   catch(Exception e)
	   { 
		  AlertBox.display("FASS Nova", "Could not find product");   
	   }
	}
}
