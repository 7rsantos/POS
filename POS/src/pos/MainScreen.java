package pos;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainScreen {

	public static void displayMainScreen(Stage stage)
	{ 
		Stage window = stage;
		
		
		
		//set the size of the window
        window.setWidth(1010);
        window.setHeight(555);
        window.setResizable(true);
        
        //position stage at the center of the screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        
        //set minimum sizes
        stage.setMinHeight(525);
        stage.setMinWidth(750);
		
        //create a new border pane
		BorderPane border = new BorderPane();
		
		//create left border pane
		BorderPane left = new BorderPane();
		Button b = new Button("Click me");
		left.setCenter(b);
		border.setLeft(left);

		//create a gridpane
		GridPane root = new GridPane();
		root.setPadding(new Insets(75,5, 75,175));
		root.setVgap(8);
		
		//create table view
		TableView table = new TableView();
		table.setEditable(false);
		
		//create columns
		TableColumn name = new TableColumn("Name");
		TableColumn quantity = new TableColumn("Quantity");
		TableColumn price = new TableColumn("Price");
		TableColumn subtotal = new TableColumn("Subtotal");
		
		name.setPrefWidth(500);
		quantity.setPrefWidth(55);
		price.setPrefWidth(110);
		subtotal.setPrefWidth(120);
		
		
		//add columns to the table view
		table.getColumns().addAll(name, quantity, price, subtotal);
		
		//add table to the center of the border layout
		root.getChildren().add(table);
		border.setCenter(root);
		
		//create menu bar and added to the border layout
		MenuBar menuBar = MainScreen.createMenu();
	    menuBar.prefWidthProperty().bind(stage.widthProperty());
		border.setTop(menuBar);
		
		//set ids
		border.setId("border");
		root.setId("root");
        
		//Create the scene
		Scene scene = new Scene(border);
		
		//get the resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
		
		//set the scene and show
		window.setScene(scene);
		window.show();
		
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
}
