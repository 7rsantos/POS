package pos;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
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
		border.setPadding(new Insets(20,100, 100, 100));
		
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
		price.setPrefWidth(120);
		subtotal.setPrefWidth(120);
		
		
		//add columns to the table view
		table.getColumns().addAll(name, quantity, price, subtotal);
		
		//add table to the center of the border layout
		border.setCenter(table);
		//set ids
		border.setId("border");
        
		//Create the scene
		Scene scene = new Scene(border);
		
		//get the resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());
		
		//set the scene and show
		window.setScene(scene);
		window.show();
		
	}
}
