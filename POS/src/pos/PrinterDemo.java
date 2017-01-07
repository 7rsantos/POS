package pos;

import javafx.application.Application;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrinterDemo extends Application {
	
	private Label jobStatus = new Label();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		VBox root = new VBox();
		
		Label textLb = new Label("Text");
		
		TextArea text = new TextArea();
		text.setPrefColumnCount(20);
		text.setPrefRowCount(10);
		text.setWrapText(true);
		
		//create button
		Button print = new Button("Print Text");
		print.setOnAction(e -> print(text));
		
		HBox jobStatusBox = new HBox(5, new Label("Print Job Status: "), jobStatus);
		
		root.getChildren().addAll(textLb, text, jobStatusBox, print);
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Printing demo");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	private void print(Node node) {
		// TODO Auto-generated method stub
		jobStatus.textProperty().unbind();
		jobStatus.setText("Creating a printer job...");
		
		PrinterJob job = PrinterJob.createPrinterJob();
		
		if( job != null)
		{ 
		   jobStatus.textProperty().bind(job.jobStatusProperty().asString());
		   
		   System.out.println(new java.io.File("configuration.txt").getAbsolutePath());
		   
		   boolean printed = job.printPage(node);
		   if(printed)
		   { 
			  job.endJob();   
		   }	   
		   else
		   { 
			  jobStatus.textProperty().unbind();
			  jobStatus.setText("Printing failed..");
		   }	   
		}
		else
		{
		   jobStatus.setText("Could not create printer job");
		}	
		
	}

}
