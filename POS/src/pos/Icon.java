package pos;

import java.awt.Font;
import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Icon {

	
	public static VBox createIcon(String name, String path)
	{ 
		Image image = new Image(Icon.class.getResourceAsStream(path));
		Label iconName = new Label(name);
		ImageView icon = new ImageView(image);
		
		//set label text properties
		iconName.setTextFill(Color.WHITE);
		
		icon.setOnMouseClicked(e -> System.out.println("Product Search"));
		iconName.setOnMouseClicked(e -> System.out.println("Product Search"));
		
		VBox layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(icon, iconName);
		
		return layout;
	}
	
	public static Button createButtonIcon(String name, String path)
	{ 
		
		//create image 
		Image image = new Image(Icon.class.getResourceAsStream(path));
		
		//create imageview
		ImageView imageview = new ImageView(image);
		
		//set imageview size
		imageview.setFitWidth(30);
		imageview.setFitHeight(30);
		
		//create icon
		Button icon = new Button("", imageview);
		
		
		return icon;
	}
	
	public static Button createPaymentButtonIcon(String name, String path)
	{ 
		
		//create image 
		Image image = new Image(Icon.class.getResourceAsStream(path));
		
		//create imageview
		ImageView imageview = new ImageView(image);
		
		//set imageview size
		imageview.setFitWidth(185);
		imageview.setFitHeight(150);
		
		//create icon
		Button icon = new Button("", imageview);
		
		
		return icon;
	}	
	
	public static FileChooser createImageChooser()
	{ 
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Image");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"),
				new ExtensionFilter("JPEG", "*.jpg"));
		
		return fileChooser;
		
	}
	
	public static File selectImage(FileChooser fileChooser)
	{
		File selectedFile = fileChooser.showOpenDialog(null);
		
		return selectedFile;
	} 
	
	public static DirectoryChooser buildDirectory()
	{ 
		//build new directory
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Directory");
		
		return directoryChooser;
	}
	
	public static File selectDirectory(DirectoryChooser directoryChooser)
	{
		File selectedFile = directoryChooser.showDialog(null);
		
		return selectedFile;
	} 
	
}
