package pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ActivityReport {
	
   private static Logger logger = Logger.getLogger(ActivityReport.class);	

   public static void displayActivityReport(Date start, Date end, String code, int register)
   {
	   //stage
	   Stage stage = createStage();
	   
	   //convert to string
	   String start1 = new SimpleDateFormat("yyyy-MM-dd").format(start);
	   String end1 = new SimpleDateFormat("yyyy-MM-dd").format(end);
	   
	   //scene
	   Scene scene = createScene(start1, end1, register, code);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	   
   }
   
   /*
    * Setup root layout
    */
   private static ScrollPane getRootLayout()
   {
      ScrollPane root = new ScrollPane();
      
      //set layout
      root.setPadding(new Insets(20, 20, 20, 20));
      root.setId("border");
      root.getStylesheets().add(ActivityReport.class.getResource("MainScreen.css").toExternalForm());
      
      return root;
   }
   
   /*
    * Setup stage
    * 
    */
   private static Stage createStage()
   {
	   Stage stage = new Stage();
	   
	   stage.setTitle("FASS Nova - Activity Report");
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.centerOnScreen();
	   stage.setResizable(false);
	   stage.setMinWidth(300);
	   stage.setMaxHeight(300);
	   return stage;
   }
   
   /*
    * Create the scene for the activity report
    */
   private static Scene createScene(String start, String end, int register, String code)
   {
	   //root layout
	   ScrollPane root = getRootLayout(); 
	   
	   //title section
	   Text title = new Text(Configs.getProperty("StoreName"));
	   Text subtitle1 = new Text("Store Number #" + Configs.getProperty("StoreNumber"));
	   Text subtitle2 = new Text("Activity Report");
	   Text subtitle3 = new Text(start + " - " +  end);
	   
	   //header layout
	   VBox header = new VBox();
	   header.setAlignment(Pos.CENTER);
	   header.setPadding(new Insets(10, 10, 10, 10));
	   header.setSpacing(7);
	   
	   //add nodes to header layout
	   header.getChildren().addAll(title, subtitle1, subtitle2, subtitle3);
	   
	   //list money wire transactions
	   ObservableList<MoneyWire> transactions = getMoneyWireTransactions(start, end);
	   
	   //money wires layout
	   GridPane moneyWires = new GridPane();
	   moneyWires.setVgap(7);
	   moneyWires.setHgap(7);
	   moneyWires.setPadding(new Insets(10, 10, 10, 10));
	   moneyWires.setAlignment(Pos.CENTER);
	   
	   //add title
	   moneyWires.add(new Text("Money Wire"), 1, 0);
	   
	   //counter
	   int count = 1;
	   
	   //add nodes to money wires layout
	   for(MoneyWire m : transactions)
	   {
		  if(m.getType().equals("send"))
		  {	  
	         moneyWires.add(new Text(m.getCompany()), 0, count);  
	         moneyWires.add(new Text(m.getWireNo()), 1, count);   
	         moneyWires.add(new Text(m.getAmount()), 2, count);   

	         count++;
		  }   
	   }	   
	   
	   //money wire totals
	   GridPane moneyWireTotals = getMoneyWireTotals(start, end, register, code);
	   
	   //category section
	   GridPane categorySummary = createCategorySection(start, end, code, register);
	   
	   //payment methods
	   GridPane paymentSummary = Reports.paymentMethodsSection(start, end, code, register);
	   
	   //cash withdrawals and deposits
	   VBox cashWithdrawalsSection = Reports.displayCashWD(start, end, code, register);
	   
	   //revenue section
	   VBox revenueSection = getRevenueSection(Configs.getTempValue("salesTemp"));

	   //reset
	   Configs.saveTempValue("salesTemp", "0");
	   
	   //layout
	   VBox layout = new VBox();
	   layout.setAlignment(Pos.CENTER);
	   layout.setSpacing(7);
	   layout.setPadding(new Insets(20, 20, 20, 20));
	   
	   //add nodes to container
	   layout.getChildren().addAll(header, moneyWires, moneyWireTotals, categorySummary, paymentSummary, cashWithdrawalsSection, revenueSection);
	   
	   //add nodes to root
	   root.setContent(layout);
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   return scene;
	   
   }
   
   /*
    * Get money wire totals
    * 
    */
   private static GridPane getMoneyWireTotals(String start, String end, int register, String code)
   {
	   ObservableList<String> companies = MoneyWire.getCompanyNames();
	   GridPane result = new GridPane();
	   result.setVgap(7);
	   result.setHgap(7);
	   result.setAlignment(Pos.CENTER);
	   
	   //add title
	   result.add(new Text("Company Totals"), 1, 0);
	   
	   int count = 1;
	   
	   for(String s : companies)
	   {
		  String total = Reports.getMoneyWireTotals(s, start, end, code, register);
		  
		  logger.info("Got money wire totals");
		  
		  if(total == null)
		  {
		     total = "0";	  
		  }	  
		  
		  Text t = new Text(s); 
		  result.add(t, 0, count);
		  result.add(new Text(total), 2, count);
		  
		  count++;
	   }	   
	   
	   return result;
   }
   
   /*
    * List Money Wire Transactions
    */
   private static ObservableList<MoneyWire> getMoneyWireTransactions(String start, String end)
   {
	  ObservableList<MoneyWire> result = FXCollections.observableArrayList();
	  String query = "CALL listMoneyWires(?,?,?,?)";
	  
	  
	  try
	  {
	     java.sql.Connection conn = Session.openDatabase();
	     PreparedStatement ps = conn.prepareStatement(query);
	     
	     //set parameters
	     ps.setString(1, start);
	     ps.setString(2, end);
	     ps.setInt(3, Integer.parseInt(Configs.getProperty("Register")));
	     ps.setString(4, Configs.getProperty("StoreCode"));
	     
	     //execute
	     ResultSet rs = ps.executeQuery();
	     
	     //process the result set
	     while(rs.next())
	     {
	        result.add(new MoneyWire(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));	 
	     }	 
	     
	     //close
	     rs.close();
	     ps.close();
	     conn.close();
	  }
	  catch(Exception e)
	  {
	     logger.error("Money wire transactions were not retrieved", e);	  
	  }
	  
	  return result;
   }
   
   /*
    * Category section
    */
   private static GridPane createCategorySection(String start, String end, String code, int register)
   {
	   //title
	   Text title = new Text("Sales per Category");
	   
	   //categories section
	   Text bookslbl = new Text("Books");
	   Text foodlbl = new Text("Food");
	   Text electronicslbl = new Text("Electronics");
	   Text drinkslbl = new Text("Drinks");
	   Text miscellaneouslbl = new Text("Miscellaneous");
	   Text category = new Text("Category");
	   Text totallbl = new Text("Total");
	   
	   //get values of categories
	   Text books = new Text(Reports.getCategoryAmount("Books", start, end, code, register));
	   Text food = new Text(Reports.getCategoryAmount("food", start, end, code, register));
	   Text electronics = new Text(Reports.getCategoryAmount("Electronics", start, end, code, register));
	   Text drinks = new Text(Reports.getCategoryAmount("drinks", start, end, code, register));
	   Text miscellaneous = new Text(Reports.getCategoryAmount("miscellaneous", start, end, code, register));
	   Text salesTotal = new Text (Double.toString(Reports.getSalesTotal(Double.parseDouble(books.getText()), Double.parseDouble(food.getText()), Double.parseDouble(electronics.getText()), 
			                             Double.parseDouble(drinks.getText()), Double.parseDouble(miscellaneous.getText()))));
	   
	   
	   //gridpane of categories
	   GridPane categorySummary = new GridPane();
	   categorySummary.setHgap(7);
	   categorySummary.setVgap(7);
	   categorySummary.setAlignment(Pos.CENTER);
	   categorySummary.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes
	   categorySummary.add(title, 1, 0);
	   categorySummary.add(foodlbl, 0, 1);
	   categorySummary.add(food, 2, 1);
	   categorySummary.add(drinkslbl, 0, 2);
	   categorySummary.add(drinks, 2, 2);
	   categorySummary.add(bookslbl, 0, 3);
	   categorySummary.add(books, 2, 3);
	   categorySummary.add(electronicslbl, 0, 4);
	   categorySummary.add(electronics, 2, 4);
	   categorySummary.add(miscellaneouslbl, 0, 5);
	   categorySummary.add(miscellaneous, 2, 5);  
	   categorySummary.add(totallbl, 0, 6);
	   categorySummary.add(salesTotal, 1, 6);
	   
	   //category layout
	   VBox categoryLayout = new VBox();
	   categoryLayout.setAlignment(Pos.CENTER);
	   categoryLayout.setSpacing(7);
	   categoryLayout.setPadding(new Insets(10, 10, 10, 10));
	   
	   //save temp value
	   Configs.saveTempValue("salesTemp", salesTotal.getText());
	   
	   //add nodes to category layout
	   categoryLayout.getChildren().addAll(category, categorySummary);	   
	   
	   return categorySummary;
   }
   
   /*
    * Revenue and tax section
    */
   private static VBox getRevenueSection(String salesTotal)
   {
	   //calculated total
	   Text taxlbl = new Text("Calculated Tax");
	   Text tax = new Text("0.00");
	   
	   //get calculated total tax
	   tax.setText(Double.toString(Receipt.setPrecision(Double.parseDouble(salesTotal) * (Double.parseDouble(Configs.getProperty("TaxRate")) / 100))));
	   
	   // tax layout
	   HBox taxLayout = new HBox();
	   taxLayout.setSpacing(7);
	   taxLayout.setPadding(new Insets(10, 10, 10, 10));
	   taxLayout.setAlignment(Pos.CENTER);
	   
	   //add nodes to tax layout
	   taxLayout.getChildren().addAll(taxlbl, tax);
	   
	   //get total revenue for the period
	   Text revenuelbl = new Text("Revenue for the period: ");
	   Text revenue = new Text(Double.toString(Receipt.setPrecision(Double.parseDouble(salesTotal) + Double.parseDouble(tax.getText()))));
	   
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
	   
	   //layout
	   HBox cashLayout = new HBox();
	   cashLayout.setSpacing(7);
	   cashLayout.setAlignment(Pos.CENTER);
	   
	   //add nodes to children
	   cashLayout.getChildren().addAll(revenueLayout, cashlbl, cash);
	   
	   //root
	   VBox root = new VBox();
	   root.setAlignment(Pos.CENTER);
	   root.setSpacing(7);
	   
	   root.getChildren().addAll(revenueLayout, taxLayout, cashLayout);
	   
	   return root;
   }
   
   /*
    *  Display audits
    */
   public static void displayAudits()
   {
      //stage
	  Stage stage = new Stage();
	  
	  //date pickers
	  DatePicker start = new DatePicker();
	  DatePicker end = new DatePicker();
	  
	  //set default values
	  start.setValue(LocalDate.now());
	  end.setValue(LocalDate.now());
	  
	  //table
	  TableView<Audit> table = ActivityReport.createTable();

	  //date
	  Date date = new Date();
	  
	  //convert
	  String start2 = new SimpleDateFormat("yyy-MM-dd").format(date);
	  String end2 = new SimpleDateFormat("yyyy-MM-dd").format(date);
		
	  //get audits
	  ObservableList<Audit> audits = getAudits(start2, end2);
	   
	  //set items
	  table.setItems(audits);
	  
	  //buttons
	  Button search = new Button("Search", new ImageView(new Image(ActivityReport.class.getResourceAsStream("/res/search2.png"))));
	  Button done = new Button("Done", new ImageView(new Image(ActivityReport.class.getResourceAsStream("/res/Apply.png"))));
	  
	  //set on action
	  done.setOnAction(e -> stage.close());
	  search.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
	       
			if(start.getValue().compareTo(end.getValue()) <= 0)
			{
			   //get local date 
			   LocalDate localDate1 = start.getValue();
			   LocalDate localDate2 = end.getValue();	
				
			   //convert to java.utl.Date
			   Date date1 = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
			   Date date2 = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
				
			   //convert
			   String start2 = new SimpleDateFormat("yyy-MM-dd").format(date1);
			   String end2 = new SimpleDateFormat("yyyy-MM-dd").format(date2);
				
			   //get audits
			   ObservableList<Audit> items = getAudits(start2, end2);
			   
			   //set items
			   table.setItems(items);
			   
			   //refresh
			   table.refresh();	
			}
			else
			{
			   AlertBox.display("FASS Nova", "Could not validate input");	
			}	
		}
	     	  
	  });
	  
	  //labels
	  Label startlbl = new Label("Start");
	  Label endlbl = new Label("End");
	  
	  //set color
	  startlbl.setTextFill(Color.WHITE);
	  endlbl.setTextFill(Color.WHITE);
	  
	  //top
	  HBox top = new HBox();
	  top.setAlignment(Pos.CENTER);
	  top.setSpacing(10);
	  top.setPadding(new Insets(20, 20, 20, 10));
	  
	  //add nodes to top
	  top.getChildren().addAll(startlbl, start, endlbl, end, search);
	  
	  //bottom
	  FlowPane bottom = new FlowPane();
	  bottom.setAlignment(Pos.CENTER);
	  bottom.setPadding(new Insets(20, 20, 20, 20));
	  
	  //add nodes to bottom
	  bottom.getChildren().add(done);
	  
	  //root
	  BorderPane root = new BorderPane();
	  
	  //add nodes to root
	  root.setTop(top);
	  root.setCenter(table);
	  root.setBottom(bottom);
	  
	  //set id 
	  root.setId("border");
	  
	  //style sheets
	  root.getStylesheets().add(ActivityReport.class.getResource("MainScreen.css").toExternalForm());
	  
	  //setup stage
	  stage.setTitle("FASS Nova - List of Audits");
	  stage.initModality(Modality.APPLICATION_MODAL);
	  stage.centerOnScreen();
	  stage.setMinWidth(600);
	  stage.setMaxHeight(370);
	  
	  //scene
	  Scene scene = new Scene(root);
	  
	  //set scene
	  stage.setScene(scene);
	  
	  //show 
	  stage.show();
   }
   
   /*
    * Get audits from the database
    */
   private static ObservableList<Audit> getAudits(String start, String end)
   {
      ObservableList<Audit> result = FXCollections.observableArrayList();	   
      String query = "CALL listAudits(?,?,?,?)";
      
      try
      {
         java.sql.Connection conn = Session.openDatabase();
         PreparedStatement ps = conn.prepareStatement(query);
         
         //set parameters
         ps.setString(1, start);
         ps.setString(2, end);
         ps.setInt(3, Integer.parseInt(Configs.getProperty("Register")));
         ps.setString(4, Configs.getProperty("StoreCode"));
         
         //execute
         ResultSet rs = ps.executeQuery();
         
         //process
         while(rs.next())
         {
            String expected = Double.toString(Receipt.setPrecision(rs.getDouble(2) + (Math.sqrt(rs.getDouble(4) * rs.getDouble(4)))));	 
            result.add(new Audit(rs.getString(1), expected, rs.getString(3), rs.getString(4), rs.getString(5)));	 
         }
         
         //close
         rs.close();
         ps.close();
         conn.close();
      }
      catch(Exception e)
      {
         logger.error("Could not get audits", e);	  
      }
      
      return result;
   }
   
   /*
    * Construct table view for audits
    */
   @SuppressWarnings("unchecked")
   private static TableView<Audit> createTable()
   {
      TableView<Audit> table = new TableView<Audit>();
	  table.setEditable(false);
		
	  //create columns
	  TableColumn<Audit, String> auditDate = new TableColumn<Audit, String>("Date");
	  auditDate.setCellValueFactory(new PropertyValueFactory<>("auditDate"));
		
	  TableColumn<Audit, String> expectedCash = new TableColumn<Audit, String>("Expected Cash");
	  expectedCash.setCellValueFactory(new PropertyValueFactory<>("expectedCash"));
		
	  TableColumn<Audit, String> actualCash = new TableColumn <Audit, String>("Actual Cash");
	  actualCash.setCellValueFactory(new PropertyValueFactory<>("actualCash"));
		
	  TableColumn<Audit, String> difference = new TableColumn<Audit, String>("Difference");
	  difference.setCellValueFactory(new PropertyValueFactory<>("difference"));
		
	  TableColumn<Audit, String> user = new TableColumn<Audit, String>("User");
	  user.setCellValueFactory(new PropertyValueFactory<>("user"));	
		
	  //set sizes
	  auditDate.setPrefWidth(90);
	  expectedCash.setPrefWidth(100);
	  actualCash.setPrefWidth(100);
	  difference.setPrefWidth(100);
	  user.setPrefWidth(80);
		
	  //add columns to the table view
      table.getColumns().addAll(auditDate, expectedCash, actualCash, difference, user);	
      
      return table;
   }
}
