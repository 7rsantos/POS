package pos;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PhotoScreen {
	
	private static Button start;
	private static Button previous;
	private static Button takePicture;
	private static ImageView imageview;
	Mat frame;
	
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that realizes the video capture
	private VideoCapture capture = new VideoCapture();
	// a flag to change the button behavior
	private boolean cameraActive = false;
	// the id of the camera to be used
	private static int cameraId = 0;

	public static void initComponents()   {
	   //create border pane that will hold all nodes
 	   BorderPane root = new BorderPane();
	
	   //create imageview that will hold the picture
	   imageview = new ImageView();
	   
	   //setup imageview
	   imageview.setFitHeight(300);
	   imageview.setFitWidth(300);
	
	   //create buttons
	   previous = new Button("Go back");
	   start = new Button("Pause");
	   takePicture = new Button("Take Snapshot");
	   
	   //set on action events
	   PhotoScreen jc = new PhotoScreen();
	   start.setOnAction(e -> jc.startCamera(e));
	   takePicture.setOnAction(e -> jc.takePicture());
	
	   //create VBox that hold buttons
	   HBox bottom = new HBox();
	   
	   //set spacing within buttons
	   bottom.setSpacing(7);
	   
	   //setup the alignment
	   bottom.setAlignment(Pos.TOP_CENTER);
	   
	   //setup padding
	   bottom.setPadding(new Insets(0, 0, 10, 0));
	   
	   //add nodes to the layout
	   bottom.getChildren().addAll(previous, start, takePicture);
	   
	   //add nodes to root
	   root.setBottom(bottom);
	   root.setCenter(imageview);
	   
	   //create the scene
	   Scene scene = new Scene(root);
	   
	   //setup the stage and show
	   Stage photoScreen = new Stage();
	   photoScreen.setWidth(400);
	   photoScreen.setHeight(400);
	   photoScreen.setResizable(false);
	   
	   //set on close request
	   photoScreen.setOnCloseRequest(new EventHandler<WindowEvent>()
	   {		   
	      public void handle(WindowEvent e)
	      { 
	    	  jc.setClosed();
	      }
	   
	   });
	   //setup title
	   photoScreen.setTitle("FAAS Nova - Take Picture");
	   
	   //setup modality
	   photoScreen.initModality(Modality.APPLICATION_MODAL);
	   
	   //set the scene
	   photoScreen.setScene(scene);
	   
	   //show the window
	   photoScreen.show();
	   
	
	}
	
	public static void displayPhotoScreen()
	{ 
		initComponents();
		
	}
	
	/**
	 * The action triggered by pushing the button on the GUI
	 *
	 * @param event
	 *            the push button event
	 */
	public void startCamera(ActionEvent event)
	{
		//currentFrame.setFitHeight(300);
		//currentFrame.setFitWidth(300);
		if (!this.cameraActive)
		{
			// start the video capture
			this.capture.open(cameraId);
			
			// is the video stream available?
			if (this.capture.isOpened())
			{
				this.cameraActive = true;
				
				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run()
					{
						// effectively grab and process a single frame
						frame = grabFrame();
						
						// convert and show the frame
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(imageview, imageToShow);
					}
				};
				
				//snapshot.setId("snap");
				//snapshot.setOnAction(e -> takePicture());
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				// update the button content
				this.start.setText("Stop Camera");
			}
			else
			{
				// log the error
				System.err.println("Impossible to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.start.setText("Start Camera");
			
			// stop the timer
			this.stopAcquisition();
		}
	}
	
	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Mat} to show
	 */
	private Mat grabFrame()
	{
		// init everything
		Mat frame = new Mat();
		
		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);
				
				// if the frame is not empty, process it
				//if (!frame.empty())
				//{
					//Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
				//}
				
			}
			catch (Exception e)
			{
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		
		return frame;
	}
	
	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	public void stopAcquisition()
	{
		if (this.timer!=null && !this.timer.isShutdown())
		{
			try
			{
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		
		if (this.capture.isOpened())
		{
			// release the camera
			this.capture.release();
		}
	}
	
	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view
	 *            the {@link ImageView} to update
	 * @param image
	 *            the {@link Image} to show
	 */
	public static void updateImageView(ImageView view, Image image)
	{
		Utils.onFXThread(view.imageProperty(), image);
	}
	
	/**
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed()
	{
		this.stopAcquisition();
	}
	
	public void takePicture()
	{ 
		
	    //FileChooser fileChooser = new FileChooser(); 
        //File file = fileChooser.showOpenDialog(null);
        //if (file != null) {
        //Highgui.imwrite(file.getPath(), frame);	
		Imgcodecs.imwrite("C:/Users/Atomic/Downloads/example.jpg", frame);
		
	}
	
}
