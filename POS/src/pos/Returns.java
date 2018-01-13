package pos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collections;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Returns {

   private static TableView<Product> table;
   private static TableView<Product> refundTable; 
   private static ObservableList<Product> masterProducts;
   private static ObservableList<Product> refundProducts;
   private static NumericTextField ticketTotal;
   private static NumericTextField total;
   private static String ticket;
   private static String ticketNo;
   private static Logger logger = Logger.getLogger(Returns.class);
   
	/*
	 *  Display the search ticket window
	 */
	public static void displayTicketSearchScreen()
	{ 
	   //stage
		Stage stage = new Stage();
		
		//text field
		NumericTextField searchField = new NumericTextField();
		
		searchField.setPromptText("Example: 1.1.17.03.02.1");
		
		//label
		Label label = new Label("Ticket No");
		Label example = new Label("Example: 1.1.17.03.03.1");
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
				
				//close the main screen
				//MainScreen.closeStage();
				
				//search for the ticket
				searchTicket(searchField.getText());
				
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
		stage.show();
	}
	
	/*
	 * Search for sales ticket
	 */
	private static void searchTicket(String ticketno)
	{ 
	   ticketNo = ticketno;	
	   table = createProductsTable();
	   ObservableList<Product> products = FXCollections.observableArrayList();	
	   String query = "CALL listItems(?)";
	   
	   try
	   { 
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, ticketno);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  if(rs.next())
		  { 
			   do
			   { 
				   if(rs.getInt(3) == 1)
				   {	   
			          products.add(new Product(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));	     
				   }				   
				   else
				   {
					  int i = 0; 
					  while(i != rs.getInt(3))
					  { 
					     products.add(new Product(rs.getString(1), rs.getString(2), 1, rs.getDouble(4)));
					     i++;
					     
					  }		  
				   }	   
			   } while(rs.next());
			   
			   
			   //set product list
			   masterProducts = products;
			   
			   //set table items
			   table.setItems(masterProducts);
			   
			   //set ticket number
			   ticket = ticketno;
			   
			   //go to next screen
			   displayTicketContents();
			   
			   //close
			   rs.close();
			   ps.close();
			   conn.close();
		  }
		  else
		  { 
		     AlertBox.display("FASS Nova - Error ", "Could not find ticket");	  
		  }	  
	   }
	   catch(Exception e)
	   { 
		  AlertBox.display("FASS Nova", "Could not find product");   
		  logger.error("Error searching for sales ticket in the database", e);
	   }
	   
	}
	
	/*
	 * Display the ticket contents
	 */
	private static void displayTicketContents()
	{ 
	   Stage stage = new Stage();
	   
	   //close main screen
	   MainScreen.closeStage();
	   
	   //root layout
	   BorderPane root = new BorderPane();
	   
	   //  2 table view, accept, cancel, add item to refund list
	   refundProducts = FXCollections.observableArrayList();
	   refundTable = createProductsTable();
	   
	   //buttons
	   Button cancel = new Button("Cancel",new ImageView(new Image(Returns.class.getResourceAsStream("/res/Cancel.png"))));
	   Button next = new Button("Next ", new ImageView(new Image(Returns.class.getResourceAsStream("/res/Go forward.png"))));
	   Button add = new Button("", new ImageView(new Image(Returns.class.getResourceAsStream("/res/turnRight.png"))));
	   Button remove = new Button("", new ImageView(new Image(Returns.class.getResourceAsStream("/res/turnLeft.png"))));	   
	   
	   //disable buttons
	   next.setDisable(true);
	   
	   //set on action
	   cancel.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
			
			//close current stage
			stage.close();
			
			//set main screen scene
			Scene mainScreen = MainScreen.displayMainScreen(stage);
			stage.setScene(mainScreen);
			
			//show
			stage.show();
			
		}
		   
	   });
	   add.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) 
		{		
		   //add item to refund table
		   if(table.getSelectionModel().getSelectedItem() != null)
		   {
			  String name = table.getSelectionModel().getSelectedItem().getName();
			  double unitPrice = table.getSelectionModel().getSelectedItem().getUnitPrice();
			  int quantity = table.getSelectionModel().getSelectedItem().getQuantity();
			  
			  //create new product
			  Product p = new Product(name,"", quantity, unitPrice);
			  
			  //add to list
			  refundProducts.add(p);
			  
			  //set table items
			  refundTable.setItems(refundProducts);
			  
			  //delete from master list
			  masterProducts.remove(table.getSelectionModel().getSelectedItem());
			  
			  //re-compute totals
			  ticketTotal.setText(Double.toString(Receipt.setPrecision(computeTotal(masterProducts))));
			  total.setText(Double.toString(Receipt.setPrecision(computeTotal(refundProducts))));
			  
			  //refresh
			  refundTable.refresh();
			  table.refresh();
			  
			  //set disable
			  next.setDisable(false);
		   }	   		
		   else
		   { 
			  AlertBox.display("FASS Nova", "Please select a product");   
		   }	   
		}  
		   
	   });
	   remove.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) 
		{		
		   //add item to refund table
		   if(refundTable.getSelectionModel().getSelectedItem() != null)
		   {
			  String name = refundTable.getSelectionModel().getSelectedItem().getName();
			  double unitPrice = refundTable.getSelectionModel().getSelectedItem().getUnitPrice();
			  int quantity = refundTable.getSelectionModel().getSelectedItem().getQuantity();
			  
			  //create new product
			  Product p = new Product(name,"", quantity, unitPrice);
			  
			  //add to list
			  masterProducts.add(p);
			  
			  //set table items
			  table.setItems(masterProducts);
			  
			  //delete from refund list
			  refundProducts.remove(refundTable.getSelectionModel().getSelectedItem());
			  
			  //re-compute totals
			  ticketTotal.setText(Double.toString(Receipt.setPrecision(computeTotal(masterProducts))));
			  total.setText(Double.toString(Receipt.setPrecision(computeTotal(refundProducts))));
			  
			  //refresh
			  refundTable.refresh();
			  table.refresh();
			  
			  if(refundProducts.isEmpty())
			  {
			      next.setDisable(true);	  
			  }	  
		   }	   		
		   else
		   { 
			  AlertBox.display("FASS Nova", "Please select a product");   
		   }	   
		}  
		   
	   });	   
	   
	   next.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		    
			//close the stage
			stage.close();
			
			//save changes in the database
			modifyTicket(Double.parseDouble(total.getText()), Double.parseDouble(ticketTotal.getText()));
		}
		   
	   });
	   //labels
	   Label refundlbl = new Label("Items to be refunded");
	   Label itemslbl = new Label("Sales Ticket Items");
	   Label totallbl = new Label("Total Amount to be refunded $");
	   Label ticketlbl = new Label("Ticket total $");
	   
	   //setup labels
	   refundlbl.setTextFill(Color.WHITE);
	   itemslbl.setTextFill(Color.WHITE);
	   totallbl.setTextFill(Color.WHITE);
	   ticketlbl.setTextFill(Color.WHITE);
	   refundlbl.setFont(new Font("Courier Sans", 14));
	   totallbl.setFont(new Font("Courier Sans", 14));
	   ticketlbl.setFont(new Font("Courier Sans", 14));
	   itemslbl.setFont(new Font("Courier Sans", 14));
	   
		//text field
		total = new NumericTextField();
		total.setEditable(false);
		total.setText("0.00");
		
		ticketTotal = new NumericTextField();
		ticketTotal.setEditable(false);
		ticketTotal.setText(Double.toString(Receipt.setPrecision(computeTotal(masterProducts))));	   
		
	   //create right and left layout
	   VBox right = new VBox();
	   VBox left = new VBox();

	   //set right layout
	   right.setPadding(new Insets(10, 10, 10, 10));
	   right.setAlignment(Pos.CENTER);
	   right.setSpacing(5);
	   
	   //center layout
	   VBox center = new VBox();
	   center.setSpacing(5);
	   center.setAlignment(Pos.CENTER);
	   
	   //add nodes to center
	   center.getChildren().addAll(add, remove);
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setSpacing(5);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.setAlignment(Pos.BOTTOM_RIGHT);
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, next);
	   
	   //set left layout
	   left.setPadding(new Insets(10, 10, 10, 10));
	   left.setSpacing(5);
	   left.setAlignment(Pos.CENTER);
	   
	   //total layout
	   HBox totalLayout = new HBox();
	   totalLayout.setPadding(new Insets(5,5,5,5));
	   
	   //add nodes to total layout
	   totalLayout.getChildren().addAll(totallbl, total);
	   
	   //ticket total layut
	   HBox ticketTotalLayout = new HBox();
	   ticketTotalLayout.setPadding(new Insets(5, 5, 5, 5));
	   
	   //add nodes to ticket layout
	   ticketTotalLayout.getChildren().addAll(ticketlbl, ticketTotal);
	   
	   //set right and left layout
	   right.getChildren().addAll(refundlbl, refundTable, totalLayout);
	   left.getChildren().addAll(itemslbl, table, ticketTotalLayout);
	   
	   //set root
	   root.setCenter(center);
	   root.setRight(right);
	   root.setLeft(left);
	   root.setBottom(bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(Audit.class.getResource("MainScreen.css").toExternalForm());
	   
	   //create scene
	   Scene scene = new Scene(root);
	   stage.setScene(scene);
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Returns");
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(300);
	   stage.setResizable(false);
	   stage.centerOnScreen();
	   
	   //show the scene
	   stage.show();
	}
	
	/*
	 * Modify the sales ticket in the database
	 */
	protected static void modifyTicket(double refundTotal, double total) {
	
	   String status = "";
	   String query1 = "CALL updateTicketStatus(?,?)";
	   String query2 = "CALL updateTicketTotal(?,?)";
	   String query3 = "CALL updateExpectedCash(?,?)";
	   
	   if(masterProducts.isEmpty())
	   {
		  status = "Refunded";   
	   }
	   else
	   {
		  status = "Modified";    
	   }	   
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  
		  //update ticket status
		  java.sql.PreparedStatement ps = conn.prepareStatement(query1);
		  
		  //set parameters
		  ps.setString(1, ticket);
		  ps.setString(2, status);
		  
		  //execute update
		  ps.executeUpdate();
		  
		  //update ticket total
		  ps = conn.prepareStatement(query2);
		  
		  //add tax rate to total
		  total += total * (Double.parseDouble(Configs.getProperty("TaxRate"))/100);
		  
		  //set parameters
		  ps.setString(1, ticket);
		  ps.setDouble(2, total);
		  
		  //execute update
		  ps.executeUpdate();
		  
		  boolean isCash = isCashPayment();
		  
		  if(isCash)
		  {
			 //update the expected cash 
		     ps = conn.prepareStatement(query3);
		     
		     //set parameters
		     ps.setDouble(1, RegisterUtilities.getExpectedCash() - refundTotal);
		     ps.setString(2, Configs.getProperty("Register"));
		     
		     //execute query
		     ps.executeUpdate();
		     
		  }	  
		  
		  //close the connection
		  ps.close();
		  conn.close();
		  
		  //update items
		  deleteRefundedItems();
		  
		  //go to refund screen
          if(isCash)
          {	  
		     displayRefund(1);    
          }   
          else
          {
        	 displayRefund(2); 
          } 	  
	   }
	   catch(Exception e)
	   { 
		  logger.error("Error modifying sales ticket", e);  
	   }
	}

	@SuppressWarnings("unchecked")
	public static TableView<Product> createProductsTable()
	{ 
		TableView<Product> table = new TableView<Product>();
		
		//set editable to false
		table.setEditable(false);
		
		//create columns
		TableColumn<Product, String> name = new TableColumn<Product, String>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//TableColumn<Product, String> unitSize = new TableColumn<Product, String>("Unit Size");
		//unitSize.setCellValueFactory(new PropertyValueFactory<>("unitSize"));
		
		TableColumn<Product, String> quantity = new TableColumn<Product, String>("Quantity");
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		TableColumn<Product, String> unitPrice = new TableColumn<Product, String>("Unit Price");
		unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		
		TableColumn<Product, String> price = new TableColumn<Product, String>("Price");
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		//set sizes
		name.setPrefWidth(200);
		//unitSize.setPrefWidth(75);		
		quantity.setPrefWidth(75);
		unitPrice.setPrefWidth(100);
		price.setPrefWidth(100);
		
		//set table height
		table.setMaxHeight(290);
		
		//add columns to the table view
        table.getColumns().addAll(name, quantity, unitPrice, price);			
		
		
		return table;
	}
	
	/*
	 * Compute total
	 * 
	 */
	private static double computeTotal(ObservableList<Product> products)
	{ 
	   double result = 0.0;
	   
	   for(Product p : products)
	   { 
		  result += p.getPrice();   
	   }
	   
	   return result;
	}
	
	/*
	 * Determine if ticket was paid with cash
	 */
	private static boolean isCashPayment()
	{
	   boolean isCash = false;
	   String query = "SELECT paymentMethod FROM salesTicket WHERE salesTicket.ticketno = ?";
	   
	   try
	   {
		  Connection conn  = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameter
		  ps.setString(1, ticket);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //process
		  while(rs.next())
		  {
		     if(rs.getString(1).equals("Cash Received"))
		     {
		        isCash = true;	 
		     } 	 
		  }
		  
		  //close
		  rs.close();
		  ps.close();
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  logger.error("Error verifying if cash payment", e);   
	   }
	   
	   return isCash;
	}
	
	/*
	 * Delete refunded items from a sales ticket and update quantity of products
	 */
	private static void deleteRefundedItems()
	{
	   String query1 = "CALL deleteItem(?,?)";
	   String query2 = "CALL updateItemQuantity(?, ?, ?)";
	   	   	   
	   try
	   {
		   Connection conn = Session.openDatabase();
		   PreparedStatement ps = conn.prepareStatement(query1);
		   PreparedStatement pst = conn.prepareStatement(query2);
		   
		   //determine the items to be deleted
		   for(Product p : refundProducts)
		   {
			   if(!masterProducts.contains(p.getName()))
			   {
				  //set parameters
				  ps.setString(1, ticket);
				  ps.setString(2, p.getName());
				  
				  //execute query
				  ps.executeQuery();
				  
				  //clear parameters
				  ps.clearParameters();
			   }
			   else
			   {
				  if(!masterProducts.isEmpty())
				  {
				     
				     //set parameters
				     pst.setString(1, ticket);
				     pst.setString(2, p.getName());
				     pst.setInt(3, Collections.frequency(masterProducts, p.getName()));
				     
				     //execute query
				     pst.executeQuery();
				     
				     //clear parameters
				     pst.clearParameters();
				  }		  
			   }	   
		   }
		   
		   //close the connection
		   ps.close();
		   pst.close();
		   conn.close();
	   }
	   catch(Exception e)
	   {
		  logger.error("Error deleteing refunded items", e);   
	   }
	}
	
	/*
	 * Display amount to be refunded
	 * @param caller determines if its cash or card payment
	 */
	private static void displayRefund(int caller)
	{
	   Stage stage = new Stage();
	   
	   //root layout
	   VBox root = new VBox();
	   
	   //layout to hold text fields and labels
	   Label ticketlbl = new Label("Ticket Total");
	   Label refundlbl = new Label("Amount to be refunded");
	   
	   //setup labels
	   ticketlbl.setFont(new Font("Courier Sans", 14));
	   refundlbl.setFont(new Font("Courier Sans", 14));
	   ticketlbl.setTextFill(Color.WHITE);
	   refundlbl.setTextFill(Color.WHITE);
	   
	   //text fields
	   NumericTextField ticket = new NumericTextField();
	   NumericTextField refund = new NumericTextField();
	   
	   ///set editable to false
	   ticket.setDisable(false);
	   refund.setDisable(false);
	   
	   //set values
	   ticket.setText(ticketTotal.getText());
	   refund.setText(total.getText());
	   
	   //change background color of text fields
	   refund.setStyle("-fx-control-inner-background: #D3D3D3;");
	   ticket.setStyle("-fx-control-inner-background: #00ff00;");
	   
	   //grid pane
	   GridPane top = new GridPane();
	   
	   //set nodes
	   top.add(ticketlbl, 0, 0);
	   top.add(ticket, 1, 0);
	   top.add(refundlbl, 0, 1);
	   top.add(refund, 1, 1);
	   
	   //setup top
	   top.setVgap(6);
	   top.setHgap(6);
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(Returns.class.getResourceAsStream("/res/Apply.png"))));
	   Button print = new Button("Accept & Print", new ImageView(new Image(Returns.class.getResourceAsStream("/res/Print.png"))));
	   
	   //set on action
	   accept.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//close the stage
			stage.close();
			
			if(caller == 1)
			{
			   //open the cash drawer
				Session.openCashDrawer();
			}	
			
			//go to the next screen
		    PaymentScreen.backToMainScreen(stage, 2);
		}
		   
	   });
	   
	   print.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//close the stage
			stage.close();	
			
			//print the receipt		
			double subtotalAmount = Double.parseDouble(total.getText());
			double refundAmount = Receipt.setPrecision(subtotalAmount + (Double.parseDouble(Configs.getProperty("TaxRate"))/100) * subtotalAmount);
			Receipt.printRefundReceipt(ticketNo, refundProducts, Double.toString(refundAmount), Double.toString(subtotalAmount));
			
			//go to the next screen
		    Scene mainScreen = MainScreen.displayMainScreen(stage);
		    stage.setScene(mainScreen);
			stage.show();
		}
		   
	   });	   
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.setSpacing(5);
	   
	   //add nodes
	   bottom.getChildren().addAll(accept, print);
	   
	   //add nodes to root
	   root.getChildren().addAll(top, bottom);
	   	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   if(caller == 1)
	   {
		   //open the cash drawer
			Session.openCashDrawer();
			
		   //print the receipt	
	   }
	   
	   //setup stage
	   stage.setTitle("FASS Nova");
	   stage.setMinWidth(350);
	   stage.setResizable(false);
	   stage.centerOnScreen();
	   
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show the stage
	   stage.show();
	}
	
}
