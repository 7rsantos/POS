package pos;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Employee {

	private String Username;
	private String  firstName;
	private String  lastName;
	private String phoneNumber;
	private String  Email;
	private String dateOfBirth;
	private String privilege;
	
	public Employee(String user, String first, String last, String phone, String email, 
			String dob, String privilege)
	{ 
	   this.Username = user;
	   this.firstName = first;
	   this.lastName = last;
	   this.phoneNumber = phone;
	   this.Email = email;
	   this.dateOfBirth = dob;
	   this.privilege = privilege;   
	}
	
	public String getFirstName()
	{ 
		return this.firstName;
	}
	
	public void setFirstName(String first)
	{ 
		this.firstName = first;
	}
	
	public String getLastName()
	{ 
		return this.lastName;
	}
	
	public void setLastName(String last)
	{ 
		this.lastName = last;
	}
	
	public String getUsername()
	{ 
		return this.Username;
	}
	
	public void setUserName(String user)
	{ 
		this.Username = user;
	}
	
	public String getPhoneNumber()
	{ 
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(String phone)
	{ 
		this.phoneNumber = phone;
	}
	
	public String getEmail()
	{ 
		return this.Email;
	}
	
	public void setEmail(String email)
	{ 
		this.Email = email;
	}
	
	public String getDateOfBirth()
	{ 
		return this.dateOfBirth;
	}
	
	public void setdateOfBirth(String dob)
	{ 
		this.dateOfBirth = dob;
	}
	
	public String getPrivilege()
	{ 
		return this.privilege;
	}
	
	public void setPrivilege(String privilege)
	{ 
		this.privilege = privilege;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Employee> createEmployeeTable()
	{ 
		TableView<Employee> table = new TableView<Employee>();
		
		table.setEditable(false);
		
		//create columns
		TableColumn<Employee, String> user = new TableColumn<Employee, String>("Username");
		user.setCellValueFactory(new PropertyValueFactory<>("Username"));		
		
		TableColumn<Employee, String> firstName = new TableColumn<Employee, String>("firstName");
		firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
		TableColumn<Employee, String> lastName = new TableColumn<Employee, String>("lastName");
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
	    TableColumn<Employee, String> phone = new TableColumn <Employee, String>("phoneNumber");
		phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		
		TableColumn<Employee, String> email = new TableColumn<Employee, String>("Email");
		email.setCellValueFactory(new PropertyValueFactory<>("Email"));
		
		TableColumn<Employee, String> dob = new TableColumn<Employee, String>("dateOfBirth");
		dob.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));	
		
		TableColumn<Employee, String> privilege = new TableColumn<Employee, String>("privilege");
		privilege.setCellValueFactory(new PropertyValueFactory<>("privilege"));	
		
		//set sizes
		user.setPrefWidth(100);
		firstName.setPrefWidth(100);
		lastName.setPrefWidth(100);		
		privilege.setPrefWidth(70);
		dob.setPrefWidth(95);
		phone.setPrefWidth(100);
		email.setPrefWidth(160);
		
		
		//add columns to the table view
        table.getColumns().addAll(user, firstName, lastName, phone, email, dob, privilege);	
		
		return table;
	}
}
