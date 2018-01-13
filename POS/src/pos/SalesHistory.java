package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SalesHistory {
	
	private static Logger logger = Logger.getLogger(SalesHistory.class);

	/*
	 * Search for all sales that occurred on a given date
	 */
	
	public static ObservableList<SalesTicket> searchSalesHistory(String date)
	{ 
		//result observable list
		ObservableList<SalesTicket> result = FXCollections.observableArrayList();
		
	   String query = "CALL listTickets(?)";	
	   try
	   { 
		  Connection conn = Session.openDatabase();
		  
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, date);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  while(rs.next())
		  { 
			 result.add(new SalesTicket(rs.getString(1), rs.getString(2), rs.getDouble(3),
					     rs.getString(4), rs.getString(5))); 
		  }	   
		  
		  //close the connection
		  rs.close();
		  ps.close();
		  conn.close();
	   }
	   catch(Exception e)
	   { 
	      logger.error("Could not search history", e);	   
	   }
	   
	   return result;
	}

	
	/*
	 *  Display sales history for a certain day
	 */
	public static void displaySalesHistory()
	{ 
	   //root layout
		BorderPane root = new BorderPane();
		
		//stage
		Stage stage = new Stage();
		
		//buttons
		Button done = new Button("Done", new ImageView(new Image(SalesHistory.class.getResourceAsStream("/res/Apply.png"))));
		Button search = new Button("Search", new ImageView(new Image(SalesHistory.class.getResourceAsStream("/res/search2.png"))));
		
		//menu
		MenuBar menu = createMenu();
		menu.prefWidthProperty().bind(stage.widthProperty());
		
		//date picker
		DatePicker datePicker = new DatePicker();

		//search layout
		HBox searchLayout = new HBox();
		searchLayout.setSpacing(5);
		searchLayout.setAlignment(Pos.CENTER);
		searchLayout.getChildren().addAll(datePicker, search);
		
		//top layout
		VBox top = new VBox();
		top.setAlignment(Pos.CENTER);
		top.setPadding(new Insets(0, 0, 10, 0));
		top.setSpacing(7);
		
		//add nodes to top layout
		top.getChildren().addAll(menu, searchLayout);
		
		//table view for ticket info
		TableView<SalesTicket> table = SalesTicket.getSalesTicketTable();
		
		//date variable
		Date date = new Date();
		String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
		
		//add items to table
		ObservableList<SalesTicket> tickets = FXCollections.observableArrayList();
		tickets = searchSalesHistory(dateFormat);
		table.setItems(tickets);
		
		//table view for ticket details
		TableView<Product> details = createDetailsTable();		
		
		//observable list
		ObservableList<Product> products = FXCollections.observableArrayList();
		
		//add listener to selection
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
               				
				if(table.getSelectionModel().getSelectedItem() != null)
				{ 
					//clear any items in products list
					products.clear();
					
					//query to list ticket details
				    String query = "CALL listItems(?)"; 
				    String query2 = "CALL listWD(?)";
				    String query3 = "CALL listSR(?)";
				    
				    try
				    { 
				       Connection conn = Session.openDatabase();
				       
				       PreparedStatement ps = conn.prepareStatement(query);
				       
				       //set parameters
				       ps.setString(1, table.getSelectionModel().getSelectedItem().getTicketno());
				       
				       //execute query
				       ResultSet rs = ps.executeQuery();
				       
				       //process the result set
				       while(rs.next())
				       { 
				    	  products.add(new Product(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));   
				       }	   
				       
				       //second query
				       ps.clearParameters();
				       
				       //set query
				       ps = conn.prepareStatement(query2);
				       
				       //set parameters
				       ps.setString(1, table.getSelectionModel().getSelectedItem().getTicketno());
				       
				       //execute
				       rs = ps.executeQuery();
				       
				       //process the result set
				       while(rs.next())
				       {
				    	  products.add(new Product(rs.getString(1), rs.getString(2), 1, rs.getDouble(3)));   
				       }
				       
				       //third query
				       ps.clearParameters();
				       ps = conn.prepareStatement(query3);
				       
				       //set parameters
				       ps.setString(1, table.getSelectionModel().getSelectedItem().getTicketno());
				       
				       //execute query
				       rs = ps.executeQuery();
				       
				       //process the result set
				       while(rs.next())
				       {
				    	  products.add(new Product(rs.getString(2) + " " + rs.getInt(1), "0", 1, rs.getDouble(3)));       
				       }	   
				       
				       //set table items
				       details.setItems(products);
				       
				       //refresh tables
				       details.refresh();
				       
				       //close
				       rs.close();
				       ps.close();
				       conn.close();
				       
				    }
				    catch(Exception e)
				    { 
				       logger.error("Could not load sales history", e);	
				    }
				}	

			} 
			
		});
		
		//center
		HBox center = new HBox();
		
		//set center
		center.setAlignment(Pos.CENTER);
		center.setSpacing(5);
		
		//add nodes to center
		center.getChildren().addAll(table, details);
		
		//set table properties
		table.prefHeightProperty().bind(center.heightProperty());
        table.prefWidthProperty().bind(center.widthProperty());
        
		//set details table properties
		details.prefHeightProperty().bind(center.heightProperty());
        details.prefWidthProperty().bind(center.widthProperty());
		
		//bottom layout
		FlowPane bottom = new FlowPane();
		bottom.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.setPadding(new Insets(10, 10, 10, 10));
		
		//add nodes
		bottom.getChildren().addAll(done);
		
		//setup root
		root.setTop(top);
		//root.setLeft(left);
		//root.setRight(right);
		root.setCenter(center);
		root.setBottom(bottom);
		
		//implement actions
		done.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
                
			    //close the current stage
			    stage.close();
				
				//create new stage
				Stage window = new Stage();
				
				Scene main = MainScreen.displayMainScreen(window);
				
				//set scene
				window.setScene(main);
				
				//show the new scene
				window.show();
			} 
			
		});
		
		search.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   
				if(datePicker.getValue() != null)
				{	
				   //build the date with format yyyy-MM-dd	
				   String dateFormat2 = datePicker.getValue().getYear() + "-" + 
						   datePicker.getValue().getMonthValue() + "-" + datePicker.getValue().getDayOfMonth();
				
				   //call search history
				   ObservableList<SalesTicket> sales = searchSalesHistory(dateFormat2);
				   table.setItems(sales);
				
				   //refresh the table
				   table.refresh();
				}
				else
				{ 
					AlertBox.display("FASS Nova - Error", "Please select a date");
				}	
			} 
			
		});
		
		//set id
		root.setId("border");
		
		//load stylesheet
		root.getStylesheets().add(SalesHistory.class.getResource("MainScreen.css").toExternalForm());
		
		//create scene
		Scene scene = new Scene(root);
		
		//setup the stage
		stage.setTitle("FASS Nova - Sales History");
		
		//set the size of the window
		Screen screen = Screen.getPrimary(); 
		Rectangle2D bounds = screen.getVisualBounds(); 

		stage.setX(bounds.getMinX()); 
		stage.setY(bounds.getMinY()); 
		stage.setMinHeight(bounds.getHeight()); 
		stage.setMinWidth(bounds.getWidth()); 
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.setScene(scene);
		
		//show the stage
		stage.show();
	}
	
	/*
	 * Create menu bar for sales history screen
	 * 
	 */
	public static MenuBar createMenu()
	{ 
		MenuBar menu = new MenuBar();
		
		//create actions menu
		Menu actions = new Menu("Actions");
		
		//create menu items
		MenuItem select = new MenuItem("Select ticket");
		MenuItem modify = new MenuItem("Modify ticket");
		MenuItem refund = new MenuItem("Refund ticket");
		
		//add menu items to actions menu
		actions.getItems().addAll(select, modify, refund);
		
		//add actions to menu bar
		menu.getMenus().add(actions);
		
        //implement actions			
		
		return menu;
	}
	
	/*
	 * Create table view for details
	 */
	@SuppressWarnings("unchecked")
	public static TableView<Product> createDetailsTable()
	{ 
		TableView<Product> table = new TableView<Product>();
		
		//create columns
		TableColumn<Product, String> name = new TableColumn<Product, String>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Product, String> quantity = new TableColumn<Product, String>("Quantity");
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
	    TableColumn<Product, String> unitSize = new TableColumn <Product, String>("Unit Size");
		unitSize.setCellValueFactory(new PropertyValueFactory<>("unitSize"));
		
		TableColumn<Product, String> price = new TableColumn<Product, String>("Price");
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		//set sizes
		name.setPrefWidth(275);
		unitSize.setPrefWidth(100);
		quantity.setPrefWidth(100);
		price.setPrefWidth(160);		
		
		//add columns to the table view
        table.getColumns().addAll(name, unitSize, quantity, price);	
		
		//return the table
		return table;		
	}
	
}
