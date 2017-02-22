package pos;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Audit {

	private static StringProperty property;
	private static Label total;
	private static double[] totalArray = new double[15];
	
	private static NumericTextField hundred;
	private static NumericTextField fifty;
	private static NumericTextField twenty;
	private static NumericTextField five;
	private static NumericTextField one;
	private static NumericTextField ten;
	/*
	 * Display the audit cash screen 1
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	public static void displayAuditBills()
	{ 
	   Stage stage = new Stage();
	   
	   //root layout
	   BorderPane root = new BorderPane();
	   
	   //title
	   Text title = new Text("Type in the number of bills for each category");
	   title.setFont(new Font("Courier Sans", 18));
	   title.setFill(Color.WHITE);
	   
	   //top layout
	   FlowPane top = new FlowPane();
	   top.setAlignment(Pos.TOP_CENTER);
	   top.getChildren().add(title);
	   
	   //images for labels
	   Image image1 = new Image(Audit.class.getResourceAsStream("/res/100bill.jpg"));
	   Image image2 = new Image(Audit.class.getResourceAsStream("/res/50bill.jpg"));
	   Image image3 = new Image(Audit.class.getResourceAsStream("/res/20bill.jpg"));
	   Image image4 = new Image(Audit.class.getResourceAsStream("/res/10bill.jpg"));
	   Image image5 = new Image(Audit.class.getResourceAsStream("/res/5bill.jpg"));
	   Image image6 = new Image(Audit.class.getResourceAsStream("/res/1bill.jpg"));
	   
	   //imageview
	   ImageView hundredimg = new ImageView(image1);
	   ImageView fiftyimg = new ImageView(image2);
	   ImageView twentyimg = new ImageView(image3);
	   ImageView tenimg = new ImageView(image4);
	   ImageView fiveimg = new ImageView(image5);
	   ImageView oneimg = new ImageView(image6);	   
	   
	   //set widths and heights
	   hundredimg.setFitWidth(150);
	   hundredimg.setFitHeight(60);
	   
	   fiftyimg.setFitWidth(150);
	   fiftyimg.setFitHeight(60);
	   
	   twentyimg.setFitWidth(150);
	   twentyimg.setFitHeight(60);
	   
	   tenimg.setFitWidth(150);
	   tenimg.setFitHeight(60);
	   
	   fiveimg.setFitWidth(150);
	   fiveimg.setFitHeight(60);
	   
	   oneimg.setFitWidth(150);
	   oneimg.setFitHeight(60);
	   
	   //grid pane
	   GridPane center = new GridPane();
	   center.setAlignment(Pos.CENTER);
	   center.setPadding(new Insets(10, 10, 10, 20));
	   center.setVgap(7);
	   center.setHgap(10);
	   
	   //initialize numeric fields
	   hundred = new NumericTextField();
	   fifty = new NumericTextField();
	   twenty = new NumericTextField();
	   ten = new NumericTextField();
	   five = new NumericTextField();
	   one = new NumericTextField();	   	   
	   	   
	   //Labels
	   Label hundredlbl = new Label("$0");
	   Label fiftylbl = new Label("$0");
	   Label twentylbl = new Label("$0");
	   Label tenlbl = new Label("$0");
	   Label fivelbl = new Label("$0");
	   Label onelbl = new Label("$0");
	   Label totallbl = new Label("Total");
	   total = new Label("0.00");
	   
	   //format labels
	   hundredlbl.setTextFill(Color.WHITE);
	   fiftylbl.setTextFill(Color.WHITE);
	   twentylbl.setTextFill(Color.WHITE);
	   tenlbl.setTextFill(Color.WHITE);
	   fivelbl.setTextFill(Color.WHITE);
	   onelbl.setTextFill(Color.WHITE);
	   totallbl.setTextFill(Color.WHITE);
	   totallbl.setFont(new Font("Courier Sans", 14));
	   total.setTextFill(Color.WHITE);
	   total.setFont(new Font("Courier Sans", 14));
	   
	   //set on action
	   one.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty(computeSubTotal(one.getText(), 1, true));
		   onelbl.textProperty().unbind();
		   onelbl.textProperty().bind(property);
		   
		   //update the array at position 0 if not empty;
		   if(!one.getText().isEmpty() && one.getText() != null)
		   {	   
		     totalArray[0] = Double.parseDouble(one.getText());
		   }
		   else
		   { 
			  totalArray[0] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();
		   
		} });
	   five.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty(computeSubTotal(five.getText(), 5, true));
		   fivelbl.textProperty().unbind();
		   fivelbl.textProperty().bind(property);

		   //update the array at position 0 if not empty;
		   if(!five.getText().isEmpty() || five.getText() != null)
		   {	   
		     totalArray[1] = Double.parseDouble(fivelbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[1] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();
		   
		} });
	   
	   ten.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty(computeSubTotal(ten.getText(), 10, true));
		   tenlbl.textProperty().unbind();
		   tenlbl.textProperty().bind(property);
		   
		   //update the array at position 0 if not empty;
		   if(!ten.getText().isEmpty() || ten.getText() != null)
		   {	   
		     totalArray[2] = Double.parseDouble(tenlbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[2] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();		   
		} });
	   twenty.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty(computeSubTotal(twenty.getText(), 20, true));
		   twentylbl.textProperty().unbind();
		   twentylbl.textProperty().bind(property);
		   
		   //update the array at index 3 if not empty;
		   if(!twenty.getText().isEmpty() || twenty.getText() != null)
		   {	   
		     totalArray[3] = Double.parseDouble(twentylbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[3] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();
		   
		} });	   
	    fifty.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty(computeSubTotal(fifty.getText(), 50, true));
		   fiftylbl.textProperty().unbind();
		   fiftylbl.textProperty().bind(property);
		   
		   //update the array at position 4 if not empty;
		   if(!fifty.getText().isEmpty() || fifty.getText() != null)
		   {	   
		     totalArray[4] = Double.parseDouble(fiftylbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[4] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();		   
		   
		} });
	   
	    hundred.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty(computeSubTotal(hundred.getText(), 100, true));
		   hundredlbl.textProperty().unbind();
		   hundredlbl.textProperty().bind(property);
		   
		   //update the array at position 5 if not empty;
		   if(!hundred.getText().isEmpty() || hundred.getText() != null)
		   {	   
		     totalArray[5] = Double.parseDouble(hundredlbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[5] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();		   
		   
		} });	  
	    
	   //add nodes to center
	   center.add(hundredimg, 0, 0);
	   center.add(hundred, 1, 0);
	   center.add(hundredlbl, 2, 0);
	   center.add(fiftyimg, 0, 1);
	   center.add(fifty, 1, 1);
	   center.add(fiftylbl, 2, 1);
	   center.add(twentyimg, 0, 2);
	   center.add(twenty, 1, 2);
	   center.add(twentylbl, 2, 2);
	   center.add(tenimg, 0, 3);
	   center.add(ten, 1, 3);
	   center.add(tenlbl, 2, 3);
	   center.add(fiveimg, 0, 4);
	   center.add(five, 1, 4);
	   center.add(fivelbl, 2, 4);
	   center.add(oneimg, 0, 5);
	   center.add(one, 1, 5);
	   center.add(onelbl, 2, 5);
	   center.add(totallbl, 0, 6);
	   center.add(total, 1, 6);
	   
	   //buttons
	   Button next = new Button("Next", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Go forward.png"))));
	   Button cancel = new Button("Cancel", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Cancel.png"))));
	   
	   //implement actions
	   cancel.setOnAction( new EventHandler<ActionEvent>() {

		  @Override
		  public void handle(ActionEvent event) {
      	     
			  //close the stage
			  stage.close();
			  
			  //reset temp list
			  Configs.saveTempValue("temp1", "0");
			  Configs.saveTempValue("temp2", "0");
			  Configs.saveTempValue("temp3", "0");
			  Configs.saveTempValue("temp4", "0");
			  Configs.saveTempValue("temp5", "0");
			  Configs.saveTempValue("temp6", "0");
			  Configs.saveTempValue("temp7", "0");
			  Configs.saveTempValue("temp8", "0");
			  Configs.saveTempValue("temp9", "0");
			  Configs.saveTempValue("temp10", "0");
			  Configs.saveTempValue("temp11", "0");
			  Configs.saveTempValue("temp12", "0");			  
		  } 
		   
	   });
	   next.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
           
			//close the stage
			stage.close();
			
			//save properties
			initializeFields(1, one.getText(), five.getText(), ten.getText(), twenty.getText(), fifty.getText(), hundred.getText());
			
			//go to the next screen
			auditWrappedBills();
		}
		   
	   } );
	   
	   if(totalArray.length > 0)
	   {	   
	      //set values to text fields
		   hundred.setText(Configs.getTempValue("temp6"));
		   fifty.setText(Configs.getTempValue("temp5"));
		   twenty.setText(Configs.getTempValue("temp4"));
		   ten.setText(Configs.getTempValue("temp3"));
		   five.setText(Configs.getTempValue("temp2"));
		   one.setText(Configs.getTempValue("temp1"));
	   }	   
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setSpacing(7);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.setAlignment(Pos.CENTER);
	   
	   //add buttons to bottom layout
	   bottom.getChildren().addAll(cancel, next);
	   
	   //setup root
	   root.setTop(top);
	   root.setCenter(center);
	   root.setBottom(bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheet
	   root.getStylesheets().add(Audit.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.centerOnScreen();
	   stage.setMinWidth(550);
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setTitle("FASS Nova - Audit Cash");
	   stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

		@Override
		public void handle(WindowEvent event) {
           
			//reset temp file   			
			Configs.saveTempValue("temp1", "0");
			Configs.saveTempValue("temp2", "0");
			Configs.saveTempValue("temp3", "0");
			Configs.saveTempValue("temp4", "0");
			Configs.saveTempValue("temp5", "0");
			Configs.saveTempValue("temp6", "0");
			
			stage.hide();
		}
		   
	   });
	   
	   //show the stage
	   stage.setScene(scene);
	   stage.show();	   
	}
	
	/*
	 * Compute the total 
	 * @return A string representing the total amount
	 */
	private static String computeSubTotal(String s, int bill, boolean isInteger)
	{ 
	   if(s == null || s.isEmpty()) 
	   { 
	      return "$0";	   
	   }	   
	   else if(isInteger)
	   { 
	      int amount = bill * Integer.parseInt(s);
	      return "$" + Integer.toString(amount);
	   }	
	   else
	   {
		   double amount = bill * Double.parseDouble(s);
		   return "$" + Double.toString(amount);
	   }	   
	}
	
	/*
	 * Compute the total
	 * @param s String representing any sub total
	 * @return a string representing the total
	 */
	private static void computeTotal()
	{	
	   double result = 0.0;
	   for(int i = 0; i < totalArray.length; i++)
	   {
	      result += totalArray[i];	     
	   }	   
	   property = new SimpleStringProperty(Double.toString(Receipt.setPrecision(result)));
	   total.textProperty().unbind();
	   total.textProperty().bind(property);	   
	}
	
	/*
	 * Count the number of wrapped bills
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void auditWrappedBills()
	{ 			
	
	   Stage stage = new Stage();
	   	   
	   //root layout
	   BorderPane root = new BorderPane();
	   
	   //title
	   Text title = new Text("Type in the total amount of wrapped bills for each category");
	   title.setFont(new Font("Courier Sans", 18));
	   title.setFill(Color.WHITE);
	   
	   //top layout
	   FlowPane top = new FlowPane();
	   top.setAlignment(Pos.TOP_CENTER);
	   top.getChildren().add(title);
	   
	   //images for labels
	   Image image1 = new Image(Audit.class.getResourceAsStream("/res/100bill.jpg"));
	   Image image2 = new Image(Audit.class.getResourceAsStream("/res/50bill.jpg"));
	   Image image3 = new Image(Audit.class.getResourceAsStream("/res/20bill.jpg"));
	   Image image4 = new Image(Audit.class.getResourceAsStream("/res/10bill.jpg"));
	   Image image5 = new Image(Audit.class.getResourceAsStream("/res/5bill.jpg"));
	   Image image6 = new Image(Audit.class.getResourceAsStream("/res/1bill.jpg"));
	   
	   //imageview
	   ImageView hundredimg = new ImageView(image1);
	   ImageView fiftyimg = new ImageView(image2);
	   ImageView twentyimg = new ImageView(image3);
	   ImageView tenimg = new ImageView(image4);
	   ImageView fiveimg = new ImageView(image5);
	   ImageView oneimg = new ImageView(image6);	   
	   
	   //set widths and heights
	   hundredimg.setFitWidth(150);
	   hundredimg.setFitHeight(60);
	   
	   fiftyimg.setFitWidth(150);
	   fiftyimg.setFitHeight(60);
	   
	   twentyimg.setFitWidth(150);
	   twentyimg.setFitHeight(60);
	   
	   tenimg.setFitWidth(150);
	   tenimg.setFitHeight(60);
	   
	   fiveimg.setFitWidth(150);
	   fiveimg.setFitHeight(60);
	   
	   oneimg.setFitWidth(150);
	   oneimg.setFitHeight(60);
	   
	   //grid pane
	   GridPane center = new GridPane();
	   center.setAlignment(Pos.CENTER);
	   center.setPadding(new Insets(10, 10, 10, 20));
	   center.setVgap(7);
	   center.setHgap(10);
	   
	   //initialize numeric fields
	   hundred = new NumericTextField();
	   fifty = new NumericTextField();
	   twenty = new NumericTextField();
	   ten = new NumericTextField();
	   five = new NumericTextField();
	   one = new NumericTextField(); 	   	   
	   	   
	   //Labels
	   Label hundredlbl = new Label("$0");
	   Label fiftylbl = new Label("$0");
	   Label twentylbl = new Label("$0");
	   Label tenlbl = new Label("$0");
	   Label fivelbl = new Label("$0");
	   Label onelbl = new Label("$0");
	   Label totallbl = new Label("Total");
	   //total = new Label("0.00");	   
	   
	   //format labels
	   hundredlbl.setTextFill(Color.WHITE);
	   fiftylbl.setTextFill(Color.WHITE);
	   twentylbl.setTextFill(Color.WHITE);
	   tenlbl.setTextFill(Color.WHITE);
	   fivelbl.setTextFill(Color.WHITE);
	   onelbl.setTextFill(Color.WHITE);
	   totallbl.setTextFill(Color.WHITE);
	   totallbl.setFont(new Font("Courier Sans", 14));
	   total.setTextFill(Color.WHITE);
	   total.setFont(new Font("Courier Sans", 14));
	   
	   //set on action
	   one.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty("$"+one.getText());
		   onelbl.textProperty().unbind();
		   onelbl.textProperty().bind(property);
		   
		   //update the array at position 0 if not empty;
		   if(!one.getText().isEmpty() && one.getText() != null)
		   {	   
		     totalArray[6] = Double.parseDouble(one.getText());
		   }
		   else
		   { 
			  totalArray[6] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();
		   
		} });
	   five.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty("$"+five.getText());
		   fivelbl.textProperty().unbind();
		   fivelbl.textProperty().bind(property);

		   //update the array at position 0 if not empty;
		   if(!five.getText().isEmpty() && five.getText() != null)
		   {	   
		     totalArray[7] = Double.parseDouble(fivelbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[7] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();
		   
		} });
	   
	   ten.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty("$"+ten.getText());
		   tenlbl.textProperty().unbind();
		   tenlbl.textProperty().bind(property);
		   
		   //update the array at position 0 if not empty;
		   if(!ten.getText().isEmpty() && ten.getText() != null)
		   {	   
		     totalArray[8] = Double.parseDouble(tenlbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[8] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();		   
		} });
	   twenty.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty("$"+twenty.getText());
		   twentylbl.textProperty().unbind();
		   twentylbl.textProperty().bind(property);
		   
		   //update the array at index 3 if not empty;
		   if(!twenty.getText().isEmpty() && twenty.getText() != null)
		   {	   
		     totalArray[9] = Double.parseDouble(twentylbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[9] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();
		   
		} });	   
	    fifty.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty("$"+fifty.getText());
		   fiftylbl.textProperty().unbind();
		   fiftylbl.textProperty().bind(property);
		   
		   //update the array at position 10 if not empty;
		   if(!fifty.getText().isEmpty() && fifty.getText() != null)
		   {	   
		     totalArray[10] = Double.parseDouble(fiftylbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[10] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();		   
		   
		} });
	   
	    hundred.textProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		   
		   //update label	
		   property = new SimpleStringProperty("$" + hundred.getText());
		   hundredlbl.textProperty().unbind();
		   hundredlbl.textProperty().bind(property);
		   
		   //update the array at position 11 if not empty;
		   if(!hundred.getText().isEmpty() && hundred.getText() != null)
		   {	   
		      totalArray[11] = Double.parseDouble(hundredlbl.getText().substring(1));
		   }
		   else
		   { 
			  totalArray[11] = 0;   
		   }	   
		   
		   //update the amount
		   computeTotal();		   
		   
		} });	  
	    
	   //add nodes to center
	   center.add(hundredimg, 0, 0);
	   center.add(hundred, 1, 0);
	   center.add(hundredlbl, 2, 0);
	   center.add(fiftyimg, 0, 1);
	   center.add(fifty, 1, 1);
	   center.add(fiftylbl, 2, 1);
	   center.add(twentyimg, 0, 2);
	   center.add(twenty, 1, 2);
	   center.add(twentylbl, 2, 2);
	   center.add(tenimg, 0, 3);
	   center.add(ten, 1, 3);
	   center.add(tenlbl, 2, 3);
	   center.add(fiveimg, 0, 4);
	   center.add(five, 1, 4);
	   center.add(fivelbl, 2, 4);
	   center.add(oneimg, 0, 5);
	   center.add(one, 1, 5);
	   center.add(onelbl, 2, 5);
	   center.add(totallbl, 0, 6);
	   center.add(total, 1, 6);
	   
	   //buttons
	   Button next = new Button("Next", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Go forward.png"))));
	   Button previous = new Button("Previous", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Go back.png"))));
	   
	   //implement actions
	   previous.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
        
			//close the stage
			stage.close();
			
			//save properties
			initializeFields(2, one.getText(), five.getText(), ten.getText(), twenty.getText(), fifty.getText(), hundred.getText());
			
			//go to the next screen
			displayAuditBills();
		}
		   
	   } );
	   next.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
        
			//close the stage
			stage.close();
			  
		    //save properties
		    initializeFields(2, one.getText(), five.getText(), ten.getText(), twenty.getText(), fifty.getText(), hundred.getText());
		  
		    //go to next stage
		    
		 }
		   
	   } );
	   
	   if(totalArray.length > 5)
	   {	   
	      //set values to text fields
		   hundred.setText(Configs.getTempValue("temp12"));
		   fifty.setText(Configs.getTempValue("temp11"));
		   twenty.setText(Configs.getTempValue("temp10"));
		   ten.setText(Configs.getTempValue("temp9"));
		   five.setText(Configs.getTempValue("temp8"));
		   one.setText(Configs.getTempValue("temp7"));
	   }
	   
	   //bottom layout
	   HBox bottom = new HBox();
	   bottom.setSpacing(7);
	   bottom.setPadding(new Insets(10, 10, 10, 10));
	   bottom.setAlignment(Pos.CENTER);
	   
	   //add buttons to bottom layout
	   bottom.getChildren().addAll(previous, next);
	   
	   //setup root
	   root.setTop(top);
	   root.setCenter(center);
	   root.setBottom(bottom);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheet
	   root.getStylesheets().add(Audit.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup scene
	   Scene scene = new Scene(root);
	   
	   //setup stage
	   stage.centerOnScreen();
	   stage.setMinWidth(550);
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setTitle("FASS Nova - Audit Cash");
	   stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

		@Override
		public void handle(WindowEvent event) {
           
			//reset temp file   			
			Configs.saveTempValue("temp1", "0");
			Configs.saveTempValue("temp2", "0");
			Configs.saveTempValue("temp3", "0");
			Configs.saveTempValue("temp4", "0");
			Configs.saveTempValue("temp5", "0");
			Configs.saveTempValue("temp6", "0");
			Configs.saveTempValue("temp7", "0");
			Configs.saveTempValue("temp8", "0");
			Configs.saveTempValue("temp9", "0");
			Configs.saveTempValue("temp10", "0");
			Configs.saveTempValue("temp11", "0");
			Configs.saveTempValue("temp12", "0");
			
			stage.hide();
		}
		   
	   });	   
	   
	   //show the stage
	   stage.setScene(scene);
	   stage.show();
	} 
	
	/*
	 *  Initialize values of text fields (bills)
	 */
	private static void initializeFields(int caller, String s1, String s2, String s3, String s4, String s5, String s6)
	{
		if(caller == 1)
		{	
		   //save temporary values
		   Configs.saveTempValue("temp1", s1);
		   Configs.saveTempValue("temp2", s2);
		   Configs.saveTempValue("temp3", s3);
		   Configs.saveTempValue("temp4", s4);
		   Configs.saveTempValue("temp5", s5);
		   Configs.saveTempValue("temp6", s6);
		}
		else if(caller == 2)
		{ 
			//save temporary values
			Configs.saveTempValue("temp7", s1);
			Configs.saveTempValue("temp8", s2);
			Configs.saveTempValue("temp9", s3);
			Configs.saveTempValue("temp10", s4);
			Configs.saveTempValue("temp11", s5);
			Configs.saveTempValue("temp12", s6);			
		}	
	}

}
