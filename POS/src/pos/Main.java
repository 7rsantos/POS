package pos;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        launch(args);
	}

	@Override
	public void start(Stage window) throws Exception {
		//display the login window
		Login.displayLogin();
		
	}

}
