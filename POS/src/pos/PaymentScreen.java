package pos;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PaymentScreen {

private static ObservableList<Product> allProducts;	
private static Stage stage;
private static double receiptTotal;
private static Label amountlbl;
private static String status;
private static String ticketNo;

	public static Scene displayPaymentScreen(ObservableList<Product> products, double total, Stage window, double discount, String ticketStatus, String ticketno)
	{ 
		//make a copy of products observable list
		allProducts = FXCollections.observableArrayList(products);
		
		//set balance due
		receiptTotal = total;
		
		//set status
		status = ticketStatus;
		ticketNo = ticketno;
		
		stage = window;
		setWindowSize(stage);
		Scene scene = buildPaymentScreen(total);
		
		return scene;		
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
	
	public static Scene buildPaymentScreen(double total)
	{ 
		//layout that will hold all nodes
		BorderPane root = new BorderPane();
		
		//top layout
		HBox subtitle = new HBox();
		
		//set spacing
		subtitle.setSpacing(8);
		
		//set alignment
		subtitle.setAlignment(Pos.BOTTOM_LEFT);
		
		//set insets
		subtitle.setPadding(new Insets(10, 10, 5, 45));
		
		//nodes to display amount
		Text balance = new Text("Balance due: ");
		amountlbl = new Label(Double.toString(total));
		
		//format text
		balance.setFill(Color.RED);
		amountlbl.setTextFill(Color.RED);
		
		//set font
		balance.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		amountlbl.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		
		//add children to layout
		subtitle.getChildren().addAll(balance, amountlbl);
		
		//create menu
		MenuBar menu = createMenu();
		
		//create top layout
		VBox top = new VBox();
		
		//set spacing
		top.setSpacing(5);
		
		//add nodes to top
		top.getChildren().addAll(menu, subtitle);
		
		//set top
		root.setTop(top);
		
		//create text
		Text selectPayment = new Text("Select Payment Method");
		selectPayment.setFill(Color.WHITE);
		selectPayment.setFont(Font.font("Courier New", FontWeight.BOLD, 14));		
		
		//create payment icons
		Button cash = Icon.createPaymentButtonIcon("Cash", "/res/cash.jpg");
		Button visa = Icon.createPaymentButtonIcon("Visa", "/res/visa.png");
		Button mastercard = Icon.createPaymentButtonIcon("MasterCard", "/res/mastercard.png");
		Button amex = Icon.createPaymentButtonIcon("Amex", "/res/amex.png");
		Button discover = Icon.createPaymentButtonIcon("Discover", "/res/discover.jpeg");
		
		//implement actions
		cash.setOnAction(e -> cashPayment());
		visa.setOnAction(e-> cardPayment("Visa"));
		mastercard.setOnAction(e-> cardPayment("MasterCard"));
		amex.setOnAction(e-> cardPayment("American Express"));
		discover.setOnAction(e-> cardPayment("Discover"));
		
		//create labels
		Label cashText = new Label("Cash");
		Label visaText = new Label("Visa");
		Label masterText = new Label("MasterCard");
		Label amexText = new Label("American Express");
		Label discoverText = new Label("Discover");
		
		
		//set label text fill color
		cashText.setTextFill(Color.WHITE);
		visaText.setTextFill(Color.WHITE);
		masterText.setTextFill(Color.WHITE);
		amexText.setTextFill(Color.WHITE);
		discoverText.setTextFill(Color.WHITE);
		
		//create layouts to hold options
		VBox cashOption = new VBox();
		VBox visaOption = new VBox();
		VBox masterOption = new VBox();
		VBox amexOption = new VBox();
		VBox discoverOption = new VBox();
		
		//set alignment of layouts
		cashOption.setAlignment(Pos.CENTER);
		visaOption.setAlignment(Pos.CENTER);
		masterOption.setAlignment(Pos.CENTER);
		amexOption.setAlignment(Pos.CENTER);
		discoverOption.setAlignment(Pos.CENTER);

		
		//create layout to hold buttons
		GridPane methods = new GridPane();
		
		//add icons to layout
		cashOption.getChildren().addAll(cash, cashText);
		visaOption.getChildren().addAll(visa, visaText);
		masterOption.getChildren().addAll(mastercard, masterText);
		amexOption.getChildren().addAll(amex, amexText);
		discoverOption.getChildren().addAll(discover, discoverText);
		
		//add all layout options to layout
		methods.add(selectPayment, 0, 0);
		methods.add(cashOption, 0, 1);
		methods.add(visaOption, 1, 1);
		methods.add(masterOption, 2, 1);
		methods.add(amexOption, 3, 1);
		methods.add(discoverOption, 0, 2);
		
		//set spacing
		methods.setHgap(10);
		methods.setVgap(10);
		
		//set insets of grid layout
		methods.setPadding(new Insets(5, 10, 5, 30));
				
		//set center
		root.setCenter(methods);
		
		//create image
		Image image = new Image(PaymentScreen.class.getResourceAsStream("/res/Previous.png"));
		
		//create previous button
		Button previous = new Button("Previous", new ImageView(image));
		
		//implement actions
		previous.setOnAction(e -> backToMainScreen(stage, 1));
		
		//create bottom area
		HBox bottom = new HBox();
		
		//set alignment
		bottom.setPadding(new Insets(5, 10, 25, 30));
		
		//add nodes to bottom
		bottom.getChildren().add(previous);
		
		//add button to root
		root.setBottom(bottom);
		
		
		//create the payment scene
		Scene scene = new Scene(root);
		
		//set id
		root.setId("border");
		
		//get the resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());		
		
		return scene;
	}
	
	public static void backToMainScreen(Stage stage, int caller) {
	   
	   
		Stage window = new Stage();
		
	    //close current window
        stage.close();
		
	    //close the window 	    
	    Scene mainScreen = MainScreen.displayMainScreen(window);
	    
	    //reset lists
	    MainScreen.resetProductList();
	    
	    if(caller == 1)
	    {	
	       //build the main screen
	       MainScreen.setTableItems(allProducts);
	    }
	    //display the new window
	    window.setScene(mainScreen);
	    window.show();	    
		
	}

	public static MenuBar createMenu()
	{ 
		MenuBar menu = new MenuBar();
		
		Menu user = CustomMenu.createUserMenu();
		Menu actions = CustomMenu.createActionsMenu();
		Menu inventory = CustomMenu.createInventoryMenu();
		Menu customers = CustomMenu.createCustomersMenu();
		Menu reports = CustomMenu.createReportsMenu();
		Menu settings = CustomMenu.createSettingsMenu();
		Menu end_of_day = CustomMenu.createEndofDayMenu();
		Menu about = CustomMenu.createAboutMenu();
		Menu externalTools = CustomMenu.createExternalToolsMenu();	
		
		//disable menu items
		user.setDisable(true);
		actions.setDisable(true);
		inventory.setDisable(true);
		customers.setDisable(true);
		reports.setDisable(true);
		settings.setDisable(true);
		end_of_day.setDisable(true);
		about.setDisable(true);
		externalTools.setDisable(true);
		
		//add menus to the menu bar
		menu.getMenus().addAll(user, actions, inventory, customers, reports, end_of_day, 
				settings, about, externalTools);
		
		return menu;
	}
	
	private static void cashPayment()
	{ 
	   //create root layout
		VBox root = new VBox();
		
		//create top layout
		GridPane top = new GridPane();
		
		//create bottom layout
		HBox bottom = new HBox();
		
		//labels
		Label balanceDue = new Label("Balance Due");
		Label cash = new Label("Cash Received");
		
		//set label fonts
		balanceDue.setFont(Font.font("Courier", FontWeight.BOLD, 14));
		cash.setFont(Font.font("Courier", FontWeight.BOLD, 14));
		
		//set text colors
		balanceDue.setTextFill(Color.WHITE);
		cash.setTextFill(Color.WHITE);
		
		//text fields
		TextField balance = new NumericTextField();
		TextField cashReceived = new NumericTextField();
		
		//set editable false
		balance.setEditable(false);
		cashReceived.setStyle("-text-fill: #ff0000;");
		
		//set text field fonts
		balance.setAlignment(Pos.CENTER);
		cashReceived.setAlignment(Pos.CENTER);
		
		//set fonts
		balance.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 18));
		cashReceived.setFont(Font.font("Courier", FontWeight.BOLD, 18));
		
		//set text color
		balance.setStyle("-fx-control-inner-background: #ff0000;");
		cashReceived.setStyle("-fx-control-inner-background: #00ff00;");
		
		//set balance
		balance.setText("$ " + Double.toString(receiptTotal));
		cashReceived.setText(Double.toString(receiptTotal));
		
		//images for buttons
		Image check = new Image(PaymentScreen.class.getResourceAsStream("/res/Apply.png"));
		Image image = new Image(PaymentScreen.class.getResourceAsStream("/res/Erase.png"));
		
		//buttons
		Button accept = new Button("Accept", new ImageView(check));
		Button cancel = new Button("Cancel", new ImageView(image));
		
		//set spacing in top
		top.setHgap(7);
		top.setVgap(7);
			
		//set spacing for bottom
		bottom.setSpacing(7);
		
		//set insets 
		top.setAlignment(Pos.CENTER);
		bottom.setAlignment(Pos.CENTER);
		
		//set insets
		top.setPadding(new Insets(40, 10, 10, 10));
		bottom.setPadding(new Insets(20, 10, 10, 10));
		
		//setup top layout
		top.add(balanceDue, 0, 0);
		top.add(balance, 1, 0);
		top.add(cash, 0, 1);
		top.add(cashReceived, 1, 1);
		
		//setup bottom layout
		bottom.getChildren().addAll(accept, cancel);
		
		//add nodes to root
		root.getChildren().addAll(top, bottom);
		
		//add nodes to the scene
		Scene scene = new Scene(root);
		
		//set id 
		root.setId("border");
		
		//load resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());		

		
		//setup the stage
		Stage secondary = new Stage();
		
		//set discount
		//implement actions
		cancel.setOnAction(e -> secondary.close());		
		accept.setOnAction(e -> processPayment(true, Double.parseDouble(cashReceived.getText()), secondary, MainScreen.discount, cash.getText(), status));
		
		//set sizes
		secondary.setMinWidth(300);
		
		//set modality
		secondary.initModality(Modality.APPLICATION_MODAL);
		
		//set title
		secondary.setTitle("FASS Nova - Cash Payment");
		
		//set stage on centered
		secondary.centerOnScreen();
		
		//set scene
		secondary.setScene(scene);
		
		//show stage
		secondary.show();
	}

	
	
	private static void processPayment(boolean isCashPayment, double cashReceived, Stage stage,			
			double discount, String paymentMethod, String status) 
	{
		
		//close the stage
		stage.close();
		

	   if(!isCashPayment)
	   {
		   if(receiptTotal == cashReceived)
		   {	   
		      changeScreen(0.00, isCashPayment, allProducts, cashReceived, discount, paymentMethod, status);
		   }
		   if(receiptTotal < cashReceived)
		   { 
			  changeScreen(receiptTotal - cashReceived, isCashPayment, allProducts, cashReceived, discount, paymentMethod, status); 
		   }	   
		   else
		   { 
		    	  receiptTotal = receiptTotal - cashReceived; 
		    	  
		    	  DecimalFormat df = new DecimalFormat("#.##");
		    	  
		    	  	    	  
		    	  //update label
		    	  StringProperty label = new SimpleStringProperty(df.format(receiptTotal));		    	  
		    	  amountlbl.textProperty().unbind();
		    	  amountlbl.textProperty().bind(label);			   
		   }	   
	   }
	   else
	   { 
	       if(receiptTotal == cashReceived)
	       { 
	    	  changeScreen(0.00, isCashPayment, allProducts, cashReceived, discount, paymentMethod, status);
	    	  
	       }	   
	       if(receiptTotal < cashReceived)
	       { 
	    	  changeScreen(cashReceived - receiptTotal, isCashPayment, allProducts, cashReceived, discount, paymentMethod, status);	       
	       }	
	       else
	       { 
	    	  receiptTotal = receiptTotal - cashReceived; 
	    	  
	    	  DecimalFormat df = new DecimalFormat("#.##");
	    	  
	    	  //get new total
	    	  	    	  
	    	  //update label
	    	  StringProperty label = new SimpleStringProperty(df.format(receiptTotal));
	    	  
	    	  
	    	  amountlbl.textProperty().unbind();
	    	  amountlbl.textProperty().bind(label);		  
	       }	   
	    	   
	   }	    
	}

	private static void changeScreen(double amount, boolean cashPayment, ObservableList<Product> productList, 
			             double receivedCash, double discount, String paymentMethod, String status) 
	{
		//create root layout
		VBox root = new VBox();
			
		//create top layout
		GridPane top = new GridPane();
			
		//create bottom layout
		HBox bottom = new HBox();
			
		//labels
		Label cash;
		if (cashPayment)
		{
		   cash = new Label("Cash Received");
		   
		   //increase actual cash
		   RegisterUtilities.increaseCash(receiptTotal);
	    }
		else
		{ 
		   cash = new Label("Card Payment");	
		}	
		Label changelbl = new Label("Change");
			
		//set label fonts
		changelbl.setFont(Font.font("Courier", FontWeight.BOLD, 14));
		cash.setFont(Font.font("Courier", FontWeight.BOLD, 14));
			
		//set text colors
		changelbl.setTextFill(Color.WHITE);
		cash.setTextFill(Color.WHITE);
			
		//text fields
		TextField cashReceived = new NumericTextField();
		TextField change = new NumericTextField();
			
		//set editable false
		cashReceived.setEditable(false);
		change.setEditable(false);
		change.setStyle("-text-fill: #ff0000;");
			
		//set text field fonts
		change.setAlignment(Pos.CENTER);
		cashReceived.setAlignment(Pos.CENTER);
			
		//set fonts
		change.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 18));
		cashReceived.setFont(Font.font("Courier", FontWeight.BOLD, 18));
			
		//set text color
		change.setStyle("-fx-control-inner-background: #D3D3D3;");
	    cashReceived.setStyle("-fx-control-inner-background: #00ff00;");
		
	    //create double format
	    DecimalFormat df = new DecimalFormat("#.##");
	    
		//set balance
		cashReceived.setText("$ " + df.format(Receipt.setPrecision(receivedCash)));
		change.setText("$" + df.format(amount));
			
		//images for buttons
		Image check = new Image(PaymentScreen.class.getResourceAsStream("/res/Apply.png"));
		Image image = new Image(PaymentScreen.class.getResourceAsStream("/res/Print.png"));
		
		//buttons
		Button accept = new Button("Accept", new ImageView(check));
		Button print = new Button("Accept & Print", new ImageView(image));
			
		//implement action
		//print.setOnAction(e -> Receipt.setupReceipt("Bob", receiptTotal));
		
		//set spacing in top
		top.setHgap(7);
		top.setVgap(7);
				
		//set spacing for bottom
		bottom.setSpacing(7);
			
		//set insets 
		top.setAlignment(Pos.CENTER);
		bottom.setAlignment(Pos.CENTER);
			
		//set insets
		top.setPadding(new Insets(40, 10, 10, 10));
		bottom.setPadding(new Insets(20, 10, 10, 10));
			
		//setup top layout
		top.add(cash, 0, 0);
		top.add(changelbl, 0, 1);
		top.add(cashReceived, 1, 0);
		top.add(change, 1, 1);
			
		//setup bottom layout
		bottom.getChildren().addAll(accept, print);
			
		//add nodes to root
		root.getChildren().addAll(top, bottom);
			
		//add nodes to the scene
		Scene scene = new Scene(root);
			
		//set id 
		root.setId("border");
			
		//load resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());		

		//open the cash drawer
		Session.openCashDrawer();
			
		//setup the stage
		Stage secondary = new Stage();
			
		//implement actions
		accept.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event) {
				
				secondary.close();
								
				//setup receipt
                Receipt.setupReceipt(change.getText(), "Cash", discount, paymentMethod, status, 0, ticketNo);				
				
                //reset values
                Configs.saveTempValue("customerID", "-1");
                Configs.saveProperty("customerName", "null");
                
				//reset product list
				MainScreen.resetProductList();
				
			    backToMainScreen(stage, 0);
			} 		
			
		});		

		print.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event) {
				
				//close the stage
				secondary.close();				
				
				//setup receipt
                Receipt.setupReceipt(change.getText(), cashReceived.getText(), discount, paymentMethod, status, 1, ticketNo);  
                
                //reset the product list
				MainScreen.resetProductList();
                
				//back to main screen
			    backToMainScreen(stage, 0);
			    
			} 		
			
		});			
			
		//set sizes
		secondary.setMinWidth(300);
			
		//set modality
		secondary.initModality(Modality.APPLICATION_MODAL);
			
		//set title
		secondary.setTitle("FASS Nova - Payment Complete");
			
		//set stage on centered
		secondary.centerOnScreen();
			
		//set scene
		secondary.setScene(scene);
			
		//show stage
		secondary.show();     		

	}

	private static void cardPayment(String card)
	{ 
	   //create root layout
		VBox root = new VBox();
		
		//create top layout
		GridPane top = new GridPane();
		
		//create bottom layout
		HBox bottom = new HBox();
		
		//labels
		Label balanceDue = new Label("Balance Due");
		Label cash = new Label("");
		
		if(card.equals("Visa"))
		{	
		   cash.setText("Visa Payment");
		}
		if(card.equals("MasterCard"))
		{	
		   cash.setText("MasterCard Payment");
		}
		if(card.equals("American Express"))
		{	
		   cash.setText("AMEX Payment");
		}
		if(card.equals("Discover"))
		{	
		   cash.setText("Discover Payment");
		}	
		
		//set label fonts
		balanceDue.setFont(Font.font("Courier", FontWeight.BOLD, 14));
		cash.setFont(Font.font("Courier", FontWeight.BOLD, 14));
		
		//set text colors
		balanceDue.setTextFill(Color.WHITE);
		cash.setTextFill(Color.WHITE);
		
		//text fields
		TextField balance = new NumericTextField();
		TextField cashReceived = new NumericTextField();
		
		//set editable false
		balance.setEditable(false);
		cashReceived.setStyle("-text-fill: #ff0000;");
		
		//set text field fonts
		balance.setAlignment(Pos.CENTER);
		cashReceived.setAlignment(Pos.CENTER);
		
		//set fonts
		balance.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 18));
		cashReceived.setFont(Font.font("Courier", FontWeight.BOLD, 18));
		
		//set text color
		balance.setStyle("-fx-control-inner-background: #ff0000;");
		cashReceived.setStyle("-fx-control-inner-background: #00ff00;");
		
		//set balance
		balance.setText("$ " + Double.toString(receiptTotal));
		cashReceived.setText(Double.toString(receiptTotal));
		
		//images for buttons
		Image check = new Image(PaymentScreen.class.getResourceAsStream("/res/Apply.png"));
		Image image = new Image(PaymentScreen.class.getResourceAsStream("/res/Erase.png"));
		
		//buttons
		Button accept = new Button("Accept", new ImageView(check));
		Button cancel = new Button("Cancel", new ImageView(image));
		
		//accept.setOnAction(e -> );
		
		//set spacing in top
		top.setHgap(7);
		top.setVgap(7);
			
		//set spacing for bottom
		bottom.setSpacing(7);
		
		//set insets 
		top.setAlignment(Pos.CENTER);
		bottom.setAlignment(Pos.CENTER);
		
		//set insets
		top.setPadding(new Insets(40, 10, 10, 10));
		bottom.setPadding(new Insets(20, 10, 10, 10));
		
		//setup top layout
		top.add(balanceDue, 0, 0);
		top.add(balance, 1, 0);
		top.add(cash, 0, 1);
		top.add(cashReceived, 1, 1);
		
		//setup bottom layout
		bottom.getChildren().addAll(accept, cancel);
		
		//add nodes to root
		root.getChildren().addAll(top, bottom);
		
		//add nodes to the scene
		Scene scene = new Scene(root);
		
		//set id 
		root.setId("border");
		
		//load resources
		scene.getStylesheets().add(MainScreen.class.getResource("MainScreen.css").toExternalForm());		

		
		//setup the stage
		Stage secondary = new Stage();
		
		//implement actions
		cancel.setOnAction(e -> secondary.close());
		accept.setOnAction(e -> processPayment(false, Double.parseDouble(cashReceived.getText()), secondary, 0, card, status));
		
		//set sizes
		secondary.setMinWidth(300);
		
		//set modality
		secondary.initModality(Modality.APPLICATION_MODAL);
		
		//set title
		secondary.setTitle("FASS Nova - " + card + " Payment");
		
		//set stage on center
		secondary.centerOnScreen();
		
		//set scene
		secondary.setScene(scene);
		
		//show stage
		secondary.show();
	}
	
	public static ObservableList<Product> getList()
	{ 
	   ObservableList<Product> result = allProducts;
	   
	   return result;
	}

}
