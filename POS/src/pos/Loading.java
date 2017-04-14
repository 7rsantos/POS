package pos;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Loading {

   /*
    * Display the loading screen	
    */
	public static void displayLoadingScreen()
	{
	   //stage
	   Stage stage = new Stage();
	   
	   //imageview
	   ImageView picture = new ImageView();
	   picture.setImage(new Image(Loading.class.getResourceAsStream("/res/pos.jpg")));
	   picture.setFitHeight(400);
	   picture.setFitWidth(400);
	   
	   //progress indicator
	   ProgressIndicator pi = new ProgressIndicator();
	   
	   //root
	   VBox root = new VBox();
	   
	   //add nodes to root
	   root.getChildren().addAll(picture, pi);
	   
	   //create main screen
	   Stage window = new Stage();
	   Scene main = MainScreen.displayMainScreen(window);
	   
	   //set scene
	   window.setScene(main);
	   
	   //setup root
	   root.setSpacing(7);
	   root.setAlignment(Pos.CENTER);
	   root.setPadding(new Insets(20, 20, 20, 20));
	   
	   //set id
	   root.setId("border");
	   
	   //get style sheets
	   root.getStylesheets().add(Loading.class.getResource("MainScreen.css").toExternalForm());
	   
	   //setup stage
	   stage.setTitle("FASS Nova");
	   stage.initStyle(StageStyle.UNDECORATED);
	   stage.setMinWidth(375);
	   stage.setMaxHeight(300);
	   stage.centerOnScreen();
	   
	   //scene
	   stage.setScene(new Scene(root));
	   
	   //show
	   stage.show();
	   
		KeyFrame key = new KeyFrame(Duration.millis(4000), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			    
				//close this stage
				stage.close();
				
				//go to main screen
				window.show();
			}
					   
		});	
	
		//setup timer to show main screen after 4 seconds
	   Timeline timeline = new Timeline(key);
	   timeline.play();	
	}
}
