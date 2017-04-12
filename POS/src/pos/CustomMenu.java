package pos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class CustomMenu {
	
    public static MenuItem payment;
	
	public static MenuBar createMenu()
	{ 
		MenuBar menu = new MenuBar();
		
		Menu user = createUserMenu();
		Menu actions = createActionsMenu();
		Menu inventory = createInventoryMenu();
		Menu customers = createCustomersMenu();
		Menu reports = createReportsMenu();
		Menu settings = createSettingsMenu();
		Menu end_of_day = createEndofDayMenu();
		Menu about = createAboutMenu();
		//Menu externalTools = createExternalToolsMenu();
		
		
		//add menus to the menu bar
		menu.getMenus().addAll(user, actions, inventory, customers, reports, end_of_day, 
				settings, about);
		
		return menu;
	}	
	
	public static Menu createActionsMenu()
	{ 
		Menu actions = new Menu("Actions");
		
		//Create Menu Items
		payment = new MenuItem("Receive Payment");
		MenuItem cancel = new MenuItem("Cancel Ticket");
		MenuItem hold = new MenuItem("Hold Receipt");
		MenuItem sales = new MenuItem("Sales History");
		MenuItem retrieve = new MenuItem("Retrieve Ticket On-Hold");
		MenuItem clear = new MenuItem("Clear Payments");
		MenuItem open = new MenuItem("Open Cash Drawer");
		MenuItem discount = new MenuItem("Customer Discounts");
		MenuItem receiveCash = new MenuItem("Receive Cash");
		MenuItem removeCash = new MenuItem("Remove Cash");
		MenuItem transferCash = new MenuItem("Transfer Cash");
		MenuItem transferInventory = new MenuItem("Transfer Product");
		
		//implement actions
		open.setOnAction(e -> Session.openCashDrawer());
		clear.setOnAction(e -> MainScreen.resetProductList());
	    discount.setOnAction(e -> Discount.displayDiscountScreen());
		cancel.setOnAction(e -> Session.logout(MainScreen.window));
		sales.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
            
				//close the stage
				MainScreen.closeStage();
				
				//display sales history screen
				SalesHistory.displaySalesHistory();
			} 

		});
		receiveCash.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				
				if(Integer.parseInt(Configs.getProperty("Privilege")) >= 1)
				{	
			       //close main screen
				   MainScreen.closeStage();
				
			      //go to next screen
				  CashOperations.displayCashWD(1);
				}
				else
				{
				   AlertBox.display("FASS Nova", "You do not have permission to perform this action");	
				}		
			}
			
		}) ;
		payment.setOnAction(e -> PaymentScreen.displayPaymentScreen(MainScreen.products, MainScreen.getTotal(), MainScreen.window, Integer.parseInt(MainScreen.Discount.getText().substring(0,  MainScreen.Discount.getText().length()-1)), MainScreen.status, MainScreen.ticketNo));

		removeCash.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				
				if(Integer.parseInt(Configs.getProperty("Privilege")) >= 1)
				{	
			       //close main screen
				   MainScreen.closeStage();
				
			      //go to next screen
				  CashOperations.displayCashWD(2);
				}
				else
				{
				   AlertBox.display("FASS Nova", "You do not have permission to perform this action");	
				}		
			}
			
		}) ;
		hold.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			
			   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)
			   {
				  if(!MainScreen.products.isEmpty())
				  {
				     //method to handle on-hold tickets
					 Receipt.createOnHoldTicket(MainScreen.products, MainScreen.getTotal(), "Pending", Double.parseDouble(MainScreen.Discount.getText().substring(0, MainScreen.Discount.getLength()-1)), "On-hold", 1);
					 
					 //set status
					 MainScreen.status = "On-hold";
				  }	  
				  else
				  {
				     AlertBox.display("FASS Nova", "No products selected");	  
				  }	  
			   }	
			   else
			   {
				  AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }	   
			}
		   	
		});
		retrieve.setOnAction(e -> Receipt.displayOnHoldTickets());
		transferCash.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   //check priority level
				if(Double.parseDouble(Configs.getProperty("Privilege")) >= 2)
				{
				   CashOperations.transferCashDisplay();	
				}	
				else
				{
				   AlertBox.display("FASS Nova", "You do not have permission to perform this action");	
				}	
			}
			
		});
		transferInventory.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			   //check privilege level
				if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)
				{
				   if(!MainScreen.productList.isEmpty())
				   {
					   ProductUtilities.displayTransferProduct(MainScreen.products);   
				   }	
				   else
				   {
					  AlertBox.display("FASS Nova", "Select products to be transferred");   
				   }	   
				}	
				else
				{
				   AlertBox.display("FASS Nova", "You do not have permission to perform this action");	
				}	
			}
			
		});
		
		
		//add items to actions
		actions.getItems().addAll(payment, cancel, new SeparatorMenuItem(), hold,
				retrieve, new SeparatorMenuItem(), clear, discount, 
				new SeparatorMenuItem(),
				sales, open, new SeparatorMenuItem(), receiveCash, removeCash, 
				new SeparatorMenuItem(), transferCash, transferInventory);
		
		return actions;
		
	}
	
	public static Menu createUserMenu() 
	{ 
		//create user menu
		Menu user = new Menu("User");
		
		//create user menu items
		MenuItem listUsers = new MenuItem("List Users");
		MenuItem getUserInfo = new MenuItem("Get User Info");
		MenuItem timeClock = new MenuItem("Time Clock");
		MenuItem createUser = new MenuItem("Create New User");
		MenuItem updateUserInfo = new MenuItem("Update User Info");
		MenuItem updatePassword = new MenuItem("Update User Password");		
		MenuItem logout = new MenuItem("Logout");
		
		//do something when triggered
		createUser.setOnAction(e -> UserDisplay.addUserDisplay());
		logout.setOnAction(e->Session.logout(MainScreen.window));
		updatePassword.setOnAction(e -> Session.passwordValidation(1));
		listUsers.setOnAction(e -> User.displayUsers());
		updateUserInfo.setOnAction(e -> Session.passwordValidation(3));
		
		//add menu items to the user menu
		user.getItems().addAll(listUsers, getUserInfo, new SeparatorMenuItem(), updateUserInfo,
				createUser, 
				updatePassword, new SeparatorMenuItem(), timeClock, new SeparatorMenuItem(),
				logout);
		
		
		//return user menu
		return user;
	}
	
	public static Menu createInventoryMenu()
	{ 
		//create inventory menu
		Menu inventory = new Menu("Inventory");
		
		//create inventory menu items
		MenuItem productSearch = new MenuItem("Product Search");
		MenuItem addInventory = new MenuItem("Add Product");
		MenuItem damagedInventory = new MenuItem("Damaged Inventory");
		MenuItem updateProductInfo = new MenuItem("Update Product Info");
		MenuItem registerProduct = new MenuItem("Register New Product");
		
		//set actions when triggered
		damagedInventory.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   //check privilege
			   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 1)
			   {
				   //close main screen
				   MainScreen.closeStage();
					  
			 	   //stage
				   Stage stage = new Stage();
						
			 	   //go to product list screen
				   Scene scene = ProductList.createProductListScene(stage, MainScreen.products, "", MainScreen.productList, 5);
			 	   stage.setScene(scene);
						
				   //show
				   stage.show();			       	   
			   }  
			   else
			   {
				   AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }	   
			}
			
		});
		addInventory.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 1)
			   {
				  //close main screen
				  MainScreen.closeStage();
				  
				  //stage
				  Stage stage = new Stage();
					
				  //go to product list screen
				  Scene scene = ProductList.createProductListScene(stage, MainScreen.products, "", MainScreen.productList, 4);
				  stage.setScene(scene);
					
				  //show
				  stage.show();
			   }
			   else
			   {
			      AlertBox.display("FASS Nova", "You do not have permission to perform this action");	   
			   }	   
			}
		   	
		});
		registerProduct.setOnAction(e -> Inventory.displayAddInventory());
		productSearch.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
               //close the stage
				MainScreen.closeStage();
				
				//open product list
				ProductList.displayProductList(MainScreen.products, MainScreen.customer.getText(), MainScreen.productList);
				
			} 			
		});
		
		updateProductInfo.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			   
				//close the stage
				MainScreen.closeStage();
				
				//stage
				Stage stage = new Stage();
				
				//go to product list screen
				Scene scene = ProductList.createProductListScene(stage, MainScreen.products, "", MainScreen.productList, 3);
				stage.setScene(scene);
				
				//show
				stage.show();
			}
			
		});
		
		//add menu items to inventory
		inventory.getItems().addAll(productSearch, new SeparatorMenuItem(), addInventory,
				registerProduct,damagedInventory, new SeparatorMenuItem(), updateProductInfo);
		
		return inventory;
	}
	
	public static Menu createCustomersMenu()
	{ 
		//create customer menu
		Menu customers = new Menu("Customers");
		
		MenuItem search = new MenuItem("Search for Customer");
		MenuItem add = new MenuItem("Add New Customer");
		
		//set on action
		add.setOnAction(e -> Customers.displayCustomerForm());
		search.setOnAction(new EventHandler<ActionEvent>()   {

			@Override
			public void handle(ActionEvent event) {				
				//go to the next screen
				Customers.displayCustomerList(MainScreen.window, MainScreen.products, 1);
			}
			
		});
		
		
		//add menu items to customer menu
		customers.getItems().addAll(search, add);
		
		return customers;
	}
	
	public static Menu createReportsMenu()
	{ 
	   // create reports menu	
	   Menu reports = new Menu("Reports");	
	   
	   MenuItem salesOverview = new MenuItem("Sales Overview");
	   MenuItem salesProduct = new MenuItem("Sales Detail for Product");
	   MenuItem charts = new MenuItem("Get charts & graphs");
	   MenuItem customerSales = new MenuItem("Sales Detail for customer");
	   //MenuItem transferReport = new MenuItem("Inventory Transfer Report");
	   //MenuItem checksDeposited = new MenuItem("Checks to be Deposited");
	   
	   //add items to the reports menu
	   reports.getItems().addAll(salesOverview, salesProduct, new SeparatorMenuItem(),
			   charts, new SeparatorMenuItem(), customerSales);
	   
	   //implement actions
	   salesOverview.setOnAction(e -> Reports.displayRanges(1));
	   salesProduct.setOnAction(e -> Reports.displayRanges(2));
	   charts.setOnAction(e -> Charts.displayChartOptions());
	   customerSales.setOnAction(e -> Reports.displayRanges(3));
	   
	   //return reports menu
	   return reports;
	}
	
	public static Menu createEndofDayMenu()
	{ 
		
		Menu end = new Menu("End of Day");
		
		//create items for the end of day menu
		MenuItem activity = new MenuItem("1. Activity Report");
		MenuItem auditCash = new MenuItem("2. Audit Cash");
		//MenuItem settlement = new MenuItem("3. Settlement");
		//MenuItem bankDeposit = new MenuItem("Create Bank Deposit");
		MenuItem auditList = new MenuItem("List of Audits");
		
		//add items to the end of day menu
		end.getItems().addAll(activity, auditCash,
				new SeparatorMenuItem(),  auditList);
		
		//implement actions
		auditCash.setOnAction(e -> Audit.displayAuditBills());
		activity.setOnAction(e -> Reports.displayRanges(4));
		auditList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			    if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)
			    {
			       //display audits
			       ActivityReport.displayAudits();	
			    }	
			    else
			    {
			       AlertBox.display("FASS Nova", "You do not have permission to perform this action");	
			    }	
			}
			
		});
		
		//return the end of day menu
		return end;
	}
	
	public static Menu createSettingsMenu()
	{ 
		//create Settings Menu
		Menu settings = new Menu("Settings");
		
		//create items for settings menu
		MenuItem selectPrinter = new MenuItem("Select Receipt Printer");
		MenuItem updateStoreInfo = new MenuItem("Update Store Info");
		MenuItem updateTax = new MenuItem("Update Tax Rate");

		
		//add items to the settings menu
		selectPrinter.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)
			   {
			      //validate password
				  Session.passwordValidation(5);
			   }	
			   else
			   {
				  AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }	   
			}
			
		});
		settings.getItems().addAll(selectPrinter, updateTax, new SeparatorMenuItem(), 
				updateStoreInfo);
		
		//set on action
		updateStoreInfo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   if(Integer.parseInt(Configs.getProperty("Privilege")) == 3)
			   {
			      //validate password
				  Session.passwordValidation(4);
			   }	
			   else
			   {
				  AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }	   
			}
			
		});
		
		updateTax.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   //check privilege level
			   if(Integer.parseInt(Configs.getProperty("Privilege")) == 3)
			   {
			      RegisterUtilities.displayTaxRateUpdate();	   
			   }	
			   else
			   {
				   AlertBox.display("FASS Nova", "You do not have permission to perform this action");   
			   }	   
			}
			
		});
		
		//return settings menu
		return settings;
		
	}
	
	public static Menu createAboutMenu()
	{ 
		//create about menu
		Menu about = new Menu("About");
		
		//create items for the about menu
		MenuItem version = new MenuItem("Version");
		
		//create info message
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		String message = "Created by: Ricardo Santos " + df.format(date);
		
		version.setOnAction(e -> AlertBox.display(" FASS Nova Version 1.0", 
				message));
		
		//add items to the about menu
		about.getItems().addAll(version);		
		
		//return about menu
		return about;
	}
	
	public static Menu createExternalToolsMenu()
	{ 
		//create external tools menu
		Menu externalTools = new Menu("External Tools");
		
		//create items for the external tools menu
		MenuItem boss = new MenuItem("Boss Revolution");
		   				
		//add items to the external tools menu
		externalTools.getItems().addAll(boss);	
		
		return externalTools;
	}	
	
}
