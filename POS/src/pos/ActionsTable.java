package pos;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ActionsTable  {

	public static ToolBar createActionsTable()
	{ 
		//create a vertical tool bar
		ToolBar actions = new ToolBar();
		actions.setOrientation(Orientation.VERTICAL);
		actions.setPadding(new Insets(10, 80, 10, 10));

		//get images for the icons
		Image salesIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Schedule.png"));
		Image discountIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Red tag.png"));
		Image returnIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Redo.png"));
		Image clearIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Erase.png"));
		Image openIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Unlock.png"));
		Image addIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Create.png"));
		Image removeIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Remove.png"));
		Image holdIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Pause.png"));
		Image cashIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Dollar.png"));
		Image withdrawIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Fall.png"));
		Image productIcon = new Image(ActionsTable.class.getResourceAsStream("/res/Delivery.png"));

		
		//create options and add an icon to each
		Label sales = new Label("Sales History", new ImageView(salesIcon));
		Label discount = new Label("Discount", new ImageView(discountIcon));
		Label manage = new Label("Manage Return", new ImageView(returnIcon));
		Label clear = new Label("Clear Payment", new ImageView(clearIcon));
		Label open = new Label("Open Cash Drawer", new ImageView(openIcon));
		Label receive = new Label("Receive Cash", new ImageView(addIcon));
		Label remove = new Label("Remove Cash", new ImageView(removeIcon));
		Label holdTicket = new Label("Hold Ticket", new ImageView(holdIcon));
		Label retrieveTicket = new Label("Retrieve Ticket", new ImageView(withdrawIcon));
		Label transferCash = new Label("Transfer Cash", new  ImageView(cashIcon));
		Label transferProduct = new Label("Transfer Product", new ImageView(productIcon));
				
		//add nodes to the tool bar				
		actions.getItems().addAll(sales, new Separator(), discount, new Separator(),
				manage, new Separator(), clear, new Separator(), open,
				new Separator(), receive, new Separator(), remove, new Separator(),
				holdTicket, new Separator(), retrieveTicket, new Separator(), transferCash,
				new Separator(), transferProduct);

		//perform an action when clicked
		sales.setOnMouseClicked(new EventHandler<MouseEvent>()  {

			@Override
			public void handle(MouseEvent event) {
            
				//close the stage
				MainScreen.closeStage();
				
				//display sales history screen
				SalesHistory.displaySalesHistory();
			} 

		});
		discount.setOnMouseClicked(e -> System.out.println("Works"));
		manage.setOnMouseClicked(e -> System.out.println("Works"));
		clear.setOnMouseClicked(e -> System.out.println("Works"));
		open.setOnMouseClicked(e -> Session.passwordValidation(2));
		receive.setOnMouseClicked(e -> System.out.println("Works"));
		remove.setOnMouseClicked(e -> System.out.println("Works"));
		holdTicket.setOnMouseClicked(e -> System.out.println("Works"));
		retrieveTicket.setOnMouseClicked(e -> System.out.println("Works"));
		transferCash.setOnMouseClicked(e -> System.out.println("Works"));
		transferProduct.setOnMouseClicked(e -> System.out.println("Works"));
		
		return actions;
	}
}
