package pos;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

public class ActionsTable  {

	public static ToolBar createActionsTable()
	{ 
		//create a vertical tool bar
		ToolBar actions = new ToolBar();
		actions.setOrientation(Orientation.VERTICAL);
		
		//create options
		Button clear = new Button("Clear Payments");
		Button discount = new Button("Customer Discount");
		Button manageReturn = new Button("Manage Returns");
		Button salesHistory = new Button("Sales History");
		Button open = new Button("Open Cash Drawer");
		Button receive = new Button("Receive Cash");
		Button remove = new Button("Remove Cash");
		Button holdTicket = new Button("Place Ticket On-Hold");
		Button retrieveTicket = new Button("Retrieve Held Ticket");
		Button transferCash = new Button("Transfer Cash");
		Button transferProduct = new Button("Transfer Product");
		
		//add nodes to the tool bar
		
		
		actions.getItems().addAll(new Hyperlink("Clear"), new Hyperlink("Discount"), new Separator(),
				manageReturn, new Separator(), salesHistory, new Separator(), open,
				new Separator(), receive, new Separator(), remove, new Separator(),
				holdTicket, new Separator(), retrieveTicket, new Separator(), transferCash,
				new Separator(), transferProduct);
		
		return actions;
	}
}
