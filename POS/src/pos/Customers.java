package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Customers {

	public static Stage stage;
	private static ObservableList<Person> customerList;
	private static TableView<Person> table;
	
	public static void displayCustomerList(Stage primary)
	{ 
		//close current stage
	    primary.close();	
	    
	    //create root layout
	    BorderPane root = new BorderPane();
	    
	    //textfield
	    TextField searchField = new TextField();
	    
	    //image for button
	    Image image = new Image(Customers.class.getResourceAsStream("/res/search.png"));
	    
	    //search button
	    Button search = new Button("Search", new ImageView(image));
	    
	    //menu bar
	    MenuBar menu = createMenu();
	    
	    //add nodes to the top layout
	    VBox top = new VBox();
	    top.setAlignment(Pos.CENTER);
	    top.setSpacing(10);
	    
	    //layout
	    HBox hbox = new HBox();
	    
	    //set spacing
	    hbox.setSpacing(7);
	    hbox.setAlignment(Pos.CENTER);
	    
	    //add text field and button
	    hbox.getChildren().addAll(searchField, search);
	    
	    //add nodes to top layout
	    top.getChildren().addAll(menu, hbox);
	    
	    //set padding
	    hbox.setPadding(new Insets(25, 25, 10, 25));
	    
	    //set spacing
	    top.setSpacing(8);
	    
	    //set top
	    root.setTop(top);
	    
        //create customer list
	    customerList = loadCustomerList();
	    
	    //center layout
	    HBox center = new HBox();
	    
	    //set padding
	    center.setPadding(new Insets(10, 35, 10, 40));
	    
	    //create table view
	    table = createCustomerTable();
	    
	    //set items
	    table.setItems(customerList);
	    
	    //set center alignment
	    center.setAlignment(Pos.TOP_RIGHT);
	    
	    //add nodes to center
	    center.getChildren().add(table);	    
	    
	    //add table to center
	    root.setCenter(center);
	    
	    //images for buttons
	    Image selectCustomer = new Image(Customers.class.getResourceAsStream("/res/Apply.png"));
	    Image deleteCustomer = new Image(Customers.class.getResourceAsStream("/res/Remove.png"));
	    
	    //buttons
	    Button select = new Button("Select Customer", new ImageView(selectCustomer));
	    Button delete = new Button("Delete Customer", new ImageView(deleteCustomer));
	    Button cancel = new Button("Cancel", new ImageView(new Image(Customers.class.getResourceAsStream("/res/Cancel.png"))));
	    
	    //implement actions
	    cancel.setOnAction(e -> cancel());
	    
	    //create right layout
	    VBox right = new VBox();
	    
	    //image and image view to show picture
	    Image customer = new Image(Customers.class.getResourceAsStream("/res/userPicture.png"));
	    ImageView imageview = new ImageView(customer);
	    imageview.setFitWidth(200);
	    imageview.setFitHeight(200);
	    
	    //add children
	    right.getChildren().addAll(imageview);
	    
	    //set padding
	    right.setPadding(new Insets(35, 10, 10, 25));
	    
	    //set right layout
	    root.setRight(right);
	    
	    //create layout
	    HBox bottom = new HBox();
	    
	    //set alignment
	    bottom.setAlignment(Pos.CENTER);
	    
	    //set spacing
	    bottom.setSpacing(8);
	    
	    //add buttons to layout
	    bottom.getChildren().addAll(select, delete, cancel);
	    
	    //bottom padding
	    bottom.setPadding(new Insets(25, 25, 10, 25));
	    
	    //set bottom
	    root.setBottom(bottom);
	    
	    //create scene
	    Scene scene = new Scene(root);
	    
	    //add id to root
	    root.setId("border");
	    
	    //load css resources
	    scene.getStylesheets().add((Customers.class.getResource("MainScreen.css").toExternalForm()));
	    
	    //create new stage
	    stage = new Stage();
	    
	    //set window size
	    setWindowSize(stage);
	    
	    //set title
	    stage.setTitle("FASS Nova - Customers List");
	    
	    //set scene
	    stage.setScene(scene);
	    
	    //show stage
	    stage.show();
	    
	}
	
	private static void cancel() {
		// TODO Auto-generated method stub
	    
	    backToMainScreen();
	    	
	}

	private static void backToMainScreen() {
		
		// TODO Auto-generated method stub
		stage.close();
		
		//create new stage
		Stage primary = new Stage();
		
		//setup main screen
		Scene main = MainScreen.displayMainScreen(primary);
		
		//set scene
		primary.setScene(main);
		
		//show
		primary.show();
		
	}

	public static ObservableList<Person> loadCustomerList()
	{ 
		ObservableList<Person> list = FXCollections.observableArrayList();
		
		//query
		String query = "CALL listCustomers()";
		
		try  {
	       //create a connection
	       Connection conn = Session.openDatabase();
	    
	       //create a prepare statement		
	       PreparedStatement ps = conn.prepareCall(query);
	       
	       //execute query
	       ResultSet rs = ps.executeQuery();
	       
	       //index
	       int i = 1;
	       
	       while(rs.next())
	       { 
	    	  list.add((new Person(rs.getString(i), rs.getString(i+1))));
	    	  System.out.print(rs.getString(i));
	    	  i++;
	       }	   
		}
		
	    catch(Exception e)
		{ 
	    	e.printStackTrace();
	    	AlertBox.display("FASS Nova - Error", "Could not load customer list");
		}
		
	    //return customer list
	    return list;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Person> createCustomerTable()
	{ 
		TableView<Person> table = new TableView<Person>();
		
		//set editable to false
		table.setEditable(false);
		
		//create columns
		TableColumn<Person, String> firstName = new TableColumn<Person, String>("First Name");
		firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
		TableColumn<Person, String> lastName = new TableColumn<Person, String>("Last Name");
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		
		//set sizes
		firstName.setPrefWidth(200);
		lastName.setPrefWidth(200);		
		
		//add columns to the table view
        table.getColumns().addAll(firstName, lastName);			
		
		
		return table;
	}
	
	public static Stage setWindowSize(Stage window)
	{ 
		
		//set the size of the window
        window.setWidth(1010);
        window.setHeight(555);
        window.setResizable(true);
        
        //position stage at the center of the screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
        window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
        
        //set minimum sizes
        window.setMinHeight(525);
        window.setMinWidth(750);    
        
        return window;
	}		
	
	public static MenuBar createMenu()
	{ 
	    Menu Actions = new Menu("Actions");
	    Menu Options = new Menu("Options");
	    
	    //build actions menu
	    MenuItem createCustomer = new MenuItem("Create Customer");
	    MenuItem modifyCustomer = new MenuItem("Update Customer Info");
	    MenuItem deleteCustomer = new MenuItem("Delete Customer");
	    
	    Actions.getItems().addAll(createCustomer, modifyCustomer, deleteCustomer);
	    
	    //build options menu
	    MenuItem cancel = new MenuItem("Cancel");
	    
	    //implement action
	    cancel.setOnAction(e -> backToMainScreen());
	    
	    Options.getItems().addAll(cancel);
	    
	    //menu
	    MenuBar menu = new MenuBar();
	    
	    //add items to menu bar
	    menu.getMenus().addAll(Actions, Options);
	    
	    return menu;
	}
}
