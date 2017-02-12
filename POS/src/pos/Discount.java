package pos;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class Discount {

	/*
	 *  Display the discount screen
	 *  Will prompt the user to select a discount type
	 */
	public static void displayDiscountScreen()
	{ 
	   //build the stage
	   Stage stage = new Stage();
		
	   //root layout
	   GridPane root = new GridPane();
	   
	   //text
	   Text title = new Text("Select discount type");
	   Text desc1 = new Text("Loyal Customer");
	   Text desc2 = new Text("Large Purchase");
	   Text desc3 = new Text("Specific Reason");
	   
	   //change text color
	   title.setFill(Color.WHITE);
	   desc1.setFill(Color.WHITE);
	   desc2.setFill(Color.WHITE);
	   desc3.setFill(Color.WHITE);
	   
	   //toggle group
	   ToggleGroup group = new ToggleGroup();
	   RadioButton five = new RadioButton("5%");
	   RadioButton ten = new RadioButton("10%");
	   RadioButton custom = new RadioButton("Custom");
	   
	   //change text color
	   five.setTextFill(Color.WHITE);
	   ten.setTextFill(Color.WHITE);
	   custom.setTextFill(Color.WHITE);
	   
	   //set toggle group
	   five.setToggleGroup(group);
	   ten.setToggleGroup(group);
	   custom.setToggleGroup(group);
	   
	   //Button 
	   Button select = new Button("Apply Discount", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Apply.png"))));

	   //set on action
	   select.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
		   if(group.getSelectedToggle() != null)
		   { 
			  if(group.getSelectedToggle().equals(custom))
			  { 
			     //close the current stage
				 //stage.close();
				  
				 //go to next screen
				 getCustomDiscount();
				 
				 //close the stage
				 stage.close();
				 
			  }	  
			  else if(group.getSelectedToggle().equals(ten))
			  { 
				 //apply 10% discount 
			     applyDiscount("10%");	
			     
				 //close the current stage
				 stage.close();
			  }	  
			  else
			  { 
				 //apply 5% discount 
				 applyDiscount("5%");
				 
				 //close the current stage
				 stage.close();
			  }
			  
		   }	    
		   else
		   {
			  AlertBox.display("FASS Nova", "Please select a discount type");
	  	   }	
		} 

		   
	   });
	   
	   //setup root
	   root.setVgap(10);
	   root.setPadding(new Insets(10, 10, 10, 10));
	   
	   //set nodes in root layout
	   root.add(title, 0, 0);
	   root.add(five, 0, 1);
	   root.add(desc1, 1, 1);
	   root.add(ten, 0, 2);
	   root.add(desc2, 1, 2);
	   root.add(custom, 0, 3);
	   root.add(desc3, 1, 3);
	   root.add(select, 0, 4);
	   
	   //set id
	   root.setId("border");
	   
	   //load style sheets
       root.getStylesheets().add(Discount.class.getResource("MainScreen.css").toExternalForm());
		
	   //build the scene
	   Scene scene = new Scene(root);
		
	   //setup stage
	   stage.setMinWidth(300);
	   stage.centerOnScreen();
	   stage.setResizable(false);
	   stage.setTitle("FASS Nova");
		
	   //set the scene
	   stage.setScene(scene);
		
	   //show the stage
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.showAndWait();
	}
	
	/*
	 * Get the custom amount for the discount
	 */
	private static void getCustomDiscount()
	{
		//stage.close();
		
	   //stage
		Stage window = new Stage();
		
		//title
		Text title = new Text("Type in custom amount");
		title.setFill(Color.WHITE);
		title.setFont(new Font("Courier Sans", 16));
		
		//top layout
		VBox top = new VBox();
		top.setPadding(new Insets(10, 10, 10, 10));
		top.setSpacing(6);		
		
		//buttons
		Button select = new Button("Apply", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Apply.png"))));
		Button cancel = new Button("Cancel", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Cancel.png"))));
		
		//numeric text fields
		NumericTextField discount = new NumericTextField();
		
		//add nodes to top
		top.getChildren().addAll(title, discount);
		
		//implement action
		cancel.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
			   
				//close the stage
				window.close();
				
				//display discount screen
				displayDiscountScreen();
			} 			
		});
		
		select.setOnAction(new EventHandler<ActionEvent>()   {

			@Override
			public void handle(ActionEvent event) {
				
				//close the stage
				window.close();
				
				//apply discount
				applyDiscount(discount.getText() + "%");
				
			} 
			
		});
		
		
		//bottom layout
		HBox bottom = new HBox();
		
		//setup bottom
		bottom.setSpacing(5);
		
		//add nodes to bottom
		bottom.getChildren().addAll(select, cancel);
		
		//root layout
		BorderPane root = new BorderPane();
		
		//setup root
		bottom.setPadding(new Insets(10, 10, 10, 10));
		
		root.setTop(top);
		root.setBottom(bottom);
		
		//add nodes to root
		//root.getChildren().addAll(title, discount, bottom);
		
		//set id
		root.setId("border");
		
		//get style sheets
		root.getStylesheets().add(Discount.class.getResource("MainScreen.css").toExternalForm());
		
		//scene
		Scene custom = new Scene(root);
		
		
		//setup stage
		window.setTitle("FASS Nova");
		
		window.setMinWidth(250);
		window.centerOnScreen();
		
		window.setScene(custom);
		window.initModality(Modality.APPLICATION_MODAL);		
		window.showAndWait();
	}
	
	/*
	 * Apply discount
	 * 
	 */
	private static void applyDiscount(String discount)
	{ 				
	   //apply discount to main screen	
		MainScreen.Discount.setText(discount);
		
		//recompute subtotal
		MainScreen.subTotal.setText(Double.toString(Receipt.setPrecision(Product.computeSubTotal(MainScreen.products, discount))));
		MainScreen.Total.setText(Double.toString(Receipt.setPrecision(Product.computeTotal(MainScreen.subTotal.getText(), MainScreen.Tax.getText()))));
	}
}
