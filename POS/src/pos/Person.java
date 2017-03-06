package pos;

public class Person {
    
	private String firstName;
	private String lastName;
	private int id;
	
	public Person(String first, String last, int idNumber)
	{ 
	   this.firstName = first;
	   this.lastName = last;
	   id = idNumber;
	}
	
	public int getId()
	{ 
	   return id;	
	}
	
	public void setId(int newID)
	{ 
	   this.id = newID;	
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
