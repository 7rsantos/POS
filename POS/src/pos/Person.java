package pos;

public class Person {
    
	private String firstName;
	private String lastName;
	
	public Person(String first, String last)
	{ 
	   this.firstName = first;
	   this.lastName = last;
	}
	
	public String getFirstName()
	{ 
	   return this.firstName;	
	}
	
	public String getLastName()
	{ 
		return this.lastName;
	}
	
	public void setFirstName(String first)
	{ 
		this.firstName = first;
	}
	
	public void setLastName(String last)
	{ 
		this.lastName = last;
	}
}
