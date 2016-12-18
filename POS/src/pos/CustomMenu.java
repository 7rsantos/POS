package pos;

import java.awt.Desktop;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CustomMenu {

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
		Menu externalTools = createExternalToolsMenu();
		
		
		//add menus to the menu bar
		menu.getMenus().addAll(user, actions, inventory, customers, reports, end_of_day, 
				settings, about, externalTools);
		
		return menu;
	}	
	
	public static Menu createActionsMenu()
	{ 
		Menu actions = new Menu("Actions");
		
		//Create Menu Items
		MenuItem payment = new MenuItem("Receive Payment");
		MenuItem cancel = new MenuItem("Cancel Ticket");
		MenuItem hold = new MenuItem("Hold Receipt");
		MenuItem holdPrint = new MenuItem("Hold and Print Receipt");
		MenuItem remove = new MenuItem("Remove Item");
		MenuItem sales = new MenuItem("Sales History");
		MenuItem retrieve = new MenuItem("Retrieve Ticket On-Hold");
		MenuItem clear = new MenuItem("Clear Payments");
		MenuItem open = new MenuItem("Open Cash Drawer");
		MenuItem discount = new MenuItem("Customer Discounts");
		MenuItem receiveCash = new MenuItem("Receive Cash");
		MenuItem removeCash = new MenuItem("Remove Cash");
		MenuItem transferCash = new MenuItem("Transfer Cash");
		MenuItem transferInventory = new MenuItem("Transfer Product");
		
		//add items to actions
		actions.getItems().addAll(payment, cancel, new SeparatorMenuItem(), hold, holdPrint,
				retrieve, remove, new SeparatorMenuItem(), clear, discount, 
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
		registerProduct.setOnAction(e -> Inventory.displayAddInventory());
		
		
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
	   MenuItem inventoryReport = new MenuItem("Inventory Audit Report");
	   MenuItem transferReport = new MenuItem("Inventory Transfer Report");
	   MenuItem checksDeposited = new MenuItem("Checks to be Deposited");
	   
	   //add items to the reports menu
	   reports.getItems().addAll(salesOverview, salesProduct, new SeparatorMenuItem(),
			   inventoryReport, transferReport, new SeparatorMenuItem(), checksDeposited);
	   
	   //return reports menu
	   return reports;
	}
	
	public static Menu createEndofDayMenu()
	{ 
		
		Menu end = new Menu("End of Day");
		
		//create items for the end of day menu
		MenuItem activity = new MenuItem("1. Activity Report");
		MenuItem auditCash = new MenuItem("2. Audit Cash");
		MenuItem settlement = new MenuItem("3. Settlement");
		MenuItem bankDeposit = new MenuItem("Create Bank Deposit");
		MenuItem auditList = new MenuItem("List of Audits");
		
		//add items to the end of day menu
		end.getItems().addAll(activity, auditCash, settlement, new SeparatorMenuItem(),
				bankDeposit, new SeparatorMenuItem(),  auditList);
		
		//return the end of day menu
		return end;
	}
	
	public static Menu createSettingsMenu()
	{ 
		//create Settings Menu
		Menu settings = new Menu("Settings");
		
		//create items for settings menu
		MenuItem changeDatabase = new MenuItem("Setup Database");
		MenuItem storeInfo = new MenuItem("Store Info");
		MenuItem updateStoreInfo = new MenuItem("Update Store Info");
		
		
		//add items to the settings menu
		settings.getItems().addAll(changeDatabase, new SeparatorMenuItem(), 
				storeInfo, updateStoreInfo);
		
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
