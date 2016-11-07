package pos;

import java.awt.Font;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
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
		//root.setPadding(new Insets(75,5, 75,175));
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
		box.setId("box");;
        
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
		
		//create pay and cancel buttons
		Button pay = new Button("Pay");
		Button cancel = new Button("Cancel");
		Button remove = new Button("Remove Item");
		
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
		
		Tax.setText("9.45%");
		Discount.setText("0.00%");
		
		return grid;
	}
	
	public static GridPane createOptionSection()
	{ 
		GridPane options = new GridPane();
		
		//create buttons
		Button search =  new Button("Product Search");
		Button money = new Button("Money Wire");
		Button check = new Button("Check Cashing");
		
		options.add(search, 1, 20);
		options.add(money, 2, 20);
		options.add(check, 3, 20);
	
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
		
		name.setPrefWidth(240);
		unit.setPrefWidth(55);
		quantity.setPrefWidth(55);
		unitPrice.setPrefWidth(110);
		price.setPrefWidth(120);
		
		
		//add columns to the table view
		table.getColumns().addAll(name, unit, quantity, unitPrice, price);
		
		return table;
	}
}
