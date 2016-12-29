package pos;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

private static boolean perform;
	
	public static void display(String title, String message)
	{ 
	   Stage window = new Stage();
	   
	   //Block other window events
	   window.initModality(Modality.APPLICATION_MODAL);
	   window.setTitle(title);
	   window.setMinWidth(250);
	   window.setResizable(false);
	   
	   //setup label and close button
	   Label label = new Label();
	   label.setText(message);
	   Button close = new Button("OK");
	   close.setOnAction(e -> window.close());
	   
	   //setup layout
	   VBox layout = new VBox(10);
	   layout.getChildren().addAll(label, close);
	   layout.setAlignment(Pos.CENTER);
	   
	   //Display window and wait for it to be closed
	   Scene scene = new Scene(layout);
	   window.setScene(scene);
	   window.showAndWait();
	   
	}

	public static void displayOptionDialog(String title, String message)
	{ 
	   Stage window = new Stage();
	   
	   //Block other window events
	   window.initModality(Modality.APPLICATION_MODAL);
	   window.setTitle(title);
	   window.setMinWidth(250);
	   window.setResizable(false);
	   
	   //setup label and close button
	   Label label = new Label();
	   label.setText(message);
	   Button yes = new Button("Yes");
	   Button no = new Button("No");
	   
	   no.setOnAction(new EventHandler<ActionEvent>()
	   {

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			MainScreen.cancelTicket(false);
			window.close();
		}});
	   
		yes.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				MainScreen.cancelTicket(true);
				window.close();
			} 
		});
	    
		
		
	   //layout to hold buttons
	   HBox bottom = new HBox();
	   
	   //set spacing
	   bottom.setSpacing(5);
	   
	   //set alignment
	   bottom.setAlignment(Pos.CENTER);
	   
	   //add children
	   bottom.getChildren().addAll(yes, no);
		
	   //setup layout
	   VBox layout = new VBox(10);
	   layout.getChildren().addAll(label, bottom);
	   layout.setAlignment(Pos.CENTER);
	   
	   //Display window and wait for it to be closed
	   Scene scene = new Scene(layout);
	   window.setScene(scene);
	   window.show();
	   
	}
	
	public static void displayDialog(String title, String message)
	{ 
	   Stage window = new Stage();
	   
	   //Block other window events
	   window.initModality(Modality.APPLICATION_MODAL);
	   window.setTitle(title);
	   window.setMinWidth(250);
	   window.setResizable(false);
	   
	   //setup label and close button
	   Label label = new Label();
	   label.setText(message);
	   Button close = new Button("OK");
	   close.setOnAction(e -> window.close());
	   
	   //setup layout
	   VBox layout = new VBox(10);
	   layout.getChildren().addAll(label, close);
	   layout.setAlignment(Pos.CENTER);
	   
	   //Display window and wait for it to be closed
	   Scene scene = new Scene(layout);
	   window.setScene(scene);
	   window.show();
	   
	}	
	



	

}
