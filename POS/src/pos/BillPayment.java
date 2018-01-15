package pos;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BillPayment {
   /*
    * process phone cards
    */
	public static void processPhoneCards(ObservableList<Product> products, int option)
	{

		   if(option == 10)
		   {
		      //create product
			  Product p = new Product("$10 Phone Card", " ", 1, 10);
			  
			  //add product to lists
			  products.add(p);
			  
			  //go back to main screen
			  backToMainScreen(products);
		   }	
		   else if(option == 7)
		   {
			  //create product
			  Product p = new Product("$7 Phone Card", " ", 1, 7);
				  
			  //add product to lists
			  products.add(p);
				  
				  
			  //go back to main screen
			  backToMainScreen(products);			   
		   }
		   else if(option == 5)
		   {
			  //create product
			  Product p = new Product("$5 Phone Card", " ", 1, 5);
				  
			  //add product to lists
			  products.add(p);
				  
				  
			  //go back to main screen
			  backToMainScreen(products);			   
		   }	
		   else
		   {
			  //create product
			  Product p = new Product("$2 Phone Card", " ", 1, 2);
				  
			  //add product to lists
			  products.add(p);
				  
			  //go back to main screen
			  backToMainScreen(products);				   
		   }	   
			  			
	      
	}
	
	   /*
	    * process phone cards
	    */
		public static void processBillPayment(ObservableList<Product> allProducts, int caller)
		{
		   //root
		   TextField amount = new NumericTextField();
		   
		   //Button
		   Button accept = new Button("Accept");
		   
		   //root
		   VBox root = new VBox();
		   
		   //stage
		   Stage stage = new Stage();
		   
		   accept.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent e) {
			
			   if(amount.getText() != null && !amount.getText().isEmpty())
			   {
				   if(caller == 1)
				   {	   
			         //get quantity
				     double price = Double.parseDouble(amount.getText());
				  
				     //create product
				     Product p = new Product("Payxchange bill payment", "0", 1, price);
				  
				     //add product to the list
				     //allProducts.add(p);
				  
				     //close
				     stage.close();
				  
				     //MainScreen.productList.add("MoneyWire");
					 MainScreen.products.add(p);
					  
					 //set table items
					 MainScreen.setTableItemsforNonTaxItems(MainScreen.products);
					  
					 //add amount
					 MainScreen.Total.setText(Double.toString(Receipt.setPrecision(MainScreen.getTotal() + Double.parseDouble(amount.getText()))));
					  
					 //refresh table
					 MainScreen.table.refresh();
			      }
				  else if(caller == 2)
				  {
				      //get quantity
					  double price = Double.parseDouble(amount.getText());
					  
					  //create product
					  Product p = new Product("Boss Revolution recharge", " ", 1, price);
					  
					  //add product to the list
					  allProducts.add(p);
					  
					  //System.out.println("Got to the Boss section");
					  
					  //close
					  stage.close();
					  
					  //back to main screen
					  backToMainScreen(allProducts);					   
				  }
				  else
				  {
				      //get quantity
					  double price = Double.parseDouble(amount.getText());
					  
					  //create product
					  Product p = new Product("Mi Llamada recharge", " ", 1, price);
					  
					  //add product to the list
					  allProducts.add(p);
					  
					  //close
					  stage.close();
					  
					  //back to main screen
					  backToMainScreen(allProducts);					  
				  }		  
			   }	
			   else
			   {
			      AlertBox.display("FASS Nova", "Put in amount!");	   
			   }	   
				
		   }
			
			   
		   });
		   
		   //setup root
		   root.setAlignment(Pos.CENTER);
		   root.setSpacing(7);
		   root.setPadding(new Insets(12,12,12,12));
		   
		   //add nodes to root
		   root.getChildren().addAll(amount, accept);
		   
		   //set id
		   root.setId("border");
		   
		   //get style sheets
		   root.getStylesheets().addAll(BillPayment.class.getResource("MainScreen.css").toExternalForm());
		   
		   //scene
		   Scene scene = new Scene(root);
		   
		   //setup stage
		   stage.setTitle("Nova - Select amount");
		   stage.initModality(Modality.APPLICATION_MODAL);
		   stage.setResizable(false);
		   stage.setMinWidth(250);
		   stage.centerOnScreen();
		   
		   //set scene
		   stage.setScene(scene);
		   
		   //show
		   stage.showAndWait();
		   
		}
		
		/*
		 * Go back to main screen
		 */
		public static void backToMainScreen (ObservableList<Product> allProducts) {
			   		   
			MainScreen.setTableItems(allProducts);   
			
		}

}
