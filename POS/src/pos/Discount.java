package pos;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
	  
	   //set id
	   root.setId("border");
	   
	   //load style sheets
       root.getStylesheets().add(Discount.class.getResource("MainScreen.css").toExternalForm());
		
	   //build the scene
	   Scene scene = new Scene(root);
		
	   //setup stage
	   stage.centerOnScreen();
	   stage.setResizable(false);
	   stage.setTitle("FASS Nova - Select discount type");
	   stage.setMinWidth(100);
		
	   //set the scene
	   stage.setScene(scene);
		
	   //show the stage
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.showAndWait();
	}
	
	/*
	 * Apply discount
	 * 
	 */
	private static void applyDiscount(String discount)
	{ 				
	   //apply discount to main screen	
		MainScreen.Discount.setText(discount);
	}
}
