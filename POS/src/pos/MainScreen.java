package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MainScreen {
	
	static Stage window;
	public static TableView<Product> table;
	public static ImageView carnetIcon;
    //private static Button checkCashing;
    private static Button customers;
    private static Button moneyWire;
	private static Button search;
	private static Button phoneOptions;
	
	static Label customer;
	private static boolean isFoodStamp =false; 
	private static String barcode = "";
	private static boolean active = false;
	private static Button food;
	
	public static ObservableList<Product> products = FXCollections.observableArrayList();
	public static ArrayList<String> productList = new ArrayList<String>();
	
	private static Button pay;
	private static Button remove;
	public static TextField subTotal;
	public static TextField Total;	
	public static TextField Tax;
	public static TextField Discount;
	public static double discount;
	public static String status;
	public static String ticketNo;
	private static Logger logger = Logger.getLogger(MainScreen.class);
	
	/*
	 * get total of receipt
	 */
	public static double getTotal()
	{ 
		if(Total.getText() != null && !Total.getText().isEmpty())
		{	
	       return Receipt.setPrecision(Double.parseDouble(Total.getText()));
		}
		
		return 0;
	}
	
	public static Scene displayMainScreen(Stage stage)
	{ 
		
		setWindowSize(stage);
		
		Scene scene = buildMainScreen(stage);
				
		return scene;
	}
	
	public static Stage setWindowSize(Stage stage)
	{ 
		window = stage;
		
		//set the size of the window
		Screen screen = Screen.getPrimary(); 
		Rectangle2D bounds = screen.getVisualBounds(); 

		window.setX(bounds.getMinX()); 
		window.setY(bounds.getMinY()); 
		window.setMinHeight(bounds.getHeight()); 
		window.setMinWidth(bounds.getWidth());
        
        //set logo
		window.getIcons().add(new Image(MainScreen.class.getResourceAsStream("/res/FASSlogo.jpg")));

        
        window.setTitle("FASS Nova - Main Screen");
        
        
        return window;
	}
	

	@SuppressWarnings("unchecked")
	public static Scene buildMainScreen(Stage stage)
	{ 						
		
        //create a new border pane
		BorderPane border = new BorderPane();
		
		//set the status to Incomplete
		status = "Incomplete";
		ticketNo = "";
		
		//create left border pane
		BorderPane left = new BorderPane();

		
		left.setPadding(new Insets(10, 20, 20, 0));
		border.setLeft(left);
		
		//center
		FlowPane center = new FlowPane();
		
		//set size
		center.setPrefSize(300, 300);
		
		//create table
		table = createTable();
		
		table.prefHeightProperty().bind(center.heightProperty().subtract(50));
        table.prefWidthProperty().bind(center.widthProperty().subtract(100));
        
        //setup center
		center.setHgap(5);
		center.setVgap(5);
		
		center.setAlignment(Pos.CENTER);
		center.setPadding(new Insets(10, 10, 10, 10));
		
		center.getChildren().add(table);
		
		//create menu bar and added to the border layout
		MenuBar menuBar = CustomMenu.createMenu();
	    menuBar.prefWidthProperty().bind(stage.widthProperty());
		
		//create options section
		GridPane services = createOptionSection();
		
		//create a Vbox layout
		VBox top = new VBox();
		top.getChildren().addAll(menuBar, services);
		
		//add vbox to the top of border
		border.setTop(top);
		
		//create a box and add it to root
		GridPane totals = createSouthArea();
		
		
		//bottom.setPadding(new Insets(0, 5, 5, 5));
		VBox date = DateBox.createDateBox();
		//bottom.setAlignment(Pos.BOTTOM_CENTER);
		
		HBox bottom = new HBox();
		
		//get buttons on the bottom right
		FlowPane bottomRight = getButtons();
		
		bottom.getChildren().addAll(date, totals, bottomRight);
	
		
		//set center
		border.setCenter(center);
		
		//add box to border
		border.setBottom(bottom);
		//border.setRight(bottomRight);
		
		//create a tool bar for the actions
		ToolBar actions = ActionsTable.createActionsTable();
		VBox actionsLayout = new VBox();
		
		//add actions tool bar to the layout
		actionsLayout.getChildren().add(actions);
		
		//set action to the left of the border layout
		border.setLeft(actions);
		
		//set ids
		border.setId("border");
		//root.setId("root");
		bottom.setId("box");
		actions.setId("actions");
		left.setId("id");
        
		//Create the scene
		Scene scene = new Scene(border);
		
		//setup key listener for the scene
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			
			@Override
			public void handle(KeyEvent event) {
				  
			   activateTimer();
			   switch(event.getText())
			   { 
			      case "0":
			    	  barcode = barcode + 0;
			    	  break;
			      case "1":
			    	  barcode = barcode + 1;
			    	  break;
			      case "2":
			    	  barcode = barcode + 2;
			    	  break;
			      case "3":
			    	  barcode = barcode + 3;
			    	  break;
			      case "4":
			    	  barcode = barcode + 4;
			    	  break;
			      case "5":
			    	  barcode = barcode + 5;
			    	  break;
			      case "6":
			    	  barcode = barcode + 6;
			    	  break;
			      case "7":
			    	  barcode = barcode + 7;
			    	  break;
			      case "8":
			    	  barcode = barcode + 8;
			    	  break;	
			      case "9":
			    	  barcode = barcode + 9;
			    	  break;			    	  
			      default:
			    	  event.consume();
			    	  break;
			   }
			}	
		});
		
		//get the resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
		
		return scene;
	}

	private static void activateTimer() 
	{			
		if(!active)
		{ 
			KeyFrame key = new KeyFrame(
					   Duration.millis(250),
					   ae -> processBarcode());	
		
			//setup timer to search for barcode after 3 seconds
		   Timeline timeline = new Timeline(key);
		   timeline.play();	
		   active = true;
		}			
		
	}

	private static void processBarcode() {
	
	   String query = "CALL searchProduct(?)";
	   int quantity= 1;
	   String name = " ", unitSize = " ";
	   double unitPrice = 0.00;
	   	   
	   try
	   {		   
		   //create the connection
		   Connection conn = Session.openDatabase();
		   
		   //create prepared statement
		   PreparedStatement ps = conn.prepareCall(query);
		   
		   //set parameter
		   ps.setString(1, barcode);
		   
		   //execute the query
		   ResultSet rs = ps.executeQuery();
		   
		   if(!rs.next())
		   {	   
              AlertBox.displayDialog("FASS Nova - Error", "Could not find product");
		   }
		   else
		   { 
			  //process the result set
			  do
			  { 
			     name = rs.getString(1);
			     System.out.println("the name of the product: " + rs.getString(1));
			     unitSize = rs.getString(2);
			     unitPrice = Double.parseDouble(rs.getString(3));
			     
			  }	while(rs.next());
			  
			  if(!productList.contains(name))
			  {	  
				 //add name to product list
				 productList.add(name);
				  
			     //add items to products observable list
			     products.add(new Product(name, unitSize, quantity, unitPrice));	   
				   
			     //put items in the table
			     table.setItems(products);
			     
			     //activate food stamp
			     food.setDisable(false);
			     
			     //activate pay button
			     pay.setDisable(false);
			     
			     //activate remove item button
			     remove.setDisable(false);
			     
			  }   
			  else
			  { 				  
				 //get product index
				 int index = productList.indexOf(name);
				 
				 //get product quantity
				 int productQuantity = table.getItems().get(index).getQuantity();
				 
				 //update product quantity
                 table.getItems().get(index).setQuantity(productQuantity + 1 );
                 
                 //load table
                 table.refresh();
			  }	  
				  
		   }	   
		   
		   //reset the value of barcode
		   barcode = "";
		   
		   //deactivate the timer
		   active = false;
		   
		   //create decimal format
		   DecimalFormat df = new DecimalFormat("#.##");
		   
		   //compute subtotal 
		   subTotal.setText(df.format(Product.computeSubTotal(products, Discount.getText())));
		   
		   //compute total
		   Total.setText(df.format(Product.computeTotal(subTotal.getText(), Tax.getText())));
	   }
	   catch(Exception e)
	   { 
		   e.printStackTrace();
		   
		   AlertBox.display("FASS Nova - Error", "Could not connect to the database");
	   }	   

	}

	public static GridPane createSouthArea()
	{ 
		//create box labels
		Label subtotal = new Label("Subtotal");
		Label tax = new Label("Tax");
		Label total =new Label("Total");
		Label discount = new Label("Discount");
		
		//set label colors
		subtotal.setTextFill(Color.WHITE);
		tax.setTextFill(Color.WHITE);
		total.setTextFill(Color.WHITE);
		discount.setTextFill(Color.WHITE);
		
		//create text fields and set editable to false
		Discount = new TextField();
		Discount.setEditable(false);
		
		subTotal = new TextField();
		subTotal.setEditable(false);
		subTotal.setText("0.00");
		
		Tax = new TextField();
		Tax.setEditable(false);
		
		Total = new TextField();
		Total.setEditable(false);
		
		Image removeIcon = new Image(MainScreen.class.getResourceAsStream("/res/Erase.png"));				
		remove = new Button("Remove Item", new ImageView(removeIcon));
		remove.setDisable(true);
		
		//implement actions
		//cancel.setOnAction(e -> AlertBox.displayOptionDialog("FASS Nova", "Do you want to cancel ticket?"));
	    remove.setOnAction(e -> deleteItem());
	    //pay.setOnAction(e -> pay());
		
		//change size of pay button
		//pay.setMaxWidth(75);
		
		//create grid pane and set vgaps and hgaps 
		GridPane grid = new GridPane();
		grid.setVgap(2);
		grid.setHgap(12);
		grid.setPadding(new Insets(20, 50, 20, 0));
		grid.setAlignment(Pos.BOTTOM_CENTER);
		
		//add nodes to the grid pane
		//grid.add(date, 0, 1);
		grid.add(remove, 4, 1);
		//grid.add(pay, 13, 1);
		//grid.add(cancel, 12, 1);
		grid.add(discount, 6, 0);
		grid.add(Discount, 7, 0);
		grid.add(subtotal, 6, 1);
		grid.add(subTotal, 7, 1);
		grid.add(tax, 9, 0);
		grid.add(Tax, 10, 0);
		grid.add(total, 9, 1);
		grid.add(Total, 10, 1);
		
		Tax.setText(Configs.getProperty("TaxRate") + "%");
		Discount.setText("0.00%");
		
		return grid;
	}
	
	/*
	 * Build bottom right
	 * 
	 */
	public static FlowPane getButtons()
	{
	   FlowPane buttons = new FlowPane();
	   
	   buttons.setAlignment(Pos.BOTTOM_RIGHT);
	   
	   //set layout
	   buttons.setHgap(5);
	   buttons.setPadding(new Insets(20, 0, 20, 5));
	   
		//create pay, cancel, and remove buttons and set their icons
		Image payIcon = new Image(MainScreen.class.getResourceAsStream("/res/Buy.png"));
		pay = new Button("Pay", new ImageView(payIcon));
		pay.setDisable(true);
		
		Image cancelIcon = new Image(MainScreen.class.getResourceAsStream("/res/Cancel.png"));		
		Button cancel = new Button("Cancel", new ImageView(cancelIcon));
		
		//fooddstamps
		food = new Button("Food stamps",new ImageView(new Image(MainScreen.class.getResourceAsStream("/res/ebt.png"))));
		
		//set disbake
		food.setDisable(true);
		
		//implement actions
		cancel.setOnAction(e -> AlertBox.displayOptionDialog("FASS Nova", "Do you want to cancel ticket?"));
	    pay.setOnAction(e -> pay());
	    food.setOnAction(e -> calculateFoodStamps());
	    
	    //add children
	    buttons.getChildren().addAll(food, cancel, pay);
		
		return buttons;
	   
	   
	}
	
	/*
	 * Calculate new total
	 */
	private static void calculateFoodStamps() {
	
		if(!isFoodStamp)
		{	
		   //calculate new total
		   Total.setText(Double.toString(Receipt.setPrecision((Product.computeTotal(subTotal.getText(), null)))));
		   
		   //set to true
		   isFoodStamp = true;		   
		   
		}
		else
		{
		   //calculate total
		   Product.computeTotal(subTotal.getText(), Configs.getProperty("taxRate"));
		   
		   //set to false
		   isFoodStamp = false;
		}	
	}

	public static GridPane createOptionSection()
	{ 
		//layout for options section
		GridPane options = new GridPane();
		
		//create icons
		search = Icon.createButtonIcon("Product Search", "/res/search.png");
	    moneyWire = Icon.createButtonIcon("Money Wire", "/res/moneyWire.png");
		//checkCashing = Icon.createButtonIcon("Check Cashing", "/res/check.PNG");
		customers = Icon.createButtonIcon("Customer", "/res/customer.png");
		phoneOptions = Icon.createButtonIcon("Recargas/Tarjetas", "/res/android.png");
		
		//create user icon
		carnetIcon = new ImageView();
		carnetIcon = Session.getUserPicture();
		
		//add context menu
		 ContextMenu contextMenu = new ContextMenu();
		 
	     MenuItem item1 = new MenuItem("Logout");
	     item1.setOnAction(e -> Session.logout(window));
		 carnetIcon.setOnContextMenuRequested(
		    e -> contextMenu.show(carnetIcon, e.getScreenX(), e.getScreenY()+10));
		
		 //implement actions
		 customers.setOnAction(e -> Customers.displayCustomerList(window, products, 1));
		 search.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				
			   //close the current stage
			   window.close();
			   
			   //display the product list
			   ProductList.displayProductList(table.getItems(), customer.getText(), productList);
				
			} 
			 
		 });
		 moneyWire.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
		
			   //go to next screen
			   MoneyWire.displaySendReceiveScreen();	
			}
			 
		 });
		 
		 //display other services
		 phoneOptions.setOnAction(e-> MainScreen.displayOtherServices());
		 
	     //add context menu to the image
	     contextMenu.getItems().add(item1);
	     
		//create cashier info section
		Label cashier = new Label("Cashier:");
		Label cashierName = new Label(Session.getUserFirstName());		
		
		customer = new Label("Customer");
		//Label check = new Label("Check Cashing");
		Label productSearch = new Label("Product Search");
		Label money = new Label("Money Wire");
		Label optionslbl = new Label("Other services");
		
		//change label color
		customer.setTextFill(Color.WHITE);
		//check.setTextFill(Color.WHITE);
		productSearch.setTextFill(Color.WHITE);
		money.setTextFill(Color.WHITE);
		cashier.setTextFill(Color.WHITE);
		cashierName.setTextFill(Color.WHITE);
		optionslbl.setTextFill(Color.WHITE);
		
		
		//add nodes to the grid pane
		options.add(customers, 14, 1);
		options.add(customer, 14, 2);
		options.add(cashier, 26, 1);
		options.add(cashierName, 27, 1);
		options.add(carnetIcon, 30, 1);
		options.add(search, 18, 1);
		options.add(productSearch, 18, 2);
		options.add(moneyWire, 20, 1);
		options.add(money, 20, 2);
		options.add(phoneOptions, 22, 1);
		options.add(optionslbl, 22, 2);	
	
        options.setHgap(15); 
		options.setAlignment(Pos.CENTER);
		options.setPadding(new Insets(8, 30, 3, 10));
		
		return options;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static TableView createTable()
	{ 
		//create table view
		table = new TableView<>();
		table.setEditable(false);
		
		//create columns
		TableColumn<Product, String> Name = new TableColumn<Product, String>("Name");
		Name.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Product, String> unitSize = new TableColumn<Product, String>("Unit Size");
		unitSize.setCellValueFactory(new PropertyValueFactory<>("unitSize"));
		
	    TableColumn<Product, String> quantity = new TableColumn <Product, String>("Quantity");
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		TableColumn<Product, String> salesPrice = new TableColumn<Product, String>("Unit Price");
		salesPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		
		TableColumn<Product, String> price = new TableColumn<Product, String>("Price");
		price.setCellValueFactory(new PropertyValueFactory<>("price"));	
		
		//add context menu
		ContextMenu contextMenu = new ContextMenu();	 
	    MenuItem quantityMenu = new MenuItem("Update Quantity");	
	    
	    quantityMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(table.getSelectionModel().getSelectedItem() != null && !table.getSelectionModel().isEmpty())	
			   {
                  MainScreen.displayQuantity(table.getSelectionModel().getSelectedItem().getQuantity());
			   } 	   
			}
	    	
	    });
	    contextMenu.getItems().add(quantityMenu);
	     
		//add listener to item
		table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(ContextMenuEvent event) {
			   contextMenu.show(table, event.getScreenX()+2, event.getScreenY()+2);	
			}
			
		});
		
		//set sizes
		Name.setPrefWidth(600);
		unitSize.setPrefWidth(100);
		quantity.setPrefWidth(100);
		salesPrice.setPrefWidth(150);
		price.setPrefWidth(220);
		
		
		//add columns to the table view
        table.getColumns().addAll(Name, unitSize, quantity, salesPrice, price);		
		
		return table;
	}
	
	/*
	 * Delete item from table view
	 */
    public static void deleteItem()
    { 
        @SuppressWarnings("unused")
		ObservableList<Product> productSelected, allProducts = FXCollections.observableArrayList();
        
        //get items
        allProducts = table.getItems();
        
        //get selection model
        productSelected = table.getSelectionModel().getSelectedItems();
        
        if(!products.isEmpty())
        {	
           //get name of product
           String name = table.getSelectionModel().getSelectedItem().getName();
        
           //remove all instances from observable list
           productSelected.forEach(products::remove);
        
           //remove instance from products list
           productList.remove(name);  
        
           //create double format
           DecimalFormat df = new DecimalFormat("#.##");
        
           //recompute the subtotal
           subTotal.setText(df.format(Product.computeSubTotal(products, Discount.getText())));
        
           //recompute the total
           Total.setText(df.format(Product.computeTotal(subTotal.getText(), Tax.getText())));
        }
        if(products.isEmpty())
        {	
           //disable buttons
           pay.setDisable(true);
           remove.setDisable(true);
           food.setDisable(true);
        }
    }
    
    public static void cancelTicket(boolean performAction)
    { 
       if(performAction)
       { 
    	   //clear observable list
    	   products.clear();
    	   
    	   //clear products list
    	   productList.clear();
    	   
    	   //recompute subtotal
    	   subTotal.setText("0.00");
    	   
    	   //recompute total
    	   Total.setText("0.00");
    	   
    	   //log
    	   logger.info("About to logout");
    	   
    	   //logout 
    	   Session.logout(window);   
       }	   
    }
    
    /*
     * Go to the payment screen
     */
    public static void pay()
    { 
       //get the items from the table	
       ObservableList<Product> allProducts = table.getItems();
       
       //get discount
       discount = 0;
       if(!Discount.getText().isEmpty() && Discount.getText() != null)
       { 
          discount = Double.parseDouble(Discount.getText().substring(0, Discount.getText().length()-1));
       }	   	   
       
       //set the customer
       if(customer.getText().equals("Customer"))
       {
    	  //set id to 0 
           Configs.saveTempValue("customerID", "0");	  
           //Configs.saveTempValue("customerName", null);
       }	   
       
       //close the window display payment screen
       window.close();
       Scene scene = PaymentScreen.displayPaymentScreen(allProducts, Double.parseDouble(Total.getText()), window, discount, status, ticketNo);
       window.setScene(scene);
       window.show();
    }
    
    //set table items
    public static void setTableItems(ObservableList<Product> allProducts)
    { 
       
       //copy items of all products to products
       products = allProducts;
       
       table.setItems(products); 
       
       //populate product list array
       for(Product p : products)
       { 
            productList.add(p.getName());	   
       }	   
       
	   //create decimal format
	   DecimalFormat df = new DecimalFormat("#.##");
	   
	   //compute subtotal 
	   subTotal.setText(df.format(Product.computeSubTotal(products, Discount.getText())));
	   
	   //compute total
	   Total.setText(df.format(Product.computeTotal(subTotal.getText(), Tax.getText())));
	   
	   //enable pay and remove buttons
	   pay.setDisable(false);
	   remove.setDisable(false);
	   food.setDisable(false);
    }
    
    
    /*
     *  Calculate subtotal and total for non tax itemas
     */
    public static void setTableItemsforNonTaxItems(ObservableList<Product> allProducts)
    { 
       table.setItems(allProducts); 
       
       //copy items of all products to products
       products = allProducts;
       
       //populate product list array
       for(Product p : products)
       { 
            productList.add(p.getName());	   
       }	   
       
	   //create decimal format
	   //DecimalFormat df = new DecimalFormat("#.##");
	   
	   //compute subtotal 
	   //subTotal.setText(df.format(Product.computeSubTotal(products, Discount.getText())));
	   
	   //compute total
	   //Total.setText(df.format(Product.computeTotal(subTotal.getText(), null)));
	   
	   //enable pay and remove buttons
	   pay.setDisable(false);
	   remove.setDisable(false);
	   //food.setDisable();
    }
    
    
    /*
     * Reset the observable list containing the list of products
     */
    public static void resetProductList()
    { 
       productList.clear();
       products.clear();
       
       //set to false
       isFoodStamp = false;
       food.setDisable(true);
       
       //set status to incomplete
       status = "Incomplete";
       
       //set totals equal to zero
       subTotal.setText("0.00");
       Total.setText("0.00");
       Discount.setText("");
       
       //disable buttons
       remove.setDisable(true);
       pay.setDisable(true);
    }
    
    /*
     * Close the stage
     */
    public static void closeStage()
    { 
       window.close();	
    }
    
    /*
     * Add item to the list
     */
    public static void addItems(String name)
    { 

    	   String query = "CALL searchProductName(?,?)";
    	   String unitSize = "";
    	   double unitPrice = 0.0;
    	   
    	   try
    	   { 
    	 	  //create the connection
    		  Connection conn = Session.openDatabase();
    		   
    		  //create prepared statement
    		  PreparedStatement ps = conn.prepareCall(query);
    		   
    		  //set parameter
    		  ps.setString(1, name);
    		  ps.setString(2, Configs.getProperty("StoreCode")); 
    		  
    		  //execute the query
    		  ResultSet rs = ps.executeQuery();
    		  
    		  //process the result set
    		  while(rs.next())
    		  {	 
			     unitSize = rs.getString(1);
			     unitPrice = Double.parseDouble(rs.getString(2));  		     
    		  }    
    		  
			  //add name to product list
			  productList.add(name);
				  
			  //add items to products observable list
			  products.add(new Product(name, unitSize, 1, unitPrice));	   
				   
			  //put items in the table
			  table.setItems(products);
    	   }
    	   catch(Exception e)
    	   { 
    		  e.printStackTrace();   
    	   }
    	
    	
		//create decimal format
		DecimalFormat df = new DecimalFormat("#.##");
		   
		//compute sub total 
		subTotal.setText(df.format(Product.computeSubTotal(products, Discount.getText())));
		
		
		//compute total
		double total = Product.computeTotal(subTotal.getText(), Tax.getText());  	
;
		for(Product p : products)
		{
		   if(p.getUnitSize().equals("0"))
		   {
		      total += p.getUnitPrice();   
		   }	   
		}
		
		//set total
		Total.setText(Double.toString(Receipt.setPrecision(total)));
		
		
		
		//enable buttons
		food.setDisable(false);
		pay.setDisable(false);
		remove.setDisable(false);
    }
    
    /*
     * Get products list
     */
    public static ObservableList<Product> getProductList()
    { 
       return products;	
    }
    
    /*
     * Display quantity screen
     */
    private static void displayQuantity(int quantity)
    {
       //stage
       Stage stage = new Stage();
       
       //text field
       NumericTextField number = new NumericTextField();
       
       //label
       Label title = new Label("Set quantity for this item");
       title.setTextFill(Color.WHITE);
       
       //button
       Button update = new Button("Update");
       
       //set on action
       update.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		   
			if(number.getText() != null && !number.getText().isEmpty())
			{	
			   //set product quantity
			   setProductQuantity(Integer.parseInt(number.getText()));
			   
			   //close the stage
			   stage.close();
			}
			else
			{
			   AlertBox.display("FASS Nova", "Please fill in required fields");	
			}	
		}
      	   
       });
       
       //bottom layout
       HBox bottom = new HBox();
       bottom.setSpacing(7);
       bottom.setAlignment(Pos.CENTER);
       
       //add nodes to bottom
       bottom.getChildren().addAll(number, update);
       
       //root 
       VBox root = new VBox();
       
       //add nodes to root
       root.getChildren().addAll(title, bottom);
       
       
       //setup root
       root.setSpacing(7);
       root.setAlignment(Pos.CENTER);
       root.setPadding(new Insets(20, 20, 20, 20));
       //set it
       root.setId("border");
       
       //style sheets
       root.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
       
       //setup stage
       stage.setTitle("FASS Nova - Quantity");
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.setMinWidth(300);
       stage.centerOnScreen();
       
       //set scene
       stage.setScene(new Scene(root));
       
       //show
       stage.showAndWait();
    }
    
    /*
     * Set quantity
     */
    private static void setProductQuantity(int quantity)
    {
	      
	   //set new quantity
	   table.getSelectionModel().getSelectedItem().setQuantity(quantity);
	      
	   //refresh
	   table.refresh();
	 
	   //add items to list
	   products.get(products.indexOf(table.getSelectionModel().getSelectedItem()));
	      
	   //add items to observable list 
	   productList.get(productList.indexOf(table.getSelectionModel().getSelectedItem().getName()));	
		
	    //create decimal format
		DecimalFormat df = new DecimalFormat("#.##");
		   
		//compute sub total 
		subTotal.setText(df.format(Product.computeSubTotal(products, Discount.getText())));
		
		
		//compute total
		double total = Product.computeTotal(subTotal.getText(), Tax.getText());  	

		for(Product p : products)
		{
		   if(p.getUnitSize().equals("0"))
		   {
		      total += p.getUnitPrice();   
		   }	   
		}
		
		//set total
		Total.setText(Double.toString(Receipt.setPrecision(total)));
    }
    
    /*
     * Set product list
     */
    public static void setProductList(ObservableList<Product> other)
    { 
       products = other;   	
    }
    
    /*
     * Set product list for product names
     */
    public static void setProductNamesList(ArrayList<String> other)
    { 
        productList = other;  	
    }
    
    /*
     * Get product list of names
     */
    public static ArrayList<String> getProductNamesList()
    {
       return productList;
    }
    
    /*
     * Set Customer Name
     */
    public static void setCustomer(String name)
    { 
       //set the customer name	
       SimpleStringProperty property = new SimpleStringProperty(name);
       customer.textProperty().unbind();
       customer.textProperty().bind(property);
       
    }
    
    /*
     * Set recharge/ phone card
     */
    private static void displayOtherServices()
    {
       FlowPane root = new FlowPane();
       
       //setuo
       root.setVgap(7);
       root.setHgap(7);
       root.setPrefWrapLength(210);
       
       //recharges
       Button sevenPhone = Icon.createPaymentButtonIcon("$7 Phone Card", "/res/tigo.JPG");
       Button tenPhone = Icon.createPaymentButtonIcon("$10 Phone Card", "/res/claro.jpg");
       Button twoPhone = Icon.createPaymentButtonIcon("$2 Card", "/res/boss.jpg");
       Button boss = Icon.createPaymentButtonIcon("Boss Revolution", "/res/boss1.png");
       Button ml = Icon.createPaymentButtonIcon("Mi Llamada", "/res/millamada.png");
       Button payxchange = Icon.createPaymentButtonIcon("Payxchange", "/res/payxchange.jpg");
       
       //stage
       Stage stage = new Stage();
       
       //set on action
       tenPhone.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
			
			//close
			stage.close();
			
			//process
			BillPayment.processPhoneCards(products, 10);
			
		}
    	   
       });
       //set on action
       sevenPhone.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
			
			//close
			stage.close();
			
			//process
			BillPayment.processPhoneCards(products, 7);
			
		}
    	   
       });
       //set on action
       twoPhone.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
			
			//close
			stage.close();
			
			//process
			BillPayment.processPhoneCards(products, 2);
			
		}
    	   
       });
       
       //set on action
       boss.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
			
			//close
			stage.close();
			
			//process
			BillPayment.processBillPayment(products, 2);
			
		}
    	   
       });
       
       payxchange.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
			
			//close
			stage.close();
			
			//process
			BillPayment.processBillPayment(products, 1);
			
		}
    	   
       });
       
       ml.setOnAction(new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
			
			//close
			stage.close();
			
			//process
			BillPayment.processBillPayment(products, 4);
			
		}
    	   
       });
       
		//create labels
		Label cashText = new Label("$7 Phone Card");
		Label visaText = new Label("$10 Phone Card");
		Label masterText = new Label("$2 Phone Card");
		Label amexText = new Label("Boss Revolution");
		Label discoverText = new Label("Mi Llamada");
		Label foodText = new Label("Payxchange");
		
		
		//set label text fill color
		cashText.setTextFill(Color.WHITE);
		visaText.setTextFill(Color.WHITE);
		masterText.setTextFill(Color.WHITE);
		amexText.setTextFill(Color.WHITE);
		discoverText.setTextFill(Color.WHITE);
		foodText.setTextFill(Color.WHITE);
		
		//create layouts to hold options
		VBox sevenOption = new VBox();
		VBox tenOption = new VBox();
		VBox twoOption = new VBox();
		VBox bossOption = new VBox();
		VBox payOption = new VBox();
		VBox miOption = new VBox();
		
		//set alignment of layouts
		sevenOption.setAlignment(Pos.CENTER);
		tenOption.setAlignment(Pos.CENTER);
		twoOption.setAlignment(Pos.CENTER);
		bossOption.setAlignment(Pos.CENTER);
		payOption.setAlignment(Pos.CENTER);
		miOption.setAlignment(Pos.CENTER);

		//create layout to hold buttons
		HBox methods = new HBox();
		
		//add icons to layout
		sevenOption.getChildren().addAll(sevenPhone, cashText);
		tenOption.getChildren().addAll(tenPhone, visaText);
		twoOption.getChildren().addAll(twoPhone, masterText);
		bossOption.getChildren().addAll(boss, amexText);
		payOption.getChildren().addAll(payxchange, foodText);
		miOption.getChildren().addAll(ml, discoverText);
		
		
		//add all layout options to layout
		methods.getChildren().addAll(tenOption, sevenOption, twoOption, bossOption, 
				                     payOption, miOption);
		//set spacing
		methods.setSpacing(7);
		
		//set insets of grid layout
		methods.setPadding(new Insets(5, 10, 5, 30));
       
       //add nodes to root
       root.getChildren().addAll(methods);
       
       //set id
       root.setId("border");
       
       //load stylesheets
       root.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
       
       //scene
       Scene scene = new Scene(root);
       
       //set stage
       stage.setTitle("Other services");
       //stage.setMinWidth(300);
       //stage.setMaxWidth(550);
       stage.setResizable(false);
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.centerOnScreen();
       
       //set scene
       stage.setScene(scene);
       
       //show
       stage.show();
       
       
    }
    
}
