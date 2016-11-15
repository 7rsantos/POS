package pos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainScreen {
	
	private Stage stage;

	public MainScreen(Stage stage)
	{ 
		this.stage = stage;
		setWindowSize(this.stage);
		Scene scene = buildMainScreen(this.stage);
		this.stage.setScene(scene);
		this.stage.show();
	}
	
	public static Stage setWindowSize(Stage stage)
	{ 
		Stage window = stage;
		
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
		//Button b = new Button("Click me");
		//left.setCenter(b);
		left.setPadding(new Insets(50, 80, 50, 5));
		border.setLeft(left);

		//create a gridpane that will hold the table
		GridPane root = new GridPane();
		root.setPadding(new Insets(3,5, 10, 30));
		root.setVgap(8);
		

		//create table
		TableView table = createTable();
		
		
		//create menu bar and added to the border layout
		MenuBar menuBar = MainScreen.createMenu();
	    menuBar.prefWidthProperty().bind(stage.widthProperty());
		
		//create options section
		GridPane services = createOptionSection();
		
		//create a Vbox layout
		VBox top = new VBox();
		top.getChildren().addAll(menuBar, services);
		
		//add vbox to the top of border
		border.setTop(top);
		
		//create a box and add it to root
		GridPane box = createSouthArea();
		box.setPadding(new Insets(15, 5, 5, 215));
		//box.setAlignment(Pos.BOTTOM_CENTER);
		
		
		//add table to the center of the border layout
		root.setAlignment(Pos.TOP_CENTER);
		root.getChildren().add(table);
		border.setCenter(root);
		
		//add box to border
		border.setBottom(box);
		
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
		box.setId("box");
		actions.setId("actions");
		left.setId("id");
        
		//Create the scene
		Scene scene = new Scene(border);
		
		//get the resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
		
		return scene;
	}
	
	public static MenuBar createMenu()
	{ 
		MenuBar menu = new MenuBar();
		
		Menu actions = new Menu("Actions");
		Menu inventory = new Menu("Inventory");
		Menu reports = new Menu("Reports");
		Menu settings = new Menu("Settings");
		Menu end_of_day = new Menu("End of Day");
		Menu help = new Menu("Help");
		menu.getMenus().addAll(actions, inventory, reports, end_of_day, settings, help);
		
		return menu;
	}
	
	public static GridPane createSouthArea()
	{ 
		//create box labels
		Label subtotal = new Label("Subtotal");
		Label tax = new Label("Tax");
		Label total =new Label("Total");
		Label discount = new Label("Discount");
		
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
		
		//change size of pay button
		pay.setMaxWidth(75);
		
		//create grid pane and set vgaps and hgaps 
		GridPane grid = new GridPane();
		grid.setVgap(2);
		grid.setHgap(12);
		
		//add nodes to the grid pane
		grid.add(remove, 0, 0);
		grid.add(pay, 11, 1);
		grid.add(cancel, 10, 1);
		grid.add(discount, 5, 0);
		grid.add(Discount, 6, 0);
		grid.add(subtotal, 5, 1);
		grid.add(subTotal, 6, 1);
		grid.add(tax, 8, 0);
		grid.add(Tax, 9, 0);
		grid.add(total, 8, 1);
		grid.add(Total, 9, 1);
		
		Tax.setText("9.25%");
		Discount.setText("0.00%");
		
		return grid;
	}
	
	public static GridPane createOptionSection()
	{ 
		GridPane options = new GridPane();
		
		VBox search = Icon.createIcon("Product Search", "/res/search.png");
		//VBox moneyWire = Icon.createIcon("Money Wire", "/res/moneyWire.png");
		
		//create buttons
		//Button search =  new Button("Product Search");
		//Button money = new Button("Money Wire");
		Button check = new Button("Check Cashing");
		
		options.add(search, 1, 1);
		//options.add(moneyWire, 3, 1);
		options.add(check, 5, 1);
	
        options.setHgap(15); 
		options.setAlignment(Pos.CENTER);
		options.setPadding(new Insets(30, 30, 15, 30));
		
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
		
		name.setPrefWidth(330);
		unit.setPrefWidth(55);
		quantity.setPrefWidth(55);
		unitPrice.setPrefWidth(110);
		price.setPrefWidth(140);
		
		
		//add columns to the table view
		table.getColumns().addAll(name, unit, quantity, unitPrice, price);
		
		return table;
	}
}
