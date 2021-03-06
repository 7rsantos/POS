package pos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pos.Product;
import java.util.*;

public class ProductList {

	private static TableColumn<Product, String> name;
    private static TableColumn<Product, String> unitSize;
    private static ObservableList<Product> filteredList;
    private static TableView<Product> list;
    private static ObservableList<Product> productList;
    private static TextField productField;
	public static Date start;
	public static Date end;
	private static Logger logger = Logger.getLogger(ProductList.class);
    
	/*
	 * Create a border pane layout for the product list
	 */
	public static BorderPane createProductListLayout()
	{ 
	   BorderPane root = new BorderPane();
	   	   
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
	   name.setPrefWidth(320);
	   unitSize.setPrefWidth(180);
	   
	   //cell factory
	   name.setCellValueFactory(new PropertyValueFactory<>("name"));
	   unitSize.setCellValueFactory(new PropertyValueFactory<>("unitSize")); 
	   
	   //add columns to table
	   list.getColumns().addAll(name, unitSize);
	   
	   //setup table
	   list.setPrefWidth(500);
	   
	   return list;
	}
	
	/*
	 * Create the scene for the product list
	 */
	public static Scene createProductListScene(Stage stage, ObservableList<Product> allProducts, String customer, ArrayList<String> productPos, int caller)
	{
	    //create root layout
		BorderPane root = createProductListLayout();
		
		//menu
		MenuItem delete = new MenuItem("Delete Product");
		Menu actions = new Menu("Actions");
		MenuBar menu = new MenuBar();
		
		//add items to menu
		actions.getItems().add(delete);
		
		//add menus to bar
		menu.getMenus().add(actions);
		menu.prefWidthProperty().bind(stage.widthProperty());
		
		//create table view
		list = createProductTable();
		
		//set on action
		delete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   //check
			   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)
			   {
				  if(list.getSelectionModel().getSelectedItem() != null)
				  {	  
			         Product.deleteProduct(list.getSelectionModel().getSelectedItem().getName());	   
				  }
				  else
				  {
				     AlertBox.display("FASS Nova", "Please select a product");	  
				  }		  
			   }	
			   else
			   {
			      AlertBox.display("FASS Nova", "You do not have permission to perform this action");	   
			   }	   
			}
			
		});
		
		//set list items
		productList = getProductList();
		
		//filtered list
		filteredList = FXCollections.observableArrayList();
		
		//add all data
		filteredList.addAll(productList);
		
		//list for changes in master data
		productList.addListener(new ListChangeListener<Product>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Product> c) {
			   
				//update list
				updateFilteredData();
			}		
		});
		
		//set table items
		list.setItems(productList);
		
		//text field and button
		productField = new TextField();
		Button search = new Button("Search", new ImageView(new Image(ProductList.class.getResourceAsStream("/res/search2.png"))));
		Button back = new Button("Go Back", new ImageView(new Image(ProductList.class.getResourceAsStream("/res/Go back.png"))));
		
		//set on action
		search.setOnAction(e -> updateFilteredData());
		back.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			
				//close the current stage
				stage.close();
				
				//go back to MainScreen
				PaymentScreen.backToMainScreen(stage, 0);	
				
			}
			
		});
		
		//button
		Button select = new Button("Select", new ImageView(new Image(ProductList.class.getResourceAsStream("/res/Apply.png"))));
		
		//add listener to product search text field
		productField.textProperty().addListener(new ChangeListener<String>()  {
		
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				//disable select
				select.setDisable(true);
				
				// reset selection
				list.getSelectionModel().clearSelection();
				
				// update filter list
				updateFilteredData();
				
			}
			
		});
		
		//top container
		HBox top = new HBox();
		top.setSpacing(5);
		top.setAlignment(Pos.CENTER);
		top.setPadding(new Insets(0, 10, 10, 10));
		top.getChildren().addAll(productField, search);
		
		//top layout
		VBox topLayout = new VBox();
		topLayout.setAlignment(Pos.CENTER);
		topLayout.setSpacing(7);
		
		//add nodes top top layout
		topLayout.getChildren().addAll(menu, top);
		
		//add table view to center layout
		FlowPane center = new FlowPane();
		center.setAlignment(Pos.CENTER);
		center.setPadding(new Insets(10, 10, 10, 10));
		center.getChildren().add(list);
		
		//image view
		ImageView productImage = new ImageView(new Image(ProductList.class.getResourceAsStream("/res/groceries.png")));
		productImage.setFitHeight(250);
		productImage.setFitWidth(200);
		
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

		//disable select
		select.setDisable(true);
		
		//selection set on action
		select.setOnAction(new EventHandler<ActionEvent>()  {
			@Override
			public void handle(ActionEvent event) {
				
				if(caller == 1)
				{			
				   //close the stage
				   stage.close();
				
				   //set the main screen
				   Scene mainScreen = MainScreen.displayMainScreen(stage);
				
				
				   if(Collections.frequency(allProducts, list.getSelectionModel().getSelectedItem()) > 0)
				   {
                       int quantity = allProducts.get(allProducts.indexOf(list.getSelectionModel().getSelectedItem().getName())).getQuantity();
                   
                       allProducts.get(allProducts.indexOf(list.getSelectionModel().getSelectedItem())).setQuantity(quantity++);
                    
                       //update list
                       MainScreen.setTableItems(allProducts);
			       }
				   else
				   {
				       MainScreen.addItems(list.getSelectionModel().getSelectedItem().getName());  
				   }	
				
				   //set customer
				   MainScreen.setCustomer(customer);
				
				   stage.setScene(mainScreen);				
				   stage.show();				
				}
				else if (caller == 2)
				{
				   //open main screen
					PaymentScreen.backToMainScreen(stage, 2);
					
				   //open reports window	
				   Reports.getProductSalesDetails(list.getSelectionModel().getSelectedItem().getName(), start, end);	
				}	
				else if (caller ==3)
				{
				   if(list.getSelectionModel().getSelectedItem() != null)
				   {
					  //close the stage
					  stage.close(); 
					   
					  //display product update screen 
				      Inventory.displayProductUpdate(list.getSelectionModel().getSelectedItem().getName());   
				   }	
				   else
				   {
				      AlertBox.display("FASS Nova", "Please select a product");	   
				   }	   
				}	
				else if(caller == 4)
				{
				   if(list.getSelectionModel().getSelectedItem() != null && 
					  !list.getSelectionModel().isEmpty())
				   {
					  //close
					   stage.close();
					   
				      //go to next screen
					  Inventory.updateProductAmount(list.getSelectionModel().getSelectedItem().getName()); 
				   }	
				   else
				   {
					  AlertBox.display("FASS Nova", "Please select a product");   
				   }	   
				}
				else if(caller == 5)
				{
				   if(list.getSelectionModel().getSelectedItem() != null && 
					  !list.getSelectionModel().isEmpty())
				   {
					  //close
					   stage.close();
					   
					   //get product
					   Product p = list.getSelectionModel().getSelectedItem();
					   
				      //go to next screen
					  DamagedInventory.displayDamagedInventory(p.getName(), p.getUnitSize()); 
				   }	
				   else
				   {
					  AlertBox.display("FASS Nova", "Please select a product");   
				   }	   
				}
			}		
		});
		
		//add listener to selection model
		list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>(){

			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
               
				//enable button
				select.setDisable(false);
				
				//load basic info
				if(list.getSelectionModel().getSelectedItem() != null)
				{	
				   ObservableList<String> data = loadBasicProductInfo(list.getSelectionModel().getSelectedItem().getName());
				
				
				   //set items
				   brand.setText(data.get(0));
				   category.setText(data.get(1));
				   original.setText(data.get(2));
				   sales.setText(data.get(3));
				   date.setText(data.get(4));
				
				   //update photo
				   Image image = getProductPicture(list.getSelectionModel().getSelectedItem().getName());
				   productImage.setImage(image);
				}   
			}
			
		});		
		
		//bottom layout
		FlowPane bottom = new FlowPane();
		bottom.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.setHgap(7);		
		bottom.setPadding(new Insets(10, 10, 10, 10));
		bottom.getChildren().addAll(back,select);
		
		//add nodes to root
		root.setTop(topLayout);
		root.setCenter(center);
		root.setBottom(bottom);
		root.setRight(right);
		
		//create new scene
	    Scene scene = new Scene(root);
	   
	    return scene;
	}
	
	//update list when text field changes
	private static void updateFilteredData() {
	   
		//clear filter list
		filteredList.clear();
		
		//copy elements that match
		for(Product p: productList)
		{ 
		   if(matchesFilter(p))	
		   { 
			  filteredList.add(p);   
		   }	   
		}	
		
		//update table
		reapplyTableSortOrder();
	}

	
	/*
	 * Copy elements only if they match our filter
	 */
	private static boolean matchesFilter(Product p) {
	
		String filter = productField.getText();
		
		if(filter == null || filter.isEmpty())
		{ 
		   return true;	
		}		
		
		String lowerCaseFiltering = filter.toLowerCase();
		
		if(p.getName().toLowerCase().indexOf(lowerCaseFiltering) != -1)
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
	   list.setItems(filteredList);	
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
	public static void displayProductList(ObservableList<Product> allProducts, String customer, ArrayList<String> productPos)
	{ 
	   Stage stage = createListStage();	
	   
	   //create scene
	   Scene scene = createProductListScene(stage, allProducts, customer, productPos, 1);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show the stage
	   stage.show();			   
	}
	
	/*
	 * Get the list of products
	 * @return Observable List containing the list of products
	 */
	private static ObservableList<Product> getProductList()
	{ 
	   ObservableList<Product> productList = FXCollections.observableArrayList();
	   String query = "Select Name, unitSize from Product WHERE Product.productStoreCode = ?";
	   
	   try
	   { 
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareCall(query);
		  
		  //set parameters
		  ps.setString(1, Configs.getProperty("StoreCode"));
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //copy the result set
		  while(rs.next())
		  {
			 //add product to list 
			 Product p = new Product();
			 p.setName(rs.getString(1));
			 p.setUnitSize(rs.getString(2));
		     productList.add(p);	  
		  	 
		  } 
		  
		  rs.close();
		  ps.close();		  
		  conn.close();
		  
		  logger.info("Product list loaded succesfully");
	   }
	   catch(Exception e)
	   {
	      logger.error("Could not retrieve product list", e);	   
	   }
	   
	   return productList;
	}
	
	/*
	 * Load basic product info
	 */
	private static ObservableList<String> loadBasicProductInfo(String name)
	{ 
	   ObservableList<String> data = FXCollections.observableArrayList();
	   String query = "SELECT Brand, Category, originalPrice, salesPrice, dateCreated FROM Product WHERE Product.productStoreCode = ? AND Product.Name = ?";
	   
	   try
	   { 
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareCall(query);
		  
		  //set parameters
		  ps.setString(1, Configs.getProperty("StoreCode"));
		  ps.setString(2, name);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //get info
		  while(rs.next())
		  { 
		     data.add(rs.getString(1));
		     data.add(rs.getString(2));
		     data.add(rs.getString(3));
		     data.add(rs.getString(4));
		     data.add(rs.getString(5));
		  }	
		  
		  //close
		  rs.close();
		  ps.close();
		  conn.close();
		  
		  //logger
		  logger.info("list retrieved successfully");
				  
	   }
	   catch(Exception e)
	   { 
		  logger.error("Could not load basic product info", e);   
	   }
	   
	   return data;
	}
	
	/*
	 * Get product picture
	 */
	public static Image getProductPicture(String name)
	{ 
		byte[] imageArray = null;
		Image image = null;
		
	    //retrieve image from the database
	    String query = "SELECT Photo FROM Product WHERE Product.productStoreCode = ? AND Product.Name = ?";
	       
	    try
	    { 
	       Connection conn = Session.openDatabase();
	       PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
	    	  
	       //set parameters
	       ps.setString(1, Configs.getProperty("StoreCode"));
	       ps.setString(2, name);	  
	       
	       //execute query
	       ResultSet rs = ps.executeQuery();
	    	  
	       //process
	       while(rs.next())
	       { 
	    	  imageArray= rs.getBytes(1);	  
	       }	  
	    	  
	          InputStream in = new ByteArrayInputStream(imageArray);    	  
	          BufferedImage bufferedImage = ImageIO.read(in);    	  
	          image = SwingFXUtils.toFXImage(bufferedImage, null);
	    	  
	          in.close();
	          rs.close();
	          ps.close();
	    	  conn.close();
	       }
	       catch(Exception e)
	       { 
	          logger.error("Could not load product picture", e);	   
	       }	
	    
	    return image;
	}   
	
	/*
	 * Manager reports call
	 */
	public static void initializeFields(Stage stage, Date start1, Date end1)
	{
	   //initialize fields
		ProductList.start = start1;
		ProductList.end = end1;
		
		//display
		Scene productScene = ProductList.createProductListScene(stage, MainScreen.products, "Customer", MainScreen.productList, 2);
		
		//set scene
		stage.setScene(productScene);
		
		//show
		stage.show();
	 	
	}
}
