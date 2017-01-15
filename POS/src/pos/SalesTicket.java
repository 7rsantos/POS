package pos;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SalesTicket {

    private String time;
    private String ticketno;
    private double total;
    private String paymentMethod;
	private String status;
    
	/*
	 * Constructor of a sales ticket
	 */
	public SalesTicket(String time, String ticketno, double total, String paymentMethod,
			           String status)
	{ 
	   this.time = time;
	   this.ticketno = ticketno;
	   this.total = total;
	   this.status = status;
	   this.paymentMethod = paymentMethod;
	}
	
	public String getTime()
	{ 
		return this.time;
	}
	
	public void setTime(String time)
	{ 
		this.time = time;
	}
	
	public String getTicketno()
	{ 
		return this.ticketno;
	}
	
	public void setTicketNo(String ticketno)
	{ 
		this.ticketno = ticketno;
	}
	
	public double getTotal()
	{ 
		return this.total;
	}
	
	public void setTotal(double total)
	{ 
		this.total = total;
	}
	
	public void setPaymentMethod(String paymentMethod)
	{ 
		this.paymentMethod = paymentMethod;
	}
	
	public String getPaymentMethod()
	{ 
		return this.paymentMethod;
	}
	
	public void setStatus(String status)
	{ 
		this.paymentMethod = status;
	}
	
	public String getStatus()
	{ 
		return this.status;
	}
	
	/*
	 * Create a table for the sales ticket type
	 */
	@SuppressWarnings("unchecked")
	public static TableView<SalesTicket> getSalesTicketTable()
	{ 
		TableView<SalesTicket> table = new TableView<SalesTicket>();
		
		//create columns
		TableColumn<SalesTicket, String> time = new TableColumn<SalesTicket, String>("Time");
		time.setCellValueFactory(new PropertyValueFactory<>("time"));
		
		TableColumn<SalesTicket, String> ticket = new TableColumn<SalesTicket, String>("Ticket No");
		ticket.setCellValueFactory(new PropertyValueFactory<>("ticketno"));
		
	    TableColumn<SalesTicket, String> total = new TableColumn <SalesTicket, String>("Total");
		total.setCellValueFactory(new PropertyValueFactory<>("total"));
		
		TableColumn<SalesTicket, String> payment = new TableColumn<SalesTicket, String>("Payment Method");
		payment.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
		
		TableColumn<SalesTicket, String> status = new TableColumn<SalesTicket, String>("Status");
		status.setCellValueFactory(new PropertyValueFactory<>("status"));
		
		//set sizes
		time.setPrefWidth(90);
		ticket.setPrefWidth(120);
		total.setPrefWidth(100);
		payment.setPrefWidth(110);		
		status.setPrefWidth(100);
		
		//add columns to the table view
        table.getColumns().addAll(time, ticket, total, status, payment);	
		
		//return the table
		return table;
	}
	
}
