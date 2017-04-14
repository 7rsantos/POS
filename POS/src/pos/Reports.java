package pos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Reports {

	private static Date date1;
	private static Date date2;
	
   /*
    * Display Ranges	
    * @param caller determines the action to be performed
    */
	public static void displayRanges(int caller)
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //date picker
	   DatePicker start = new DatePicker();
	   DatePicker end = new DatePicker();
	   
	   //setup date picker
	   start.setValue(LocalDate.now());
	   end.setValue(LocalDate.now());
	   	   
	   //labels
	   Label startlbl = new Label("Start Date");
	   Label endlbl = new Label("End Date");
	   Label storelbl = new Label("Store");
	   Label registerlbl = new Label("Register");
	   
	   //setup labels
	   startlbl.setFont(new Font("Courier Sans", 14));
	   endlbl.setFont(new Font("Courier Sans", 14));
	   storelbl.setFont(new Font("Courier Sans", 14));
	   registerlbl.setFont(new Font("Courier Sans", 14));
	   registerlbl.setTextFill(Color.WHITE);
	   startlbl.setTextFill(Color.WHITE);
	   endlbl.setTextFill(Color.WHITE);
	   storelbl.setTextFill(Color.WHITE);
	   
	   //buttons
	   Button cancel = new Button("Cancel",new ImageView(new Image(Reports.class.getResourceAsStream("/res/Cancel.png"))));
	   Button accept = new Button("Accept",new ImageView(new Image(Reports.class.getResourceAsStream("/res/Apply.png"))));
	   
	   //choice box
	   ChoiceBox<String> store = UserDisplay.getStoreCodes();
	   ChoiceBox<String> register = CashOperations.getRegisters(Configs.getProperty("StoreCode"));
	   
	   //set default value
	   store.setValue(Configs.getProperty("StoreCode"));
	   register.setValue(Configs.getProperty("Register"));
	   
	   //set on action
	   store.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			
			//recompute value
			register.setItems(CashOperations.getRegisters(store.getValue()).getItems());
		}
		   
	   }); 
	   
	   //top layout
	   GridPane top = new GridPane();
	   top.setHgap(10);
	   top.setVgap(10);
	   top.setAlignment(Pos.CENTER);
	   
	   //add nodes to top
	   top.add(startlbl, 0, 0);
	   top.add(start, 1, 0);
	   top.add(endlbl, 0, 1);
	   top.add(end, 1, 1);
	   top.add(storelbl, 0, 2);
	   top.add(store, 1, 2);
	   top.add(registerlbl, 0, 3);
	   top.add(register, 1, 3);
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setSpacing(7);
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, accept);
	   	   
	   //set on action
	   cancel.setOnAction(e -> stage.close());
	   accept.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {	
		   
		   //validate input
		   if(start.getValue().compareTo(end.getValue()) <= 0)
		   {			   
			   if(caller == 1)
			   {
				  //get local date 
				  LocalDate localDate1 = start.getValue();
				  LocalDate localDate2 = end.getValue();
				  
				  //close the stage
				  stage.close();
				  
				  //convert to java.utl.Date
				  date1 = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
				  date2 = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
				  
				  //go to the next screen
				  displayOverview(date1, date2, store.getValue(), Integer.parseInt(register.getValue()));             
			   }	   
			   else if(caller == 2)
			   {
			      if(Integer.parseInt(Configs.getProperty("Privilege")) >= 2)	
			      {
			    	  			          
			          //close main screen
			          MainScreen.closeStage();
			          
			          //close the stage
			          stage.close();

					  //get local date 
					  LocalDate localDate1 = start.getValue();
					  LocalDate localDate2 = end.getValue();
			          
			          //initialize
					  date1 = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
					  date2 = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
			          
			          //set scene
			          ProductList.initializeFields(stage, date1, date2);
			      }  	  
			      else
			      {
			         AlertBox.display("FASS Nova", "You do not have permission to perform this action");	  
			      } 	  
			   }
			   else if (caller == 3)
			   {
			    	  
			   	  //close this stage
			      stage.close();
			    	  
			      //close the main screen
		    	  MainScreen.closeStage();			    	  
		    	  
				  //get local date 
				  LocalDate localDate1 = start.getValue();
				  LocalDate localDate2 = end.getValue();
			          
		          //initialize
				  date1 = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
				  date2 = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
			          
		          //set scene
				  Customers.initializeFields(stage, date1, date2);
		      }
			  else if(caller == 4)
			  {
	       		  //get local date 
				  LocalDate localDate1 = start.getValue();
				  LocalDate localDate2 = end.getValue();
				          
				  //close
				  stage.close();
				  
			      //initialize
				  date1 = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
				  date2 = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
				  String code = store.getValue(); 
				  
				  ActivityReport.displayActivityReport(date1, date2, code, Integer.parseInt(register.getValue()));   
			  }	
		   }   
		   else
		   {
			  AlertBox.display("FASS Nova", "Invalid Input");   
		   }	   
		}
		   
	   });
	   
	   //root layout
	   VBox root = new VBox();
	   	   
	   //set id
	   root.setId("border");
	   
	   //get style sheets
	   root.getStylesheets().add(Reports.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup root
	   root.setAlignment(Pos.CENTER);
	   root.setPadding(new Insets(20, 20, 20, 20));
	   root.setSpacing(10);
	   
	   //add nodes to root
	   root.getChildren().addAll(top, bottom);
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(330);
	   stage.centerOnScreen();	   
	   stage.setTitle("FASS Nova - Select Ranges");	  
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Display summary of sales
	 */
	private static void displayOverview(Date start, Date end, String code, int register)
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //font
	   Font font = new Font("Courier Sans", 14);
	   
	   //root
	   VBox root = new VBox();
	   
	   //date formatter
	   String startDate = new SimpleDateFormat("MM/dd/yyyy").format(start);
	   String endDate = new SimpleDateFormat("MM/dd/yyyy").format(end);
	   String startDate2 = new SimpleDateFormat("yyyy-MM-dd").format(start);
	   String endDate2 = new SimpleDateFormat("yyyy-MM-dd").format(end);	   
	   
	   //text
	   Text title = new Text(Configs.getProperty("StoreName"));
	   Text subtitle1 = new Text("Store #" + code.substring(code.length()-1));
	   Text subtitle2 = new Text(startDate + " - " + endDate);
	   
	   //setup text
	   title.setFont(font);
	   subtitle1.setFont(font);
	   subtitle2.setFont(font);
	   
	   //top
	   VBox top = new VBox();
	   
	   //setup top
	   top.setAlignment(Pos.CENTER);
	   top.setSpacing(6);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to top
	   top.getChildren().addAll(title, subtitle1, subtitle2);
	   
	   //categories section
	   Text bookslbl = new Text("Books");
	   Text foodlbl = new Text("Food");
	   Text electronicslbl = new Text("Electronics");
	   Text drinkslbl = new Text("Drinks");
	   Text miscellaneouslbl = new Text("Miscellaneous");
	   Text category = new Text("Category");
	   Text totallbl = new Text("Total");
	   
	   //setup category labels
	   category.setFont(font);
	   bookslbl.setFont(font);
	   foodlbl.setFont(font);
	   electronicslbl.setFont(font);
	   drinkslbl.setFont(font);
	   miscellaneouslbl.setFont(font);
	   totallbl.setFont(font);
	   
	   //get values of categories
	   Text books = new Text(getCategoryAmount("Books", startDate2, endDate2, code, register));
	   Text food = new Text(getCategoryAmount("food", startDate2, endDate2, code, register));
	   Text electronics = new Text(getCategoryAmount("Electronics", startDate2, endDate2, code, register));
	   Text drinks = new Text(getCategoryAmount("drinks", startDate2, endDate2, code, register));
	   Text miscellaneous = new Text(getCategoryAmount("miscellaneous", startDate2, endDate2, code, register));
	   Text salesTotal = new Text (Double.toString(getSalesTotal(Double.parseDouble(books.getText()), Double.parseDouble(food.getText()), Double.parseDouble(electronics.getText()), 
			                             Double.parseDouble(drinks.getText()), Double.parseDouble(miscellaneous.getText()))));
	   
	   
	   //gridpane of categories
	   GridPane categorySummary = new GridPane();
	   categorySummary.setHgap(7);
	   categorySummary.setVgap(7);
	   categorySummary.setAlignment(Pos.CENTER);
	   categorySummary.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes
	   categorySummary.add(foodlbl, 0, 0);
	   categorySummary.add(food, 1, 0);
	   categorySummary.add(drinkslbl, 0, 1);
	   categorySummary.add(drinks, 1, 1);
	   categorySummary.add(bookslbl, 0, 2);
	   categorySummary.add(books, 1, 2);
	   categorySummary.add(electronicslbl, 0, 3);
	   categorySummary.add(electronics, 1, 3);
	   categorySummary.add(miscellaneouslbl, 0, 4);
	   categorySummary.add(miscellaneous, 1, 4);  
	   categorySummary.add(totallbl, 0, 5);
	   categorySummary.add(salesTotal, 1, 5);
	   
	   //category layout
	   VBox categoryLayout = new VBox();
	   categoryLayout.setAlignment(Pos.CENTER);
	   categoryLayout.setSpacing(7);
	   categoryLayout.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to category layout
	   categoryLayout.getChildren().addAll(category, categorySummary);
	   
	   //money wire section
	   Text moneylbl = new Text("Money Wire");
	   ObservableList<String> companies = MoneyWire.getCompanyNames();
	   
	   //setup money wire layout
	   VBox wireSummary = new VBox();
	   wireSummary.setSpacing(7);
	   wireSummary.setAlignment(Pos.CENTER);
	   wireSummary.setPadding(new Insets(10, 10, 10, 10));
	   
	   for(String s : companies)
	   {
		  String total = getMoneyWireTotals(s, startDate2, endDate2, code, register);   
		  
		  if(total == null)
		  {
		     total = "0";	  
		  }	  
		  
		  Text t = new Text(s + "   $" + total); 
		  wireSummary.getChildren().add(t); 
	   }
	   	   
	   //money wire section layout
	   VBox wireLayout = new VBox();
	   wireLayout.setPadding(new Insets(10, 10, 10, 10));
	   wireLayout.setSpacing(7);
	   wireLayout.setAlignment(Pos.CENTER);
	   
	   //add nodes to money wire section
	   wireLayout.getChildren().addAll(moneylbl, wireSummary);
	   
	   //payment method section
	   GridPane paymentMethod = paymentMethodsSection(startDate2, endDate2, code, register);
	   
	   //cash withdrawals, deposits sections
	   VBox cashWDSummary = displayCashWD(startDate2, endDate2, code, register);
	   
	   //calculated total
	   Text taxlbl = new Text("Calculated Tax");
	   Text tax = new Text("0.00");
	   
	   taxlbl.setFont(font);
	   tax.setFont(font);
	   
	   //get calculated total tax
	   tax.setText(Double.toString(Receipt.setPrecision(Double.parseDouble(salesTotal.getText()) * (Double.parseDouble(Configs.getProperty("TaxRate")) / 100))));
	   
	   // tax layout
	   HBox taxLayout = new HBox();
	   taxLayout.setSpacing(7);
	   taxLayout.setPadding(new Insets(10, 10, 10, 10));
	   taxLayout.setAlignment(Pos.CENTER);
	   
	   //add nodes to tax layout
	   taxLayout.getChildren().addAll(taxlbl, tax);
	   
	   //get total revenue for the period
	   Text revenuelbl = new Text("Revenue for the period: ");
	   Text revenue = new Text(Double.toString(Receipt.setPrecision(Double.parseDouble(salesTotal.getText()) + Double.parseDouble(tax.getText()))));
	   
	   //setup text
	   revenuelbl.setFont(font);
	   revenue.setFont(font);
	   
	   //revenue layout
	   HBox revenueLayout = new HBox();
	   revenueLayout.setSpacing(7);
	   revenueLayout.setAlignment(Pos.CENTER);
	   revenueLayout.setPadding(new Insets(10, 10, 15, 10));
	   
	   //add nodes to revenue layout
	   revenueLayout.getChildren().addAll(revenuelbl, revenue);
	   
	   //new cash section
	   Text cashlbl = new Text("New Cash Total");
	   Text cash = new Text(Double.toString(RegisterUtilities.getExpectedCash()));
	   
	   //setup text
	   cashlbl.setFont(font);
	   cash.setFont(font);
	   
	   //layout
	   HBox cashLayout = new HBox();
	   cashLayout.setSpacing(7);
	   cashLayout.setAlignment(Pos.CENTER);
	   
	   //add nodes to children
	   cashLayout.getChildren().addAll(cashlbl, cash);
	   
	   //setup root
	   root.setPadding(new Insets(20, 20, 20, 20));
	   root.setSpacing(7);
	   root.setAlignment(Pos.CENTER);
	   
	   //add nodes to root
	   root.getChildren().addAll(top, categoryLayout, wireLayout, paymentMethod, cashWDSummary, taxLayout, revenueLayout, cashLayout);
	   
	   //scroll pane
	   ScrollPane group = new ScrollPane();
	   
	   //setup group
	   group.setContent(root);
	   group.setPadding(new Insets(20, 20, 20, 20));
	   
	   //set id
	   group.setId("border");
	   
	   //get style sheets
	   group.getStylesheets().add(Reports.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(group);
	   
	   //setup stage
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setTitle("FASS Nova - Sales Overview");
	   stage.setMinWidth(330);
	   stage.setMaxHeight(450);
	   stage.centerOnScreen();
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Construct Payment Methods Section
	 */
	public static GridPane paymentMethodsSection(String start, String end, String code, int register)
	{
	   GridPane section = new GridPane();
	   
	   //font
	   Font font = new Font("Courier Sans,", 12);
	   
	   //text
	   Text visatxt = new Text("Visa");
	   Text mastertxt = new Text("Master Card");
	   Text discovertxt = new Text("Discover Text");
	   Text amextxt = new Text("American Express");
	   Text title = new Text("Payment Methods Summary");
	   Text totaltxt = new Text("Card Total");
	   
	   //setup texts
	   visatxt.setFont(font);
	   mastertxt.setFont(font);
	   discovertxt.setFont(font);
	   amextxt.setFont(font);	   
	   title.setFont(font);
	   totaltxt.setFont(font);
	   
	   //card totals
	   Text amex = getPaymentTotal("American Express", start, end, code, register);
	   Text discover = getPaymentTotal("Discover", start, end, code, register);
	   Text master = getPaymentTotal("MasterCard", start, end, code, register);
	   Text visa = getPaymentTotal("Visa", start, end, code, register);
	   Text cardTotal = new Text(Double.toString(Double.parseDouble(visa.getText()) + 
			                     Double.parseDouble(master.getText()) +
			                     Double.parseDouble(discover.getText()) +
			                     Double.parseDouble(amex.getText()))); 		   
	   
	   //add nodes to section
	   section.add(title, 1, 0);
	   section.add(visatxt, 0, 1);
	   section.add(visa, 2, 1);
	   section.add(mastertxt, 0, 2);
	   section.add(master, 2, 2);
	   section.add(discovertxt, 0, 3);
	   section.add(discover, 2, 3);
	   section.add(amextxt, 0, 4);
	   section.add(amex, 2, 4);
	   section.add(totaltxt, 0, 5);
	   section.add(cardTotal, 2, 5);
	   
	   //setup section
	   section.setAlignment(Pos.CENTER);
	   section.setHgap(7);
	   section.setVgap(7);
	   section.setPadding(new Insets(10, 10, 10, 10));
	   section.setAlignment(Pos.CENTER);
	   
	   return section;
	}
	
	/*
	 * Get totals for each payment method
	 */
	private static Text getPaymentTotal(String payment, String start, String end, String code, int register)
	{
	   String result = "0";
	   String query = "SELECT sum(Total) FROM salesTicket WHERE salesTicket.salesHistoryDate IN"
	   		        + " (SELECT salesDate FROM salesHistory WHERE salesHistory.salesDate BETWEEN ? AND ? "
	   		        + "AND salesHistory.registerSales IN"
	   		        + " (SELECT ID FROM Register WHERE Register.ID = ? AND Register.registerStoreCode = ?)) AND salesTicket.paymentMethod = ?";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, start);
		  ps.setString(2, end);
		  ps.setInt(3, register);
		  ps.setString(4, code);
		  ps.setString(5, payment);
		  
		  //execute
		  ResultSet rs = ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
			 if(rs.getString(1) != null)
			 {	 
		        result = rs.getString(1);	 
			 }   
		  }	  
		  
		  //close
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   Text text = new Text(result);
	   text.setFont(new Font("Courier Sans", 12));
	    
	   return text;
	}
	
	/*
	 * Get the final amount for each category
	 */
	public static String getCategoryAmount(String category, String start, String end, String code, int register)
	{
	   String result = "0";
	   String query = "SELECT sum(quantity), unitPrice from Item WHERE Item.Name" 
	                   + " = (SELECT Name from Product WHERE Product.Category = "
	                   + "? AND Product.productStoreCode = ?) AND Item.salesTicketNoItem IN"
	                   + " (Select ticketNo from salesTicket WHERE salesTicket.salesHistoryDate IN "
	                   + "(SELECT salesDate FROM salesHistory WHERE salesHistory.registerSales = ?)"
	                   + " AND salesTicket.ticketDate "
	                   + " BETWEEN ? AND ?)";
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, category);
		  ps.setString(2, code);
		  ps.setInt(3, register);
		  ps.setString(4, start);
		  ps.setString(5, end);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  
		  //temp variable
		  double sum = 0;
		  
		  //process the result set
		  while(rs.next())
		  {
		     sum += rs.getInt(1) * rs.getDouble(2); 
		  }	  
		  
		  //close the connection
		  conn.close();
		  
		  //set amount
		  result = Double.toString(sum);
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return result;
	}
	
	/*
	 * Get sales total
	 */
	public static double getSalesTotal(double n1, double n2, double n3, double n4, double n5)
	{
	   double result = 0.0;
	   
	   result = n1 + n2 + n3 + n4;
	   
	   return result;
	}
	/*
	 * Get total amounts per money wire company
	 */
	public static String getMoneyWireTotals(String name, String start, String end, String code, int register)
	{
	   String result = "0";
	   String query = "SELECT sum(Amount) FROM SR_Money WHERE SR_Money.companyName = "
	   		+ " (SELECT Name FROM SR_Company WHERE SR_Company.Name = ? AND SR_Company.usernameSR IN"
	   		+ " (SELECT Username FROM Employee WHERE Employee.employeeStoreCode = ?))"
	   		+ " AND SR_Money.serviceDate IN (SELECT ticketDate FROM salesTicket WHERE  "
	   		+ "salesTicket.salesHistoryDate IN (SELECT salesDate FROM salesHistory "
	   		+ "WHERE salesHistory.registerSales = ? AND salesHistory.salesDate BETWEEN ? AND ?))";
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, name);
		  ps.setString(2, Configs.getProperty("StoreCode"));
		  ps.setInt(3, register);
		  ps.setString(4, start);
		  ps.setString(5, end);
		  
		  //execute
		  ResultSet rs = ps.executeQuery();
		  
		  //process
		  while(rs.next())
		  {
		     result = rs.getString(1); 	  
		  }	  
		  
		  //close
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return result;
	}
	
	/*
	 * Display any cash withdrawals and deposits
	 */
	public static VBox displayCashWD(String start, String end, String code, int register)
	{
	   VBox layout = new VBox();
	   String query = "SELECT cashWD_Reason, Total, Notes FROM cashWithdrawalDeposit WHERE"
	   		          + " cashWithdrawalDeposit.salesTicketNoCash IN (SELECT ticketNo FROM salesTicket WHERE"
	   		          + " salesTicket.salesHistoryDate IN (SELECT salesDate FROM salesHistory WHERE"
	   		          + " salesHistory.registerSales = (SELECT ID FROM Register WHERE Register.registerStoreCode"
	   		          + " = ? AND Register.ID = ?))) AND cashWithdrawalDeposit.transactionDate BETWEEN ? AND ?";
	   
	   GridPane wdSummary = new GridPane();
	   
	   //setup summary
	   wdSummary.setAlignment(Pos.CENTER);
	   wdSummary.setHgap(6);
	   wdSummary.setVgap(6);
	   wdSummary.setPadding(new Insets(10, 10, 10, 10));
	   
	   try
	   {
		  Connection conn =Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters
		  ps.setString(1, code);
		  ps.setInt(2, register);
		  ps.setString(3, start);
		  ps.setString(4, end);
		  
		  //execute query
		  ResultSet rs = ps.executeQuery();
		  int i = 0;
		  
		  //process
		  while(rs.next())
		  {
			 //add nodes to summary 
		     wdSummary.add(new Text(rs.getString(1)), 0, i);
		     wdSummary.add(new Text(rs.getString(2)), 1, i);
		     wdSummary.add(new Text(rs.getString(3)), 2, i);
		     
		     //increase counter
		     i++;
		  }	  
		  
		  //close
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   //title
	   Text title = new Text("Cash Withdrawals/Deposits");
	   title.setFont(new Font("Courier Sans", 11));
	   
	   //setup layout
	   layout.setAlignment(Pos.CENTER);
	   layout.setPadding(new Insets(10, 10, 10, 10));
	   layout.setSpacing(7);
	   
	   //add nodes to children
	   layout.getChildren().addAll(title, wdSummary);
	   
	   return layout;
	}
	
	/*
	 * Get sales detail per product
	 */
	public static void getProductSalesDetails(String name, Date start, Date end)
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //font
	   Font font = new Font("Courier Sans", 14);
	   
	   //convert
	   String start1 = new SimpleDateFormat("MM/dd/yyyy").format(start);
	   String end1 = new SimpleDateFormat("MM/dd/yyyy").format(end);
	   
	   //text
	   Text title  = new Text(Configs.getProperty("StoreName"));
	   Text subtitle1 = new Text("Store #" + Configs.getProperty("StoreNumber"));
	   Text subtitle2 = new Text(name);
	   Text subtitle3 = new Text(start1 + "-" + end1);
	   
	   //setup text
	   title.setFont(font);
	   subtitle1.setFont(font);
	   subtitle2.setFont(font);
	   subtitle3.setFont(font);
	   
	   //top layout
	   VBox top = new VBox();
	   
	   //setup top
	   top.setAlignment(Pos.CENTER);
	   top.setSpacing(7);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to top
	   top.getChildren().addAll(title, subtitle1, subtitle2, subtitle3);
	   
	   //text
	   Text numberlbl = new Text("Number of Units Sold");
	   Text availablelbl = new Text("Number of Units Available");
	   Text originallbl = new Text("Original Price");
	   Text salesPricelbl = new Text("Sales Price");
	   Text saleslbl = new Text("Sales Total");
	   Text costlbl = new Text("Cost of Goods sold");
	   Text grosslbl = new Text("Gross Profit");
	   
	   //set font
	   numberlbl.setFont(font);
	   availablelbl.setFont(font);
	   originallbl.setFont(font);
	   salesPricelbl.setFont(font);
	   saleslbl.setFont(font);
	   costlbl.setFont(font);
	   grosslbl.setFont(font);
	   
	   //date for mysql queries
	   String start2 = new SimpleDateFormat("yyyy-MM-dd").format(start);
	   String end2 = new SimpleDateFormat("yyyy-MM-dd").format(end);
	   
	   //run query
	   ObservableList<String> values = Reports.getProductDetails(name, start2, end2);
	   
	   //text for totals
	   Text number = new Text("0");
	   
	   if(values.get(3) != null)
	   {
		   number.setText(values.get(3));        
	   }	   
	   
	   Text available = new Text(values.get(0));
	   Text original = new Text(values.get(1));
	   Text salesPrice = new Text(values.get(2));
	   Text sales = new Text(Double.toString(Double.parseDouble(number.getText()) * Double.parseDouble(salesPrice.getText())));
	   Text cost = new Text(Double.toString(Double.parseDouble(number.getText()) * Double.parseDouble(original.getText())));
	   Text gross = new Text((Double.toString(Receipt.setPrecision(Double.parseDouble(sales.getText()) - Double.parseDouble(cost.getText())))));	   

	   //set font
	   number.setFont(font);
	   available.setFont(font);
	   original.setFont(font);
	   salesPrice.setFont(font);
	   sales.setFont(font);
	   cost.setFont(font);
	   gross.setFont(font);
	   
	   //left
	   GridPane left = new GridPane();
	   
	   //setup left
	   left.setHgap(7);
	   left.setVgap(7);
	   left.setPadding(new Insets(10, 20, 10, 10));
	   
	   //add nodes to left
	   left.add(numberlbl, 0, 0);
	   left.add(number, 1, 0);
	   left.add(salesPricelbl, 2, 0);
	   left.add(salesPrice, 3, 0);
	   left.add(availablelbl, 0, 1);
	   left.add(available, 1, 1);
	   left.add(originallbl, 2, 1);
	   left.add(original, 3, 1);
	   left.add(saleslbl, 0, 2);
	   left.add(sales, 1, 2);
	   left.add(costlbl, 0, 3);
	   left.add(cost, 1, 3);
	   left.add(grosslbl, 0, 4);
	   left.add(gross, 1, 4);
	   
	   //image view
	   Image image = ProductList.getProductPicture(name);
	   ImageView imageView = new ImageView(image);
	   imageView.setFitHeight(200);
	   imageView.setFitWidth(200);
	   
	   //button
	   Button accept = new Button("Accept", new ImageView(new Image(Reports.class.getResourceAsStream("/res/Apply.png"))));
	   
	   //set on action
	   accept.setOnAction(e -> stage.close());
	   
	   //bottom
	   HBox bottom = new HBox();
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.setSpacing(7);
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(accept);
	   
	   //layout
	   BorderPane layout = new BorderPane();
	   
	   //setup layout
	   layout.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to layout
	   layout.setTop(top);
	   layout.setLeft(left);
	   layout.setRight(imageView);
	   layout.setBottom(bottom);
	   
	   //root
	   ScrollPane root = new ScrollPane();
	   
	   //setup root
	   root.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to root
	   root.setContent(layout);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
	   root.getStylesheets().add(Reports.class.getResource("MainScreen.css").toExternalForm());
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Sales Detail For Product");
	   stage.setMinWidth(300);
	   stage.centerOnScreen();
	   stage.setMaxHeight(450);
	   stage.initModality(Modality.APPLICATION_MODAL);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Get product details from the database
	 */
	private static ObservableList<String> getProductDetails(String name, String start, String end)
	{
	   ObservableList<String> result = FXCollections.observableArrayList();
	   String query1 = "SELECT numberOfUnitsAvailable, originalPrice, salesPrice FROM Product "
	   		+ "WHERE Product.productStoreCode = ? AND Product.Name = ?";
	   String query2 = "SELECT sum(Quantity) FROM Item WHERE Item.salesTicketNoItem IN"
	   		+ " (SELECT ticketno FROM salesTicket WHERE salesTicket.salesHistoryDate IN"
	   		+ " (SELECT salesDate FROM salesHistory WHERE salesHistory.registerSales IN"
	   		+ " (SELECT ID FROM Register WHERE Register.registerStoreCode = ?) "
	   		+ " AND salesHistory.salesDate BETWEEN ? AND ?)) AND Item.Name = ?";
	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query1);
		  
		  //set parameters for first query
		  ps.setString(1, Configs.getProperty("StoreCode"));
		  ps.setString(2, name);
		  
		  //execute
		  ResultSet rs= ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     result.add(rs.getString(1));
		     result.add(rs.getString(2));
		     result.add(rs.getString(3));
		  }	  
		  
		  //clear
		  ps.clearParameters();
		  
		  //second query
		  ps = conn.prepareStatement(query2);
		  
		  //set parameters
		  ps.setString(1, Configs.getProperty("StoreCode"));
		  ps.setString(2, start);
		  ps.setString(3, end);
		  ps.setString(4, name);
		  
		  //execute
		  rs = ps.executeQuery();
		  
		  //process
		  while(rs.next())
		  {
		     result.add(rs.getString(1));	  
		  }	  
		  
		  //close the connection
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return result;
	}
	
	/*
	 * Get sales details for a customer from the database
	 */
	private static String getCustomerSalesDetails(String phone, String start, String end)
	{
	   String result = "0";
	   String query1 = "SELECT sum(Total) FROM salesTicket WHERE salesTicket.CustomerID = "
	   		+ "(SELECT ID FROM Customer WHERE Customer.phoneNumber = ?) AND "
	   		+ "salesTicket.salesHistoryDate BETWEEN ? AND ?";
	   	   
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query1);
		  
		  //set parameters for first query
		  ps.setString(1, phone);
		  ps.setString(2, start);
		  ps.setString(3, end);
		  
		  //execute
		  ResultSet rs= ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     result = rs.getString(1);
		  }	  
		  
		  //close the connection
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   
	   return result;
	}
	
	/*
	 * Get sales details for a customer from the database
	 */
	private static ObservableList<Product> getCustomerProductDetails(String phone, String start, String end)
	{
	   ObservableList<Product> result = FXCollections.observableArrayList();
	   String query = "SELECT Name, SUM(Quantity) FROM Item WHERE Item.salesTicketNoItem IN"
		   		+ " (SELECT ticketno FROM salesTicket WHERE salesTicket.salesHistoryDate "
		   		+ "BETWEEN ? AND ? AND SalesTicket.customerID = "
		   		+ "(SELECT ID FROM Customer WHERE Customer.phoneNumber = ?))"
		   		+ " GROUP BY Name LIMIT 5";
	   try
	   {
		  Connection conn = Session.openDatabase();
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  //set parameters for first query
		  ps.setString(1, start);
		  ps.setString(2, end);
		  ps.setString(3, phone);
		  
		  //execute
		  ResultSet rs= ps.executeQuery();
		  
		  //process the result set
		  while(rs.next())
		  {
		     result.add(new Product(rs.getString(1), "", rs.getInt(2), 0));
		  }	  
		  
		  //close the connection
		  conn.close();
	   }
	   catch(Exception e)
	   {
		  e.printStackTrace();   
	   }
	   return result;
	}
	
	/*
	 * Display sales detail for customer
	 */
	public static void displayCustomerSalesDetails(String first, String last, int id, String phone, Date startDate, Date endDate)
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //root layout
	   BorderPane root = new BorderPane();
	   
	   //dates
	   String start = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
	   String end = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
	   
	   //format dates
	   String start2 = new SimpleDateFormat("MM-dd-yyyy").format(startDate);
	   String end2 = new SimpleDateFormat("MM-dd-yyyy").format(endDate);
	   
	   //top layout
	   Text title = new Text(Configs.getProperty("StoreName"));
	   Text subtitle1 = new Text("Store #" + Configs.getProperty("StoreNumber"));
	   Text subtitle2 = new Text("Customer " + first + " " + last);
	   Text subtitle3 = new Text(start2 + " -  " + end2);
	   
	   VBox top = new VBox();
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 10, 10));
	   top.setSpacing(7);
	   
	   //add nodes to top
	   top.getChildren().addAll(title, subtitle1, subtitle2, subtitle3);
	   
	   //observable list with details
	   String total = Reports.getCustomerSalesDetails(phone, start, end);
	   ObservableList<Product> products = FXCollections.observableArrayList();
	   
	   products = Reports.getCustomerProductDetails(phone, start, end);
	   
	   //left layout
	   Text salestxt = new Text("Sales Total for the period: ");
	   Text sales = new Text(total);
	   Text toptxt = new Text("Top 5 Products for the period");
	   
	   //top left
	   HBox topLeft = new HBox();
	   topLeft.setSpacing(10);
	   topLeft.getChildren().addAll(salestxt, sales);
	   
	   //product layout
	   VBox productLayout = new VBox();
	   productLayout.setSpacing(12);
	   
	   //add nodes to product layout
	   productLayout.getChildren().add(toptxt);
	   
	   //var
	   int i = 0;
	   
	   for(Product p : products)
	   {
	      if(p.getName() != null)
	      {
	    	 i++; 
	         productLayout.getChildren().add(new Text(i + ". " + p.getName() + " " + p.getQuantity()));	  
	      }	  
	   }	   
	   
	   //left layout
	   VBox left = new VBox();
	   left.setPadding(new Insets(10, 10, 10, 10));
	   left.setSpacing(7);
	   
	   //add nodes to left layout
	   left.getChildren().addAll(topLeft, productLayout);
	   
	   //right layout
	   ImageView imageView = new ImageView(Customers.loadCustomerPhoto(id));
	   imageView.setFitHeight(200);
	   imageView.setFitWidth(200);
	   
	   FlowPane right = new FlowPane();
	   right.setPadding(new Insets(10, 10, 10, 10));
	   right.setAlignment(Pos.CENTER);
	   
	   //add nodes to right
	   right.getChildren().add(imageView);
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(Reports.class.getResourceAsStream("/res/Apply.png"))));
	   
	   //bottom
	   HBox bottom = new HBox();
	   
	   //setup bottom
	   bottom.setSpacing(7);
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(accept);
	   
	   //set on action
	   accept.setOnAction(e -> stage.close());
	   
	   //setup root
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //add nodes to root
	   root.setTop(top);
	   root.setBottom(bottom);
	   root.setRight(right);
	   root.setLeft(left);
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setTitle("FASS Nova - Sales Detail For Customer");
	   stage.setMinWidth(300);
	   stage.centerOnScreen();
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
}
