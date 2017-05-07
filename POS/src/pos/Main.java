package pos;


//import org.opencv.core.Core;

import javafx.application.*;
import javafx.stage.Stage;
public class Main extends Application {

    //static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    
        
	public static void main(String[] args) {
		
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
