package pos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Customers {

	public static Stage stage;
	private static ObservableList<Person> customerList;
	private static ObservableList<Person> filteredList;
	private static TableView<Person> table;
	private static File file;
	private static TextField url;
	private static TextField searchField;
	private static ImageView imageView;
	public static Date start;
	public static Date end;
	
	public static void displayCustomerList(Stage primary, ObservableList<Product> products, int caller)
	{ 
		//initialize customer list
		customerList = FXCollections.observableArrayList();
		
		//close current stage
	    primary.close();	
	    
	    //create root layout
	    BorderPane root = new BorderPane();
	    
	    //text field
	    searchField = new TextField();
	    
	    //images for buttons
	    Image selectCustomer = new Image(Customers.class.getResourceAsStream("/res/Apply.png"));
	    
	    //text fields
	    NumericTextField phone = new NumericTextField();
	    TextField record = new TextField();
	    TextField check = new TextField();
	    
	    //buttons
	    Button select = new Button("Select", new ImageView(selectCustomer));
	    Button cancel = new Button("Cancel", new ImageView(new Image(Customers.class.getResourceAsStream("/res/Cancel.png"))));
	    
	    //set on action after selecting
	    select.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
				
				//close the current stage
				stage.close();
				
				if(caller == 1)
				{	
				   //build the main screen
				   Scene mainScreen = MainScreen.displayMainScreen(stage);
				   stage.setScene(mainScreen);
				
				   //set the customer
				   MainScreen.setCustomer(table.getSelectionModel().getSelectedItem().getFirstName() + " " + table.getSelectionModel().getSelectedItem().getLastName().substring(0, 1) + ".");
			
				   
				   if(table.getSelectionModel().getSelectedItem() != null)
				   {	   
				      Configs.saveTempValue("customerName", Integer.toString(table.getSelectionModel().getSelectedItem().getId()));
				      
					  //save property
					  Configs.saveTempValue("customerID", Integer.toString(table.getSelectionModel().getSelectedItem().getId()));
				   }
				   
				   //set table items
				   MainScreen.setTableItems(products);
				
				   stage.show();
				}
				else if(caller == 2)
				{
				   if(table.getSelectionModel().getSelectedItem() != null)
				   {
				      Person p = table.getSelectionModel().getSelectedItem();
				      
				      //back to main screen
				      PaymentScreen.backToMainScreen(stage, 2);
				      
				      //open reports
					  Reports.displayCustomerSalesDetails(p.getFirstName(), p.getLastName(), p.getId(), phone.getText(), start, end);
					  
				   }	   
				}		
				
			}
	    	
	    });
	    
	    //update 
	    searchField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				//disable select
				select.setDisable(true);
				
				// reset selection
				table.getSelectionModel().clearSelection();
				
				// update filter list
				updateFilteredData();				
			}
	    	
	    });
	    
	    customerList.addListener(new ListChangeListener<Person>()  {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Person> c) {

				//update
				updateFilteredData();
			}
	    	
	    });
	    
	    //image for button
	    Image image = new Image(Customers.class.getResourceAsStream("/res/search2.png"));
	    
	    //search button
	    Button search = new Button("Search", new ImageView(image));
	    
	    //set on action
	    search.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			
				//update the list
				updateFilteredData();
				
			}
	    	
	    });
	    
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
	    
		//filtered list
		filteredList = FXCollections.observableArrayList();
		
		//add all data
		filteredList.addAll(customerList);
	    
	    //center layout
	    HBox center = new HBox();
	    
	    //set padding
	    center.setPadding(new Insets(10, 35, 10, 40));
	    
	    //create table view
	    table = createCustomerTable();
	    
	    //set items
	    table.setItems(customerList);
	    
	    //set center alignment
	    center.setAlignment(Pos.CENTER);
	    
	    //add nodes to center
	    center.getChildren().add(table);	    
	    
	    //add table to center
	    root.setCenter(center);
	    
	    //set disable
	    phone.setEditable(false);
	    record.setEditable(false);
	    check.setEditable(false);
	    
	    //image and image view to show picture
	    Image customer = new Image(Customers.class.getResourceAsStream("/res/photo.png"));
	    ImageView imageview = new ImageView(customer);
	    imageview.setFitWidth(200);
	    imageview.setFitHeight(200);
	    
	    //disable select
	    select.setDisable(true);
	    
	    //implement actions
	    cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				//close
				stage.close();
				
				// display loading
				Loading.displayLoadingScreen();
							
			}
	    	
	    });
	    
	    //add listener
	    table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {

			@Override
			public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
			    
				//enable button
				select.setDisable(false);
				
				if(table.getSelectionModel().getSelectedItem() != null)
				{
				   //update the image
				   Image image = loadCustomerPhoto(table.getSelectionModel().getSelectedItem().getId());
					
				   //load basic info
				   ObservableList<String> data = FXCollections.observableArrayList(); 
				   data = loadBasicCustomerInfo(table.getSelectionModel().getSelectedItem().getId());
				
				   //set values
				   phone.setText(data.get(0));
				   check.setText("No");
				   record.setText(data.get(1));
				   
				   //set image
				   imageview.setImage(image);	
				
			  }	  
			}
	    	
	    });
	    
	    //grid pane for basic info
	    GridPane info = new GridPane();
	    info.setVgap(5);
	    info.setHgap(5);
	    info.setAlignment(Pos.CENTER);
	    info.setPadding(new Insets(10, 10, 10, 10));
	    
	    //labels
	    Label phonelbl = new Label("Phone Number");
	    Label recordlbl = new Label("Record created on");
	    Label checklbl = new Label("Allowed to Cash Checks");
	    
	    //setup labels
	    phonelbl.setTextFill(Color.WHITE);
	    recordlbl.setTextFill(Color.WHITE);
	    checklbl.setTextFill(Color.WHITE);
	    
	    phonelbl.setFont(new Font("Courier Sans", 12));
	    recordlbl.setFont(new Font("Courier Sans", 12));
	    checklbl.setFont(new Font("Courier Sans", 12));
	    
	    //add nodes to info
	    info.add(phonelbl, 0, 0);
	    info.add(phone, 1, 0);
	    info.add(checklbl, 0, 1);
	    info.add(check, 1, 1);
	    info.add(recordlbl, 0, 2);
	    info.add(record, 1, 2);
	    
	    //create right layout
	    VBox right = new VBox();
	    
	    
	    //add children
	    right.getChildren().addAll(imageview, info);
	    
	    //set padding
	    right.setPadding(new Insets(35, 10, 10, 25));
	    
	    //set right layout
	    root.setRight(right);
	    
	    //create layout
	    HBox bottom = new HBox();
	    
	    //set alignment
	    bottom.setAlignment(Pos.CENTER);
	    
	    //set spacing
	    bottom.setSpacing(6);
	    
	    //add buttons to layout
	    bottom.getChildren().addAll(select, cancel);
	    
	    //bottom padding
	    bottom.setPadding(new Insets(10, 10, 10, 10));
	    
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
	
	/*
	 * Close the customer list
	 */
	private static void cancel() {
	    
		//close the stage
		stage.close();
		
		//go back to main screen
	    backToMainScreen();
	    	
	}

	private static void backToMainScreen() {
		
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
		String query = "SELECT firstName, lastName, ID From Customer WHERE Customer.customerStoreCode = ?";
		
		try  {
	       //create a connection
	       Connection conn = Session.openDatabase();
	    
	       //create a prepare statement		
	       PreparedStatement ps = conn.prepareCall(query);
	       
	       //set parameter
	       ps.setString(1, Configs.getProperty("StoreCode"));
	       
	       //execute query
	       ResultSet rs = ps.executeQuery();
	       
	       while(rs.next())
	       { 
	    	  list.add((new Person(rs.getString(1), rs.getString(2), rs.getInt(3))));
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
		firstName.setPrefWidth(330);
		lastName.setPrefWidth(330);				
		
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
	    MenuItem deleteCustomer = new MenuItem("Delete Customer");
	    
	    Actions.getItems().addAll(createCustomer, deleteCustomer);
	    
	    //build options menu
	    MenuItem cancel = new MenuItem("Cancel");
	    
	    //implement action
	    cancel.setOnAction(e -> backToMainScreen());
	    createCustomer.setOnAction(e -> Customers.displayCustomerForm());
	    deleteCustomer.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 1)
			   {
			      if(table.getSelectionModel().getSelectedItem() != null)
			      {
			         Customers.deleteCustomer(table.getSelectionModel().getSelectedItem().getId());	  
			      }	  
			      else
			      {
			         AlertBox.display("FASS Nova", "Select customer");	  
			      } 	  
			   }
			   else
			   {
			      AlertBox.display("FASS Nova", "You do not have permission to perform this action");	   
			   }	   
			}
	    	
	    });
	    
	    Options.getItems().addAll(cancel);
	    
	    //menu
	    MenuBar menu = new MenuBar();
	    	    
	    //add items to menu bar
	    menu.getMenus().addAll(Actions, Options);
	    
	    return menu;
	}
	
	/*
	 * Get the customer photo
	 * 
	 */
	public static Image loadCustomerPhoto(int id)
	{ 
	   Image image = null;
	   byte[] imageArray = null;
		
	   String query = "SELECT Photo FROM Customer WHERE Customer.ID = ? ";
	   
	   try
	   { 
		  Connection conn  = Session.openDatabase();
		  PreparedStatement ps = conn.prepareCall(query);
		  
		  //set parameters
		  ps.setInt(1, id);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //process
		  while(rs.next())
		  { 
		      imageArray = rs.getBytes(1);	  
		  }
		  
          InputStream in = new ByteArrayInputStream(imageArray);    	  
          BufferedImage bufferedImage = ImageIO.read(in);    	  
          image = SwingFXUtils.toFXImage(bufferedImage, null);
    	  
    	  conn.close();		  
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();   
	   }
	   
	   
	   return image;
	}
	
	/*
	 * Form to create new customers
	 */
	public static void displayCustomerForm()
	{ 
	   //stage
		stage = new Stage();
		
		//root layout
		BorderPane root = new BorderPane();
		
		//text
		Text title = new Text("Create a new customer");
		title.setFont(new Font("Courier Sans", 20));
		title.setFill(Color.WHITE);
		
		//top layout
		FlowPane top = new FlowPane();
		top.setAlignment(Pos.CENTER);
		top.getChildren().add(title);
		
		//labels
		Label firstlbl = new Label("First Name");
		Label lastlbl = new Label("Last Name");
		Label phonelbl = new Label("Phone Number");
		Label allowed = new Label("Allowed to Cash Checks");
		
		//setup labels
		firstlbl.setTextFill(Color.WHITE);
		lastlbl.setTextFill(Color.WHITE);
		phonelbl.setTextFill(Color.WHITE);
		allowed.setTextFill(Color.WHITE);
		firstlbl.setFont(new Font("Courier Sans", 12));
		lastlbl.setFont(new Font("Courier Sans", 12));
		phonelbl.setFont(new Font("Courier Sans", 12));
		allowed.setFont(new Font("Courier Sans", 12));
		
		//image view
		imageView = new ImageView(new Image(Customers.class.getResourceAsStream("/res/photo.png")));
		imageView.setFitHeight(250);
		imageView.setFitWidth(250);
		
		//text fields
		TextField first = new TextField();
		TextField last = new TextField();
		NumericTextField phone = new NumericTextField();
		url = new TextField();
		
		//check box
		CheckBox check = new CheckBox();
		check.setSelected(false);
		
		//buttons
		Button select = new Button("Select Image");
		Button add = new Button("Add", new ImageView(new Image(Customers.class.getResourceAsStream("/res/Create.png"))));
		Button cancel = new Button("Cancel", new ImageView(new Image(Customers.class.getResourceAsStream("/res/Cancel.png"))));
		//Button takePicture = new Button("Take Picture", new ImageView(new Image(Customers.class.getResourceAsStream("/res/camera.png"))));
		
		//set on action
		//takePicture.setOnAction(e -> PhotoScreen.displayPhotoScreen(3));
		cancel.setOnAction(e -> stage.close());
		select.setOnAction(e -> selectImageFile(url, imageView));
		add.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			   //check if all fields were filled
				if(!first.getText().isEmpty() && !last.getText().isEmpty() && !phone.getText().isEmpty() && !url.getText().isEmpty())		
				{ 
				   //create new customer
					if(check.isSelected())
					{	
					   addCustomer(first.getText(), last.getText(), phone.getText(), true);
					}
					else
					{ 
					   addCustomer(first.getText(), last.getText(), phone.getText(), false);						
					}	
				}	
				else
				{ 
				   AlertBox.display("FASS Nova", "Please fill in all required fields");	
				}	
				   	
				
			} 
			
		});	
		
		//hbox
		HBox pictureLayout = new HBox();
		pictureLayout.setSpacing(5);
		pictureLayout.setPadding(new Insets(5,5,5,5));
		pictureLayout.getChildren().addAll(select, url);
		
		//setup right layout
		VBox right = new VBox();
		right.setSpacing(5);
		right.setAlignment(Pos.CENTER);
		right.setPadding(new Insets(10, 10, 10, 10));
		//right.getChildren().addAll(imageView, takePicture, pictureLayout);
		right.getChildren().addAll(imageView, pictureLayout);
		
		//left layout
		GridPane left = new GridPane();
		left.setAlignment(Pos.CENTER);
		left.setPadding(new Insets(10, 10, 10, 10));
		left.setHgap(5);
		left.setVgap(5);
		
		//add nodes to right layout
		left.add(firstlbl, 0, 0);
		left.add(first, 1, 0);
		left.add(lastlbl, 0, 1);
		left.add(last, 1, 1);
		left.add(phonelbl, 0, 2);
		left.add(phone, 1, 2);
		left.add(allowed, 0, 3);
		left.add(check, 1, 3);
		
		//bottom layout
		HBox bottom = new HBox();
		bottom.setSpacing(5);
		bottom.setAlignment(Pos.CENTER);
		bottom.setPadding(new Insets(10, 10, 10, 10));
		bottom.getChildren().addAll(add, cancel);
		
		//set root
		root.setTop(top);
		root.setLeft(left);
		root.setRight(right);
		root.setBottom(bottom);
		
		//set id
		root.setId("border");
		
		//load style sheets
		root.getStylesheets().add(Customers.class.getResource("MainScreen.css").toExternalForm());
		
		//setup stage
		stage.setTitle("FASS Nova - Create New Customer");
		stage.centerOnScreen();
		stage.setMinWidth(350);
		stage.initModality(Modality.APPLICATION_MODAL);
		
		//create scene
		Scene scene = new Scene(root);
		
		//set scene
		stage.setScene(scene);
		
		//show the scene
		stage.showAndWait();
	}

	public static void selectImageFile(TextField path, ImageView customer)
	{ 
		
		//create image file chooser
	    FileChooser fileChooser = Icon.createImageChooser();
		   
        //create file that will hold the image path
    	File selectedFile = Icon.selectImage(fileChooser);
		
    	//create file
    	
	    //process the selected file path
  	    if(selectedFile != null)
		{ 
		   path.setText(selectedFile.getAbsolutePath());
		   String imageUrl = "";
		   try {
			  imageUrl = selectedFile.toURI().toURL().toExternalForm();
			  
			  file = selectedFile;
			  
		   } catch (MalformedURLException e) {
			   AlertBox.display("FASS NOVA - Error", "Could not select image");
			   e.printStackTrace();
		   }
		   Image image = new Image(imageUrl);
		   
		   //set image view dimensions
		   customer.setFitWidth(200);
		   customer.setFitHeight(250);
		   
		   customer.setImage(image);
		}	   
		else
		{ 
			AlertBox.display("FASS Nova - Selection Error", "No File Selected"); 			
        }	  		
	}
	
	/*
	 * Set the picture path
	 */
	public static void setPicturePath(File path)
	{ 
	   file = path;	
	   url.setText(path.getAbsolutePath());
	   String pictureurl = ""; 
	   try
	   {
		  pictureurl = path.toURI().toURL().toExternalForm(); 
		  Image photo = new Image(pictureurl); 
	      imageView.setImage(photo);
	   }   
	   catch(MalformedURLException e)
	   { 
		   AlertBox.display("FASS Nova - Error", "Could not upload image");
		   e.printStackTrace();
	   }
	   imageView.setFitHeight(250);
	   imageView.setFitWidth(200);
	}
	
	/*
	 * Load basic customer info
	 */
	private static ObservableList<String> loadBasicCustomerInfo(int id)
	{
		//data
	   ObservableList<String> data = FXCollections.observableArrayList();	
	   
	   String query = "SELECT phoneNumber, dateCreated, allowedToCashCheck FROM Customer WHERE Customer.ID = ?";
	   try
	   { 
		   Connection conn = Session.openDatabase();
		   PreparedStatement ps = conn.prepareCall(query);
		   
		   //set parameters
		   ps.setInt(1, id);
		   
		   //execute query
		   ResultSet rs = ps.executeQuery();
		   
		   //process
		   while(rs.next())
		   { 
			  data.add(rs.getString(1));
			  data.add(rs.getString(2));
			  data.add(rs.getString(3));
		   }	   
		   
		   conn.close();
		   
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return data;
	}
	
	/*
	 * Add customer info to the database
	 * 
	 */
	
	private static void addCustomer(String first, String last, String phone, boolean allowedToCash)
	{ 
	   String query = "CALL createCustomer(?,?,?,?,?,?)";
	   try
	   {
	      FileInputStream input = new FileInputStream(file);
	      Connection conn = Session.openDatabase();
	      PreparedStatement ps = conn.prepareCall(query);
	      
	      //set parameters
	      ps.setString(1, first);
	      ps.setString(2, last);
	      ps.setString(3, phone);
	      ps.setBoolean(4, allowedToCash);
	      ps.setBlob(5, input);
	      ps.setString(6, Configs.getProperty("StoreCode"));
	      
	      //execute query
	      ps.executeQuery();
	      
	      //display success
	      AlertBox.display("FASS Nova", "Customer added successfully");
	      
	      //close
	      conn.close();
	      
	      //close window
	      stage.close();
	   }
	   catch(Exception e)
	   {
		   AlertBox.display("FASS Nova Error", "Could not add customer");
		   e.printStackTrace();
		   
	   }
	}
	
	//update list when text field changes
	private static void updateFilteredData() {
	   
		//clear filter list
		filteredList.clear();
		
		//copy elements that match
		for(Person p: customerList)
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
	private static boolean matchesFilter(Person p) {
	
		String filter = searchField.getText();
		
		if(filter == null || filter.isEmpty())
		{ 
		   return true;	
		}		
		
		String lowerCaseFiltering = filter.toLowerCase();
		
		if(p.getFirstName().toLowerCase().indexOf(lowerCaseFiltering) != -1)
		{ 
		   return true;	
		}	
		else if(p.getLastName().toLowerCase().indexOf(lowerCaseFiltering) != -1)
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
	   table.setItems(filteredList);	
	}	
	
	/*
	 * Manager reports call
	 */
	public static void initializeFields(Stage stage, Date start1, Date end1)
	{
	   //initialize fields
		Customers.start = start1;
		Customers.end = end1;
			
		//display customer list
		Customers.displayCustomerList(stage, MainScreen.products, 2);	 	
	}
	
	/*
	 * Delete customer from the database
	 */
	private static void deleteCustomer(int id)
	{
	   String query = "CALL deleteCustomer(?,?)";
	   
	   try
	   {
	      Connection conn = Session.openDatabase();
	      PreparedStatement ps = conn.prepareStatement(query);
	      
	      //set parameters
	      ps.setInt(1, id);
	      ps.setString(2, Configs.getProperty("StoreCode"));
	      
	      //execute
	      ps.execute();
	      
	      //close
	      conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	}
}
