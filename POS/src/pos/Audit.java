package pos;

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

public class Audit {

	/*
	 * Display the audit cash screen 1
	 */
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
	   NumericTextField hundred = new NumericTextField();
	   NumericTextField fifty = new NumericTextField();
	   NumericTextField twenty = new NumericTextField();
	   NumericTextField ten = new NumericTextField();
	   NumericTextField five = new NumericTextField();
	   NumericTextField one = new NumericTextField();	   
	   NumericTextField total = new NumericTextField();
	   
	   //total box
	   total.setEditable(false);
	   
	   //Labels
	   Label hundredlbl = new Label("$0");
	   Label fiftylbl = new Label("$0");
	   Label twentylbl = new Label("$0");
	   Label tenlbl = new Label("$0");
	   Label fivelbl = new Label("$0");
	   Label onelbl = new Label("$0");
	   Label totallbl = new Label("Total");
	   
	   //format labels
	   hundredlbl.setTextFill(Color.WHITE);
	   fiftylbl.setTextFill(Color.WHITE);
	   twentylbl.setTextFill(Color.WHITE);
	   tenlbl.setTextFill(Color.WHITE);
	   fivelbl.setTextFill(Color.WHITE);
	   onelbl.setTextFill(Color.WHITE);
	   totallbl.setTextFill(Color.WHITE);
	   totallbl.setFont(new Font("Courier Sans", 14));
	   
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
	   cancel.setOnAction( e -> stage.close());
	   next.setOnAction(new EventHandler<ActionEvent>()  {

		@Override
		public void handle(ActionEvent event) {
           
			//close the stage
			stage.close();
			
			//go to the next screen
			auditWrappedBills();
		}
		   
	   } );
	   
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
	   stage.setMinWidth(500);
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.setTitle("FASS Nova - Audit Cash");
	   
	   //show the stage
	   stage.setScene(scene);
	   stage.show();	   
	}
	
	private static void auditWrappedBills()
	{ 
		//create stage
		Stage stage = new Stage();
		   
		//root layout
        BorderPane root = new BorderPane();
		   
		//title
		Text title = new Text("Type in the number of wrapped bills for each category");
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
		NumericTextField hundred = new NumericTextField();
		NumericTextField fifty = new NumericTextField();
		NumericTextField twenty = new NumericTextField();
		NumericTextField ten = new NumericTextField();
		NumericTextField five = new NumericTextField();
		NumericTextField one = new NumericTextField();	   
		   
		//add nodes to center
		center.add(hundredimg, 0, 0);
		center.add(hundred, 1, 0);
		center.add(fiftyimg, 0, 1);
		center.add(fifty, 1, 1);
		center.add(twentyimg, 0, 2);
		center.add(twenty, 1, 2);
		center.add(tenimg, 0, 3);
		center.add(ten, 1, 3);
		center.add(fiveimg, 0, 4);
		center.add(five, 1, 4);
		center.add(oneimg, 0, 5);
		center.add(one, 1, 5);
		   
		//buttons
		Button next = new Button("Next", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Go forward.png"))));
		Button previous = new Button("Previous", new ImageView(new Image(Discount.class.getResourceAsStream("/res/Go back.png"))));
		   
		//implement actions
		previous.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			   
			   //close the stage
				stage.close();
				
			   //display previous screen
				displayAuditBills();
			}
			
		});
		//next.setOnAction(e -> );
		   
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
		stage.setMinWidth(500);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("FASS Nova - Audit Cash");
		   
		//show the stage
		stage.setScene(scene);
		stage.show();
			
	}
	
}
