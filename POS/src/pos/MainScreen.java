package pos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainScreen {
	
	static Stage window;
	public static TableView<Product> table;
	
    private static Button checkCashing;
    private static Button customers;
    private static Button moneyWire;
	private static Button search;
	
	private static Label customer;
	
	private static String barcode = "";
	private static boolean active = false;
	
	public static ObservableList<Product> products = FXCollections.observableArrayList();
	public static ArrayList<String> productList = new ArrayList<String>();
	
	private static Button pay;
	private static Button remove;
	private static TextField subTotal;
	private static TextField Total;	
	private static TextField Tax;
	private static TextField Discount;
	
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
        window.setWidth(1010);
        window.setHeight(555);
        window.setResizable(true);
                
        window.setTitle("FASS Nova - Main Screen");
        
        //position stage at the center of the screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        window.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        
        //set minimum sizes
        window.setMinHeight(525);
        window.setMinWidth(750);    
        
        return window;
	}
	

	public static Scene buildMainScreen(Stage stage)
	{ 						
		
        //create a new border pane
		BorderPane border = new BorderPane();
		
		//create left border pane
		BorderPane left = new BorderPane();

		
		left.setPadding(new Insets(0, 80, 10, 7));
		border.setLeft(left);

		//create a gridpane that will hold the table
		GridPane root = new GridPane();
		root.setPadding(new Insets(3,5, 10, 30));
		root.setVgap(8);

		//create table
		table = createTable();
		
		
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
		GridPane bottom = createSouthArea();
		
		
		bottom.setPadding(new Insets(0, 5, 5, 5));
		bottom.setAlignment(Pos.BOTTOM_LEFT);
		
		
		//add table to the center of the border layout
		root.setAlignment(Pos.TOP_CENTER);
		root.getChildren().add(table);
		border.setCenter(root);
		
		//add box to border
		border.setBottom(bottom);
		
		//create a tool bar for the actions
		ToolBar actions = ActionsTable.createActionsTable();
		VBox actionsLayout = new VBox();
		
		//add actions tool bar to the layout
		actionsLayout.getChildren().add(actions);
		
		//set action to the left of the border layout
		border.setLeft(actions);
		
		//set ids
		border.setId("border");
		root.setId("root");
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
		
		// TODO Auto-generated method stub
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
		
		VBox date = DateBox.createDateBox();
		
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
		
		//create pay, cancel, and remove buttons and set their icons
		Image payIcon = new Image(MainScreen.class.getResourceAsStream("/res/Buy.png"));
		pay = new Button("Pay", new ImageView(payIcon));
		pay.setDisable(true);
		
		Image cancelIcon = new Image(MainScreen.class.getResourceAsStream("/res/Cancel.png"));		
		Button cancel = new Button("Cancel", new ImageView(cancelIcon));
		
		Image removeIcon = new Image(MainScreen.class.getResourceAsStream("/res/Erase.png"));				
		remove = new Button("Remove Item", new ImageView(removeIcon));
		remove.setDisable(true);
		
		//implement actions
		cancel.setOnAction(e -> AlertBox.displayOptionDialog("FASS Nova", "Do you want to cancel ticket?"));
	    remove.setOnAction(e -> deleteItem());
	    pay.setOnAction(e -> pay());
		
		//change size of pay button
		pay.setMaxWidth(75);
		
		//create grid pane and set vgaps and hgaps 
		GridPane grid = new GridPane();
		grid.setVgap(2);
		grid.setHgap(12);
		
		//add nodes to the grid pane
		grid.add(date, 0, 1);
		grid.add(remove, 4, 1);
		grid.add(pay, 13, 1);
		grid.add(cancel, 12, 1);
		grid.add(discount, 6, 0);
		grid.add(Discount, 7, 0);
		grid.add(subtotal, 6, 1);
		grid.add(subTotal, 7, 1);
		grid.add(tax, 9, 0);
		grid.add(Tax, 10, 0);
		grid.add(total, 9, 1);
		grid.add(Total, 10, 1);
		
		Tax.setText("9.25%");
		Discount.setText("0.00%");
		
		return grid;
	}
	
	public static GridPane createOptionSection()
	{ 
		//layout for options section
		GridPane options = new GridPane();
		
		//create icons
		search = Icon.createButtonIcon("Product Search", "/res/search.png");
	    moneyWire = Icon.createButtonIcon("Money Wire", "/res/moneyWire.png");
		checkCashing = Icon.createButtonIcon("Check Cashing", "/res/check.PNG");
		customers = Icon.createButtonIcon("Customer", "/res/customer.png");
		
		//create user icon
		ImageView carnetIcon = new ImageView();
		carnetIcon = Session.getUserPicture();
		
		//add context menu
		 ContextMenu contextMenu = new ContextMenu();
		 
	     MenuItem item1 = new MenuItem("Logout");
	     item1.setOnAction(e -> Session.logout(window));
		 //carnetIcon.setOnContextMenuRequested(
		    //e -> contextMenu.show(carnetIcon, e.getScreenX(), e.getScreenY()+10));
		
		 //implement actions
		 customers.setOnAction(e -> Customers.displayCustomerList(window));
		 
	     //add context menu to the image
	     contextMenu.getItems().add(item1);
	     
		//create cashier info section
		Label cashier = new Label("Cashier:");
		Label cashierName = new Label(Session.getUserFirstName());		
		
		customer = new Label("Customer");
		Label check = new Label("Check Cashing");
		Label productSearch = new Label("Product Search");
		Label money = new Label("Money Wire");
		
		//change label color
		customer.setTextFill(Color.WHITE);
		check.setTextFill(Color.WHITE);
		productSearch.setTextFill(Color.WHITE);
		money.setTextFill(Color.WHITE);
		cashier.setTextFill(Color.WHITE);
		cashierName.setTextFill(Color.WHITE);
		
		
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
		options.add(checkCashing, 22, 1);
		options.add(check, 22, 2);	
	
        options.setHgap(15); 
		options.setAlignment(Pos.CENTER);
		options.setPadding(new Insets(8, 30, 3, 30));
		
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
		
		//set sizes
		Name.setPrefWidth(350);
		unitSize.setPrefWidth(70);
		quantity.setPrefWidth(55);
		salesPrice.setPrefWidth(110);
		price.setPrefWidth(140);
		
		
		//add columns to the table view
        table.getColumns().addAll(Name, unitSize, quantity, salesPrice, price);		
		
		return table;
	}
	
    public static void deleteItem()
    { 
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
    	   
    	   //logout 
    	   Session.logout(window);   
       }	   
    }
    
    public static void pay()
    { 
       //get the items from the table	
       ObservableList<Product> allProducts = table.getItems();
       
       //close the window display payment screen
       window.close();
       Scene scene = PaymentScreen.displayPaymentScreen(allProducts, Double.parseDouble(Total.getText()), window);
       window.setScene(scene);
       window.show();
    }
    
    //set table items
    public static void setTableItems(ObservableList<Product> allProducts)
    { 
       table.setItems(allProducts); 
       
       //copy items of all products to products
       products = allProducts;
       
       //populate product list array
       for(Product p : products)
       { 
            productList .add(p.getName());	   
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
    }
    
    /*
     * Reset the observable list containing the list of products
     */
    public static void resetProductList()
    { 
       productList.clear();
       products.clear();
       
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
    
}
