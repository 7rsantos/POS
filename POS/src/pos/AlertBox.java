package pos;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

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
}
