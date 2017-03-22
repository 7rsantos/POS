package pos;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class CashOperations {

	private static Stage stage;
	private static TextField vendorField;
	private static ListView<String> reasons;
	private static ObservableList<String> items;
	private static ObservableList<String> filteredList;
	private static ObservableList<String> vendorItems;
	private static TextField searchField;
	private static NumericTextField amount;
	private static ListView<String> vendorList;
	
	/*
	 * Build and display the screen for cash withdrawals and deposits from register
	 */
	public static void displayCashWD(int caller)
	{
	   stage = new Stage();
	   
	   //label
	   Label reasonlbl = new Label("Reason");
	   Label noteslbl = new Label("Notes");
	   Label amountlbl = new Label("Amount");
	   
	   //setup labels
	   reasonlbl.setTextFill(Color.WHITE);
	   amountlbl.setTextFill(Color.WHITE);
	   noteslbl.setTextFill(Color.WHITE);
	   reasonlbl.setFont(new Font("Courier Sans", 14));
	   noteslbl.setFont(new Font("Courier Sans", 14));
	   amountlbl.setFont(new Font("Courier Sans", 14));

	   //text field
	   vendorField = new TextField();
	   amount = new NumericTextField();
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Apply.png"))));
	   Button cancel = new Button("Cancel", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Cancel.png"))));
	   Button vendor = new Button("Select Vendor", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/search2.png"))));
	   Button create = new Button("Create New Reason", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Create.png"))));
	   
	   //set on action
	   accept.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		   
		   Date date = new Date();	
		   String format2 = new SimpleDateFormat("yyyy-mm-dd").format(date);
		   
		   if(!Receipt.salesHistoryExists(format2))
		   {	
		       //create new sales history
		    	Receipt.createSalesHistory(date);
		   }
			
		   if(!reasons.getSelectionModel().getSelectedItem().isEmpty() && reasons.getSelectionModel().getSelectedItem() != null
			  && !vendorField.getText().isEmpty() && vendorField.getText() != null && amount.getText() != null
			  && !amount.getText().isEmpty())
		   {
			   if(Double.parseDouble(amount.getText()) > 0)
			   {
				   //close the stage
				   stage.close();
				   
				   //create cash withdrawal/deposit
				   if(caller == 1)
				   {
					  String ticketno = Receipt.createReceipt(date, Double.parseDouble(amount.getText()), "Cash Deposit", 0, "Completed"); 
					  createCashWD(reasons.getSelectionModel().getSelectedItem(), Double.parseDouble(amount.getText()), ticketno, "D", 1);      
					  
					  //increase cash
					  RegisterUtilities.increaseCash(Double.parseDouble(amount.getText()));
				   }
				   else
				   {
					  String ticketno = Receipt.createReceipt(date, Double.parseDouble(amount.getText()) * (-1), "Cash Withdrawal", 0, "Completed"); 
					  createCashWD(reasons.getSelectionModel().getSelectedItem(), Double.parseDouble(amount.getText()) * (-1), ticketno, "W", 2);					
					  
					  //decrease cash from the register
					  RegisterUtilities.increaseCash(Double.parseDouble(amount.getText()) * -1);
				   }	   
			   }	
			   else
			   {
				  AlertBox.display("FASS Nova - Error", "Please type in an amount greater than zero");   
			   }	   
		   }
		   else
		   {
			   AlertBox.display("FASS Nova - Error", "Please fill in all required fields");   
		   }	   
		}
		   
	   });
	   
	   cancel.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//close the stage
			stage.close();
			
			//go back to main screen
			Scene mainScreen = MainScreen.displayMainScreen(stage);
			stage.setScene(mainScreen);
			stage.show();
		}
		   
	   });
	   create.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   			
			//go to next screen
			createReason();
		}
		   
	   });	   
	   vendor.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {		
		
			//display vendor list
			displayVendorList();
		}
		   
	   });
	   
	   //text area
	   TextArea notes = new TextArea();
	   
	   //text field
	   vendorField = new TextField();
	   amount = new NumericTextField();
	   
	   //set maximum width
	   amount.setMaxWidth(155);
	   
	   //list view
	   reasons = new ListView<String>();
	   
	   //observable list
	   filteredList = FXCollections.observableArrayList();
	   items = getReasons();
	   
	   //add items to the list
	   reasons.setItems(items);
	   
	   //top layout
	   HBox top = new HBox();
	   top.setPadding(new Insets(10, 10, 0 ,10));
	   top.setAlignment(Pos.CENTER);
	   top.setSpacing(5);
	   
	   //add nodes to top
	   top.getChildren().addAll(vendor, vendorField);
	   
	   //left layout
	   VBox left = new VBox();
	   left.setAlignment(Pos.CENTER);
	   left.setSpacing(5);
	   left.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to left
	   left.getChildren().addAll(reasonlbl, reasons, create);
	   
	   //add listener to list
	   
	   //right layout
	   VBox right = new VBox();
	   right.setSpacing(5);
	   right.setPadding(new Insets(10, 10, 10, 10));
	   right.setAlignment(Pos.CENTER);
	   
	   //add nodes to right
	   right.getChildren().addAll(amountlbl, amount, noteslbl, notes);
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setSpacing(5);
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, accept);
	   
	   //root layout
	   BorderPane root = new BorderPane();
	   
	   //add nodes to root
	   root.setTop(top);
	   root.setRight(right);
	   root.setLeft(left);
	   root.setBottom(bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(CashOperations.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   if(caller == 1)
	   {	   
	      stage.setTitle("FASS Nova - Deposit Cash");
	   }
	   else
	   {
		  stage.setTitle("FASS Nova - Withdraw Cash");   
	   }	   
	   
	   stage.centerOnScreen();
	   stage.setMinWidth(280);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Get an observable list with reasons store in the database
	 */
	private static ObservableList<String> getReasons()
	{
	   ObservableList<String> result = FXCollections.observableArrayList();
	   String query = "Select Reason.Reason from Reason";
	   
	   try
	   {
		  java.sql.Connection conn = Session.openDatabase();  
		  PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
		  		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     result.add(rs.getString(1));	  
		  }	  
		  
		  //close the connection
		  conn.close();
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   
	   return result;
	}
	
	/*
	 * Add new reason to the database
	 */
	private static void addReason(String reason)
	{
	   String query = "Insert Ignore INTO Reason (Reason) VALUES(?)";
		   
	   try
	   {
		  java.sql.Connection conn = Session.openDatabase();  
		  PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
			  		
		  //set parameter
		  ps.setString(1, reason);	  
		  
		  //execute query
		  ps.execute();
		  
		  //close the connection
		  conn.close();
			  
		}
	    catch(Exception e)
		{
		   e.printStackTrace();   
		}		
	}	
	
	/*
	 * Add new vendor to the database
	 */
	private static void addVendor(String vendor, String phoneNumer, String manager, String address)
	{
	   String query = "CALL createVendor(?,?,?,?,?)";
	   
	   try
	   {
		   Connection conn = Session.openDatabase();
		   PreparedStatement ps = conn.prepareStatement(query);
		   
		   //set parameters
		   ps.setString(1, vendor);
		   ps.setString(2, phoneNumer);
		   ps.setString(3, manager);
		   ps.setString(4, address);
		   ps.setString(5, Configs.getProperty("StoreCode"));
		   
		   //execute query
		   ps.executeQuery();
		   
		   //close the connection
		   conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Display vendor information
	 */
	public static ObservableList<String> getVendors()
	{
	   ObservableList<String> result = FXCollections.observableArrayList();
	   String query = "CALL listVendors(?)";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, Configs.getProperty("StoreCode"));
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     result.add(rs.getString(1));  
		  }	  
		  
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return result;
	}
	
	/*
	 * Display create new vendor window
	 */
	public static void createVendor()
	{
	   //root layout
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);
		
		//stage
		Stage stage = new Stage();
		
		//label
		Label namelbl = new Label("Name");
		Label phonelbl = new Label("Phone");
		Label managerlbl = new Label("Manager");
		Label addresslbl = new Label("Address");
		
		//setup labels
		namelbl.setFont(new Font("Courier Sans", 12));
		namelbl.setTextFill(Color.WHITE);

		phonelbl.setFont(new Font("Courier Sans", 12));
		phonelbl.setTextFill(Color.WHITE);
		
		managerlbl.setFont(new Font("Courier Sans", 12));
		managerlbl.setTextFill(Color.WHITE);
		
		addresslbl.setFont(new Font("Courier Sans", 12));
		addresslbl.setTextFill(Color.WHITE);
		
		//text fields
		TextField name = new TextField();
		NumericTextField phone = new NumericTextField();
		TextField manager = new TextField();
		TextField address = new TextField();
		
		//buttons
		Button accept = new Button("Accept", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Apply.png"))));
		Button cancel = new Button("Go back", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Go back.png"))));
		
		//set on action
		cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   
			   //close the stage
			   stage.close();
			   
			   //go back to vendors list
			   displayVendorList();
			}
			
		});
		accept.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			   
				if(!name.getText().isEmpty()  && !phone.getText().isEmpty()  && !manager.getText().isEmpty()  &&
				    !address.getText().isEmpty())
				{	
				   //close the stage
				   stage.close();
				   
				   //create new vendor
				   addVendor(name.getText(), phone.getText(), manager.getText(), address.getText());
				   
				}
				else
				{
					AlertBox.display("FASS Nova - Error", "Please fill in all required fields");
				}	
				
			}
			
		});
		
		//top layout
		GridPane top = new GridPane();
		top.setVgap(7);
		top.setHgap(7);
		top.setAlignment(Pos.CENTER);
		
		//add nodes to top layout
		top.add(namelbl,0, 0);
		top.add(name, 1, 0);
		top.add(phonelbl, 0, 1);
		top.add(phone, 1, 1);
		top.add(managerlbl, 0, 2);
		top.add(manager, 1, 2);
		top.add(addresslbl, 0, 3);
		top.add(address, 1, 3);
		
		//bottom layout
		HBox bottom = new HBox();
		bottom.setSpacing(7);
		
		//add nodes to bottom
		bottom.getChildren().addAll(accept, cancel);
		
		//add nodes to root
		root.getChildren().addAll(top, bottom);
		
		//set id
		root.setId("border");
		
		//load style sheets
		root.getStylesheets().add(CashOperations.class.getResource("MainScreen.css").toExternalForm());
		
		//scene
		Scene scene = new Scene(root);
		
		//setup stage
		stage.centerOnScreen();
		stage.setMinWidth(300);
		stage.setTitle("FASS Nova - Add Vendor");
		
		//setup scene
		stage.setScene(scene);
		
		//show
		stage.show();
	}
	
	/*
	 * Display create new reason window
	 */
	public static void createReason()
	{
	   Stage stage = new Stage();
	   
	   //root layout
	   VBox root = new VBox();
	   
	   //label
	   Label reasonlbl = new Label("Reason");
	   reasonlbl.setFont(new Font("Courier Sans", 14));
	   reasonlbl.setTextFill(Color.WHITE);
	   
	   //text field
	   TextField reason = new TextField();
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Apply.png"))));
	   Button cancel = new Button("Go back", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Go back.png"))));
		
	   //top layout
	   HBox top = new HBox();
	   top.setSpacing(5);
	   top.setAlignment(Pos.CENTER);
	   
	   //add nodes to top
	   top.getChildren().addAll(reasonlbl, reason);
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setSpacing(7);
	   bottom.setAlignment(Pos.CENTER);
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, accept);
	   
	   //add nodes to root
	   root.getChildren().addAll(top, bottom);
	   
	   //set on action
	   cancel.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		   
		   //close the stage
		   stage.close();
			
		}
		   
	   });	
	   accept.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		
			if(!reason.getText().isEmpty())
			{
			   //add new reason to database
				addReason(reason.getText());
				
				//close the stage
				stage.close();
				
				//add items to observable list
				items = getReasons();
				
				//add items to the list
				reasons.setItems(items);
				
				//refresh table
				reasons.refresh();
				
			}	
			else
			{
			   AlertBox.display("FASS Nova - Error", "Please fill in all required fields");	
			}	
			
		}
		   
	   });
	   
	   
	   root.setAlignment(Pos.CENTER);
	   root.setSpacing(7);
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(CashOperations.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.centerOnScreen();
	   stage.setMinWidth(300);
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setTitle("FASS Nova - Create New Reason");
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.showAndWait();   
	}
	
	/*
	 * display list of vendors
	 * 
	 */
	private static void displayVendorList()
	{
	   Stage stage = new Stage();
	   
	   //label
	   Label vendorslbl = new Label("Vendors List");
	   
	   //setup labels
	   vendorslbl.setFont(new Font("Courier Sans", 20));
	   vendorslbl.setTextFill(Color.WHITE);
	   
	   //text field
	   searchField = new TextField();
	   
	   //button
	   Button select = new Button("Select", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Apply.png"))));
	   Button create = new Button("Create Vendor", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/Create.png"))));	   
	   Button search = new Button("Search", new ImageView(new Image(CashOperations.class.getResourceAsStream("/res/search2.png"))));
	   
	   //list view
	   vendorList = new ListView<String>();
	   vendorItems = getVendors();
	   
	   //set list view
	   vendorList.setItems(vendorItems);
	   
	   //set on action
	   select.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   //check if empty
		   if(vendorList.getSelectionModel().getSelectedItem() != null && !vendorList.getSelectionModel().getSelectedItem().isEmpty())
		   {
		      //close the stage
			   stage.close();
			   
			  //set the vendor
			  vendorField.setText(vendorList.getSelectionModel().getSelectedItem()); 
		   }	
		   else
		   {
			  AlertBox.display("FASS Nova", "Please select a vendor");   
		   }	    
		}
		   
	   });
	   
	   create.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			
			//close the stage
			stage.close();
			
			//go to create vendor screen
			createVendor();
		}
		   
	   });
	   
	   search.setOnAction(e -> updateFilteredData());
	   
	   //list for changes in master data
	   vendorList.getItems().addListener(new ListChangeListener<String>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c) {
			   
				//update list
				updateFilteredData();
			}		
	   });
	   
	   //top layout
	   HBox top = new HBox();
	   top.setSpacing(5);
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to top
	   top.getChildren().addAll(searchField, search);
	   
	   //center layout
	   VBox center = new VBox();
	   center.setAlignment(Pos.CENTER);
	   center.setSpacing(5);
	   center.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to center
	   center.getChildren().addAll(vendorslbl, vendorList);
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setSpacing(5);
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(create, select);
	   
	   //root layout
	   BorderPane root = new BorderPane();	   
	   root.setPadding(new Insets(10, 10, 10, 10));
	   
	   //setup root
	   root.setTop(top);
	   root.setCenter(center);
	   root.setBottom(bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //get style sheets
	   root.getStylesheets().add(CashOperations.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup stage
	   stage.centerOnScreen();
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(300);
	   stage.setTitle("FASS Nova - Vendors List");
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.showAndWait();
	}
	
	//update list when text field changes
	private static void updateFilteredData() {
	   
		//clear filter list
		filteredList.clear();
		
		//copy elements that match
		for(String v: vendorItems)
		{ 
		   if(matchesFilter(v))	
		   { 
			  filteredList.add(v);   
		   }	   
		}	
		
		//update table
		reapplyTableSortOrder();
	}

	
	/*
	 * Copy elements only if they match our filter
	 */
	private static boolean matchesFilter(String v) {
	
		String filter = searchField.getText();
		
		if(filter == null || filter.isEmpty())
		{ 
		   return true;	
		}		
		
		String lowerCaseFiltering = filter.toLowerCase();
		
		if(v.toLowerCase().indexOf(lowerCaseFiltering) != -1)
		{ 
		   return true;	
		}	
		
		//does not match
		return false;
	}
	
	/*
	 * Update table data once matches are found
	 */
	private static void reapplyTableSortOrder()
	{ 
		//update the table
	   vendorList.setItems(filteredList);	
	}
	
	/*
	 * Create a cash withdrawal or deposit
	 */
	private static void createCashWD(String reason, double total, String ticketNo, String c, int caller)
	{
	   String query = "CALL createcashWD(?,?,?,?,?)";	
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, reason);
		  ps.setDouble(2, total);
		  ps.setString(3, Session.getUserFirstName());
		  ps.setString(4, ticketNo);
		  ps.setString(5, c);
		  
		  //execute query
		  ps.execute();
		  
		  //close the connection
		  conn.close();
		  
		  //go to the next screen
		  displayTotal(caller, total);
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace(); 
	   }
	}
	
	/*
	 * Display amount to be refunded
	 * @param caller determines if its cash or card payment
	 */
	private static void displayTotal(int caller, double total)
	{
	   Stage stage = new Stage();
	   
	   //root layout
	   VBox root = new VBox();
	   
	   //layout to hold text fields and labels
	   Label ticketlbl = new Label("");
   
	   
	   //setup labels
	   ticketlbl.setFont(new Font("Courier Sans", 14));
	   ticketlbl.setTextFill(Color.WHITE);
	   
	   //text fields
	   NumericTextField ticket = new NumericTextField();
	   
	   ///set editable to false
	   ticket.setDisable(false);
	   
	   //set values
	   if(caller == 1)
	   {
		  ticketlbl.setText("Deposit"); 
		  ticket.setText(Double.toString(total));
		  ticket.setStyle("-fx-control-inner-background: #00ff00;");

	   }	
	   else
	   {
		  ticketlbl.setText("Withdraw"); 
		  ticket.setText("(" + Double.toString(total) + ")");
		  ticket.setStyle("-fx-control-inner-background: #ff0000;");

	   }	
	   
	   //change background color of text fields
	   //refund.setStyle("-fx-control-inner-background: #D3D3D3;");
	   
	   //grid pane
	   GridPane top = new GridPane();
	   
	   //set nodes
	   top.add(ticketlbl, 0, 0);
	   top.add(ticket, 1, 0);
	   
	   //setup top
	   top.setVgap(6);
	   top.setHgap(6);
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(Returns.class.getResourceAsStream("/res/Apply.png"))));
	   
	   //set on action
	   accept.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   
			//close the stage
			stage.close();
						
			//go to the next screen
		    PaymentScreen.backToMainScreen(stage, 2);
		}
		   
	   });	   
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.setSpacing(5);
	   
	   //add nodes
	   bottom.getChildren().addAll(accept);
	   
	   //add nodes to root
	   root.getChildren().addAll(top, bottom);
	   	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   if(caller == 1 || caller == 2)
	   {
		   //open the cash drawer
			Session.openCashDrawer();			
	   }
	   
	   //setup stage
	   stage.setTitle("FASS Nova");
	   stage.setMinWidth(350);
	   stage.centerOnScreen();
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show the stage
	   stage.show();
	}	
	
	/*
	 * Transfer cash
	 */
	public static void transferCashDisplay()
	{
	   stage = new Stage();

	   //label
	   Label storelbl = new Label("Select Store");
	   Label registerlbl = new Label("Select Register");
	   Label amountlbl = new Label("Select Amount");
	   
	   //setup labels
	   storelbl.setTextFill(Color.WHITE);
	   registerlbl.setTextFill(Color.WHITE);
	   amountlbl.setTextFill(Color.WHITE);
	   storelbl.setFont(new Font("Courier Sans", 14));
	   registerlbl.setFont(new Font("Courier Sans", 14));
	   amountlbl.setFont(new Font("Courier Sans", 14));
	   
	   //text field
	   NumericTextField amount = new NumericTextField();
	   
	   //hbox
	   GridPane top = new GridPane();
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   top.setVgap(7);
	   top.setHgap(5);
	     
	   //create choice box to display store locations
	   ChoiceBox<String> storeLocations = UserDisplay.getStoreCodes();
	   storeLocations.setValue(Configs.getProperty("StoreCode"));
	   
	   //create choice box to display register locations
	   ChoiceBox<String> registers = getRegisters(storeLocations.getSelectionModel().getSelectedItem());
	   
	   //add nodes to top
	   top.add(storelbl, 0, 0);
	   top.add(storeLocations, 1, 0);
	   top.add(registerlbl, 0, 1);
	   top.add(registers, 1, 1);
	   top.add(amountlbl, 0, 2);
	   top.add(amount, 1, 2);
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(Returns.class.getResourceAsStream("/res/Apply.png"))));
	   Button cancel = new Button("Cancel", new ImageView(new Image(Returns.class.getResourceAsStream("/res/Cancel.png"))));
	   
	   //set on action
	   cancel.setOnAction(e -> stage.close());
	   accept.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		   //check if all fields were filled
		   if(amount.getText() != null && !amount.getText().isEmpty() && storeLocations.getSelectionModel().getSelectedItem() != null
			  && !storeLocations.getSelectionModel().getSelectedItem().isEmpty() && registers.getSelectionModel().getSelectedItem() != null
			  && !registers.getSelectionModel().getSelectedItem().isEmpty())
		   {
			  if(Double.parseDouble(amount.getText()) > 0)
			  {
			     if(!Configs.getProperty("Register").equals(registers.getSelectionModel().getSelectedItem()))
			     {
			    	//decrease cash from this register
			    	RegisterUtilities.increaseCash(Double.parseDouble(amount.getText()) * -1);
				  
				    //increase cash from other register
			    	RegisterUtilities.transferCash(Double.parseDouble(amount.getText()), storeLocations.getSelectionModel().getSelectedItem());
			     } 
			     else
			     {
			        AlertBox.display("FASS Nova", "You cannot transfer cash to the same register");	 
			     }	 
			  }	  
			  else
			  {
			     AlertBox.display("FASS Nova", "Amount must be greater than zero");	  
			  }	  
		   }	
		   else
		   {
			  AlertBox.display("FASS Nova", "Please fill in all required fields");   
		   }	   
		}
		   
	   });
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setSpacing(7);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, accept);
	   
	   //root layout
	   VBox root = new VBox();
	   
	   //setup root
	   root.setAlignment(Pos.CENTER);
	   root.setSpacing(10);
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //add nodes to root
	   root.getChildren().addAll(top, bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(CashOperations.class.getResource("MainScreen.css").toExternalForm());
	   
	   stage.setTitle("FASS Nova - Transfer Cash");
	   stage.setMinWidth(350);
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.centerOnScreen();
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show the scene
	   stage.showAndWait();
	}
	
	/**
	 * Build a Choice Box that will contain the different locations
	 * of a store
	 * @return Choice Box with store codes
	 */
	public static ChoiceBox<String> getRegisters(String storeCode)
	{ 
		ChoiceBox<String> registers = new ChoiceBox<String>();
		String query = "CALL listRegisters(?)";
		
		try
		{ 
		   Connection conn = Session.openDatabase();
		   
		   //prepare statement
		   PreparedStatement ps = conn.prepareStatement(query);
		   
		   //set parameters
		   ps.setString(1, storeCode);
		   
		   //execute query
		   ResultSet rs = ps.executeQuery();
		   
		   while(rs.next())
		   { 
			  registers.getItems().add(rs.getString(1));  
		   }
		   
		   //close the connection
		   conn.close();
		}
		catch(Exception e)
		{ 
		   e.printStackTrace();
		}
		
		return registers;
	}
}
