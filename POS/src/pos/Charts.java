package pos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Charts {

	private static Logger logger = Logger.getLogger(Charts.class);
	
   /*
    * Display menu options	
    */
	public static void displayChartOptions()
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //radio button
	   RadioButton pie = new RadioButton();
	   RadioButton line = new RadioButton();
	   RadioButton monthly = new RadioButton();
	   RadioButton yearly = new RadioButton();
	   
	   //radio
	   ToggleGroup group = new ToggleGroup();
	   ToggleGroup group2 = new ToggleGroup();
	   line.setToggleGroup(group);
	   pie.setToggleGroup(group);
	   monthly.setToggleGroup(group2);
	   yearly.setToggleGroup(group2);
	   pie.setSelected(true);
	   monthly.setSelected(true);
	   
	   //labels
	   Label pielbl = new Label("Pie Chart");
	   //Label linelbl = new Label("Line Graph");
	   Label monthlylbl = new Label("Monthly");
	   Label yearlylbl = new Label("Yearly");
	   
	   //font
	   Font font = new Font("Courier Sans", 12);
	   
	   //setup labels
	   pielbl.setTextFill(Color.WHITE);
	   //linelbl.setTextFill(Color.WHITE);
	   monthlylbl.setTextFill(Color.WHITE);
	   yearlylbl.setTextFill(Color.WHITE);
	  
	   pielbl.setFont(font);
	   //linelbl.setFont(font);
	   monthlylbl.setFont(font);
	   yearlylbl.setFont(font);
	   
	   //layout
	   GridPane top = new GridPane();
	   top.setVgap(7);
	   top.setHgap(7);
	   top.setAlignment(Pos.CENTER);
	   top.setPadding(new Insets(10, 10, 20, 10));
	   
	   //add nodes to layout
	   top.add(pielbl, 0, 0);
	   top.add(pie, 1, 0);
	   //top.add(linelbl, 0, 1);
	   //top.add(line, 1, 1);
	   
	   //center
	   GridPane center = new GridPane();
	   center.setHgap(7);
	   center.setVgap(7);
	   center.setAlignment(Pos.CENTER);
	   
	   //add nodes to center
	   center.add(monthlylbl, 0, 0);
	   center.add(monthly, 1, 0);
	   center.add(yearlylbl, 0, 1);
	   center.add(yearly, 1, 1);
	   
	   //buttons
	   Button accept = new Button("Accept", new ImageView(new Image(Charts.class.getResourceAsStream("/res/Apply.png"))));
	   Button cancel = new Button("Cancel", new ImageView(new Image(Charts.class.getResourceAsStream("/res/Cancel.png"))));
	   
	   //set on action
	   cancel.setOnAction(e -> stage.close());
	   accept.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
		   
			//stage
			stage.close();
			
			//select chart
			if(monthly.isSelected())
			{
			   Charts.displayPieChart("month");	
			}	
			else
			{
			   Charts.displayPieChart("year");
			}	
		}
		   
	   });
	   
	   //bottom
	   HBox bottom = new HBox();
	   bottom.setAlignment(Pos.CENTER);
	   bottom.setPadding(new Insets(20, 20, 20, 20));
	   bottom.setSpacing(7);
	   
	   //add nodes to bottom
	   bottom.getChildren().addAll(cancel, accept);
	   
	   //root
	   BorderPane root = new BorderPane();
	   
	   //setup root
	   root.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to root
	   root.setTop(top);
	   root.setCenter(center);
	   root.setBottom(bottom);
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //set id
	   root.setId("border");
	   
	   //get style sheets
	   root.getStylesheets().add(Charts.class.getResource("MainScreen.css").toExternalForm());
	   
	   //title
	   stage.setTitle("FASS Nova - Graphs & Charts");	  
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(350);
	   stage.centerOnScreen();
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Display pie Chart
	 */
	public static void displayPieChart(String period)
	{
	   //stage
	   Stage stage = new Stage();
	   
	   Date date = new Date();
	   
	   //calendar
	   Calendar calendar = Calendar.getInstance();
	   String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	   //String year = calendar.getDisplayName(Calendar.YEAR, Calendar.LONG, Locale.getDefault());
	   //String day = calendar.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.LONG, Locale.getDefault());
	   calendar.add(Calendar.YEAR, -1);
	   
	   //monthly sales 
	   String currentMonth = new SimpleDateFormat("MM").format(date);
	   String currentYear = new SimpleDateFormat("yyyy").format(date);
	   String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
	   String previousYear = Integer.toString(calendar.get(Calendar.YEAR)) + "-" + currentMonth + "-"+ Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)); 
	   
	   
	   PieChart pie = new PieChart();
	   pie.setLegendSide(Side.LEFT);
	   pie.setClockwise(false);
	  
	   //observable list
	   ObservableList<Data> data = FXCollections.observableArrayList();
	   
	   if(period.equals("year"))
	   {

		   pie.setTitle(month + " " + calendar.get(Calendar.YEAR) + " - " + month + " " + currentYear + " " + " Sales");
		   
		   //get totals for each category
		   double booksTotal = Double.parseDouble(Charts.getCategoryYearlySalesTotal(previousYear, currentDate, "books"));
		   double electronicsTotal = Double.parseDouble(Charts.getCategoryYearlySalesTotal(previousYear, currentDate, "electronics"));
		   double foodTotal = Double.parseDouble(Charts.getCategoryYearlySalesTotal(previousYear, currentDate, "food"));
		   double drinksTotal = Double.parseDouble(Charts.getCategoryYearlySalesTotal(previousYear, currentDate, "drinks"));
		   double miscellaneousTotal = Double.parseDouble(Charts.getCategoryYearlySalesTotal(previousYear, currentDate, "miscellaneous"));
		   
		   //get grand total
		   double total = Charts.getYearlySales(previousYear, currentDate);
		   
		   //deduct taxes
		   total -= Receipt.setPrecision(total * (Double.parseDouble(Configs.getProperty("TaxRate"))/100));
		   
		   //add items
		   data.add(new PieChart.Data("Books", booksTotal/total));
		   data.add(new PieChart.Data("Electronics", electronicsTotal/total));
		   data.add(new PieChart.Data("Food", foodTotal/total));
		   data.add(new PieChart.Data("Drinks", drinksTotal/total));
		   data.add(new PieChart.Data("Miscellaneous", miscellaneousTotal/total));
	   }	
	   else
	   {
		   
		   pie.setTitle(month + " " + currentYear + " " + " Sales");
		   
		   //get totals for each category
		   double booksTotal = Double.parseDouble(Charts.getCategorySalesTotal(currentMonth, currentYear, "books"));
		   double electronicsTotal = Double.parseDouble(Charts.getCategorySalesTotal(currentMonth, currentYear, "electronics"));
		   double foodTotal = Double.parseDouble(Charts.getCategorySalesTotal(currentMonth, currentYear, "food"));
		   double drinksTotal = Double.parseDouble(Charts.getCategorySalesTotal(currentMonth, currentYear, "drinks"));
		   double miscellaneousTotal = Double.parseDouble(Charts.getCategorySalesTotal(currentMonth, currentYear, "miscellaneous"));
		   
		   //get grand total
		   double total = Charts.getMonthlySales(currentMonth, currentYear);
		   //deduct taxes
		   total = total - Receipt.setPrecision(total * (Double.parseDouble(Configs.getProperty("TaxRate"))/100));
		   
		   //add items
		   data.add(new PieChart.Data("Books", booksTotal/total));
		   data.add(new PieChart.Data("Electronics", electronicsTotal/total));
		   data.add(new PieChart.Data("Food", foodTotal/total));
		   data.add(new PieChart.Data("Drinks", drinksTotal/total));
		   data.add(new PieChart.Data("Miscellaneous", miscellaneousTotal/total));
	   }
	   
	   //set data
	   pie.setData(data);
	   
	   //root
	   FlowPane root = new FlowPane();
	   root.setAlignment(Pos.CENTER);
	   root.setPadding(new Insets(10, 10, 10, 10));
	   
	   //add nodes to children
	   root.getChildren().add(pie);
	   
	   //scene
	   Scene scene = new Scene(root);
	   
	   //set scene
	   stage.setScene(scene);
	   
	   //setup stage
	   stage.setTitle("FASS Nova - Pie Charts");
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setMinWidth(500);
	   stage.centerOnScreen();
	   
	   //show
	   stage.show();
	}
	
	/*
	 * Get data from the database
	 */
	private static double getMonthlySales(String month, String year)
	{
	   double result = 0.0;
	   String query = "SELECT Sum(Total) FROM salesTicket WHERE salesTicket.salesHistoryDate IN "
	   		          + "(SELECT salesDate FROM salesHistory WHERE salesHistory.registerSales IN"
	   		          + " (SELECT ID From Register WHERE Register.registerStoreCode = ?)) "
	   		          + "AND salesTicket.salesHistoryDate IN (SELECT salesHistoryDate FROM salesTicket WHERE"
	   		          + " Month(salesTicket.salesHistoryDate) = ? AND Year(salesTicket.salesHistoryDate) = ?) AND"
	   		          + " salesTicket.Status != 'Refunded'";
	   try
	   {
		   Connection conn = Session.openDatabase();
		   PreparedStatement ps = conn.prepareStatement(query);
		   
		   //set parameters
		   ps.setString(1, Configs.getProperty("StoreCode"));
		   ps.setString(2, month);
		   ps.setString(3, year);
		   
		   
		   //execute
		   ResultSet rs = ps.executeQuery();
		   
		   //process the result set
		   while(rs.next())
		   {
			  result = rs.getDouble(1) * -1;   
		   }	
		   
		   ps.close();
		   conn.close();
	   }
	   catch(Exception e)
	   {
	      logger.error("Could not get monthly sales", e); 	   
	   }
	   
	   return result;
	}
	
	/*
	 * Get sales per category
	 */
	private static String getCategorySalesTotal(String month, String year, String category)
	{
	   String result = "0.0";
	   String query1 = "SELECT Sum(Quantity), UnitPrice FROM Item WHERE Item.Name IN"
	   		+ " (SELECT Name FROM Product WHERE Product.Category = ? AND Product.productStoreCode = ?)"
	   		+ " and Item.salesTicketNoItem IN (SELECT ticketNo FROM salesTicket WHERE"
	   		+ " Month(salesTicket.salesHistoryDate) = ? AND Year(salesTicket.salesHistoryDate) = ?)";	   
	   try
	   {
	      Connection conn = Session.openDatabase();
	      PreparedStatement ps = conn.prepareStatement(query1);
	      
	      //set parameters
	      ps.setString(1, category);
	      ps.setString(2, Configs.getProperty("StoreCode"));
	      ps.setString(3, month);
	      ps.setString(4, year);
	      
	      //execute
	      ResultSet rs = ps.executeQuery();
	      
	      //process
	      while(rs.next())
	      {
	         result = Double.toString(Receipt.setPrecision(rs.getInt(1) * rs.getDouble(2)));
	      }	  
	      
	      //close
	      rs.close();
	      ps.close();
	      conn.close();
	   }
	   catch(Exception e)
	   {
		  logger.error("Could not get sales by category", e);   
	   }
	   
	   return result;
	}
	
	private static String getCategoryYearlySalesTotal(String start, String end, String category)
	{
	   String result = "0.0";
	   String query1 = "SELECT Sum(Quantity), UnitPrice FROM Item WHERE Item.Name IN"
	   		+ " (SELECT Name FROM Product WHERE Product.Category = ? AND Product.productStoreCode = ?)"
	   		+ " and Item.salesTicketNoItem IN (SELECT ticketNo FROM salesTicket WHERE"
	   		+ " salesTicket.salesHistoryDate  BETWEEN ? AND ?)";	   
	   try
	   {
	      Connection conn = Session.openDatabase();
	      PreparedStatement ps = conn.prepareStatement(query1);
	      
	      //set parameters
	      ps.setString(1, category);
	      ps.setString(2, Configs.getProperty("StoreCode"));
	      ps.setString(3, start);
	      ps.setString(4, end);
	      
	      //execute
	      ResultSet rs = ps.executeQuery();
	      
	      //process
	      while(rs.next())
	      {
	         result = Double.toString(Receipt.setPrecision(rs.getInt(1) * rs.getDouble(2)));
	      }	  
	      
	      //close
	      ps.close();
	      rs.close();
	      conn.close();
	   }
	   catch(Exception e)
	   {
		  logger.error("Error getting Category yearly sales", e);   
	   }
	   
	   return result;
	}
	
	/*
	 * Get data from the database
	 */
	private static double getYearlySales(String start, String end)
	{
	   double result = 0.0;
	   String query = "SELECT Sum(Total) FROM salesTicket WHERE salesTicket.salesHistoryDate IN "
	   		          + "(SELECT salesDate FROM salesHistory WHERE salesHistory.registerSales IN"
	   		          + " (SELECT ID From Register WHERE Register.registerStoreCode = ?)) "
	   		          + "AND salesTicket.salesHistoryDate IN (SELECT salesHistoryDate FROM salesTicket WHERE"
	   		          + " salesTicket.salesHistoryDate BETWEEN ? AND  ?) AND"
	   		          + " salesTicket.Status != 'Refunded'";
	   try
	   {
		   Connection conn = Session.openDatabase();
		   PreparedStatement ps = conn.prepareStatement(query);
		   
		   //set parameters
		   ps.setString(1, Configs.getProperty("StoreCode"));
		   ps.setString(2, start);
		   ps.setString(3, end);
		   
		   
		   //execute
		   ResultSet rs = ps.executeQuery();
		   
		   //process the result set
		   while(rs.next())
		   {
			  result = rs.getDouble(1);   
		   }	
		   
		   rs.close();
		   ps.close();
		   conn.close();
	   }
	   catch(Exception e)
	   {
	      logger.error("Could not get yearly sales", e);  	   
	   }
	   
	   return result;
	}
}
