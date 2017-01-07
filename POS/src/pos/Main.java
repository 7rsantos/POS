package pos;
import org.opencv.core.Core;

import javafx.application.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

	public static void main(String[] args) {
		
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// TODO Auto-generated method stub
        launch(args);
	}

	@Override
	public void start(Stage window) throws Exception {
		
		if(!Configs.getProperty("StoreName").isEmpty())
		{	
		   //display the login window
		   Login.displayLogin();
		}
		else
		{ 
			//setup the program
			Setup.initSetup();
		}	
	}

}
