package pos;

import java.time.LocalDate;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
    private static Button checkCashing;
    private static Button customers;
    private static Button moneyWire;
	private static Button search;
	private static Label customer;
	private static String barcode = "";
	private static boolean active = false;
    
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
		TableView table = createTable();
		
		
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
			    	  //event.consume();
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
		
	   System.out.println(barcode);
	   barcode = "";
	   active = false;
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
		TextField Discount = new TextField();
		Discount.setEditable(false);
		
		TextField subTotal = new TextField();
		subTotal.setEditable(false);
		
		TextField Tax = new TextField();
		Tax.setEditable(false);
		
		TextField Total = new TextField();
		Total.setEditable(false);
		
		//create pay, cancel, and remove buttons and set their icons
		Image payIcon = new Image(MainScreen.class.getResourceAsStream("/res/Buy.png"));
		Button pay = new Button("Pay", new ImageView(payIcon));
		
		Image cancelIcon = new Image(MainScreen.class.getResourceAsStream("/res/Cancel.png"));		
		Button cancel = new Button("Cancel", new ImageView(cancelIcon));
		
		Image removeIcon = new Image(MainScreen.class.getResourceAsStream("/res/Erase.png"));				
		Button remove = new Button("Remove Item", new ImageView(removeIcon));
		
		//implement actions
		cancel.setOnAction(e -> Session.logout(window));
		
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
		Image image = new Image(ActionsTable.class.getResourceAsStream("/res/Carnet.jpg"));
		ImageView carnetIcon = new ImageView(image);
		carnetIcon.setFitWidth(70);
		carnetIcon.setFitHeight(50);
		
		//add context menu
		 ContextMenu contextMenu = new ContextMenu();
		 
	     MenuItem item1 = new MenuItem("Logout");
	     item1.setOnAction(e -> Session.logout(window));
		 carnetIcon.setOnContextMenuRequested(
		    e -> contextMenu.show(carnetIcon, e.getScreenX(), e.getScreenY()+10));
		
	     //add context menu to the image
	     contextMenu.getItems().add(item1);
	     
		//create cashier info section
		Label cashier = new Label("Cashier:");
		Label cashierName = new Label("Admin");		
		
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
	
	public static TableView createTable()
	{ 
		//create table view
		TableView table = new TableView();
		table.setEditable(false);
		
		//create columns
		TableColumn name = new TableColumn("Name");
		TableColumn unit = new TableColumn("Unit Size");		
		TableColumn quantity = new TableColumn("Quantity");
		TableColumn unitPrice = new TableColumn("Unit Price");
		TableColumn price = new TableColumn("Price");
		
		name.setPrefWidth(365);
		unit.setPrefWidth(55);
		quantity.setPrefWidth(55);
		unitPrice.setPrefWidth(110);
		price.setPrefWidth(140);
		
		
		//add columns to the table view
		table.getColumns().addAll(name, unit, quantity, unitPrice, price);
		
		return table;
	}
}
