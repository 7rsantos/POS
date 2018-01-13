package pos;




import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

//import org.opencv.core.Core;

import javafx.application.*;
import javafx.stage.Stage;
public class Main extends Application {

    //static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	private static Logger logger = LogManager.getLogger(Main.class);
        
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
			//needs setup
			logger.info("Program needs to be setup");
			
			//setup the program
			Setup.initSetup();
		}	
	}

}
