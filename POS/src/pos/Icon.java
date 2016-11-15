package pos;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Icon {

	
	public static VBox createIcon(String name, String path)
	{ 
		Image image = new Image(Icon.class.getResourceAsStream(path));
		Label iconName = new Label(name);
		ImageView icon = new ImageView(image);
		
		icon.setOnMouseClicked(e -> System.out.println("Product Search"));
		iconName.setOnMouseClicked(e -> System.out.println("Product Search"));
		
		VBox layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(icon, iconName);
		
		return layout;
	}
	
}
