package pos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pos.Product;

public class ProductList {

	private static TableColumn<Product, String> name;
    private static TableColumn<Product, String> unitSize;
	
	/*
	 * Create a border pane layout for the product list
	 */
	public static BorderPane createProductListLayout()
	{ 
	   BorderPane root = new BorderPane();
	   
	   //setup the layout
	   root.setPadding(new Insets(10, 10, 10, 10));
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(ProductList.class.getResource("MainScreen.css").toExternalForm());
	   
	   return root;
	}
	
	/*
	 * Create table view to display the products
	 */
	@SuppressWarnings("unchecked")
	private static TableView<Product> createProductTable()
	{ 
	   //initialize table	
	   TableView<Product> list = new TableView<>();
	   
	   //set editable
	   list.setEditable(false);
	   
	   //create columns
	   name = new TableColumn<Product, String>("Name"); 
	   unitSize = new TableColumn<Product, String>("unitSize");
	   
	   //setup column
	   name.setPrefWidth(200);
	   unitSize.setPrefWidth(75);
	   
	   //cell factory
	   name.setCellValueFactory(new PropertyValueFactory<>("name"));
	   unitSize.setCellValueFactory(new PropertyValueFactory<>("unitSize")); 
	   
	   //add columns to table
	   list.getColumns().addAll(name, unitSize);
	   
	   //setup table
	   list.setPrefWidth(350);
	   
	   return list;
	}
	
	/*
	 * Create the scene for the product list
	 */
	private static Scene createProductListScene()
	{
	    //create root layout
		BorderPane root = createProductListLayout();
		
		//create table view
		TableView<Product> list = createProductTable();
		
		//set list items
		
		//text field and button
		TextField product = new TextField();
		Button search = new Button("Search", new ImageView(new Image(ProductList.class.getResourceAsStream("/res/Apply.png"))));
		
		//top layout
		HBox top = new HBox();
		top.setSpacing(5);
		top.setAlignment(Pos.CENTER);
		top.setPadding(new Insets(10, 10, 10, 10));
		top.getChildren().addAll(product, search);
			
		//add table view to center layout
		FlowPane center = new FlowPane();
		center.setAlignment(Pos.CENTER);
		center.setPadding(new Insets(10, 10, 10, 10));
		center.getChildren().add(list);
		
		//image view
		ImageView productImage = new ImageView(new Image(ProductList.class.getResourceAsStream("/res/userPicture	.png")));
		productImage.setFitHeight(150);
		productImage.setFitWidth(150);
		
		//nodes to load extra information
		Label brandlbl = new Label("Brand");
		Label originalbl = new Label("Original Price");
		Label saleslbl = new Label("Sales Price");
		Label categorylbl = new Label("Category");
		Label datelbl = new Label("Date Created");
		
		//setup labels
		brandlbl.setTextFill(Color.WHITE);
		originalbl.setTextFill(Color.WHITE);
		saleslbl.setTextFill(Color.WHITE);
		categorylbl.setTextFill(Color.WHITE);
		datelbl.setTextFill(Color.WHITE);
		
		brandlbl.setFont(new Font("Courier Sans", 12));
		originalbl.setFont(new Font("Courier Sans", 12));
		saleslbl.setFont(new Font("Courier Sans", 12));
		categorylbl.setFont(new Font("Courier Sans", 12));
		datelbl.setFont(new Font("Courier Sans", 12));
		
		//text fields
		TextField brand = new TextField();
		TextField category = new TextField();
		TextField original = new TextField();
		TextField sales = new TextField();
		TextField date = new TextField();
		
		//set editable to false
		brand.setEditable(false);
		category.setEditable(false);
		original.setEditable(false);
		sales.setEditable(false);
		date.setEditable(false);
		
		//data layout
		GridPane data = new GridPane();
		data.setHgap(5);
		data.setVgap(5);
		data.setAlignment(Pos.CENTER);
		
		data.add(brandlbl, 0, 0);
		data.add(brand, 1, 0);
		data.add(categorylbl, 0, 1);
		data.add(category, 1, 1);
		data.add(originalbl, 0, 2);
		data.add(original, 1, 2);
		data.add(saleslbl, 0, 3);
		data.add(sales, 1, 3);
		data.add(datelbl, 0, 4);
		data.add(date, 1, 4);
		
		//right layout
		VBox right = new VBox();
		right.setAlignment(Pos.CENTER);
		right.setSpacing(5);
		right.setPadding(new Insets(10, 10, 10, 10));
		
		right.getChildren().addAll(productImage, data);
		
		//button
		Button select = new Button("Select", new ImageView(new Image(ProductList.class.getResourceAsStream("/res/Apply.png"))));
	
		//bottom layout
		FlowPane bottom = new FlowPane();
		bottom.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.setPadding(new Insets(10, 10, 10, 10));
		bottom.getChildren().add(select);
		
		//add nodes to root
		root.setTop(top);
		root.setCenter(center);
		root.setBottom(bottom);
		root.setRight(right);
		
		//create new scene
	    Scene scene = new Scene(root);
	   
	    return scene;
	}
	
	/*
	 * Setup the stage for the list
	 */
	private static Stage createListStage()
	{ 
	   Stage stage = new Stage();
	   
	   //set title
	   stage.setTitle("FASS Nova - Product List");
	   
	   //set size
	   stage.setMinWidth(350);
	   
	   //center on screen
	   stage.centerOnScreen();
	   
	   return stage;
	}
	
	/*
	 * Display product list
	 */
	public static void displayProductList()
	{ 
	   Stage stage = createListStage();	
	   
	   //create scene
	   Scene scene = createProductListScene();
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show the stage
	   stage.show();			   
	}
}
