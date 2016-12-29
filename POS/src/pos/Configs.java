package pos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class Configs {

	public static Properties prop = new Properties();
	
	public void saveProperty(String title, String value)
	{ 
	   try  {
		  
		   //save property on external file
	      prop.setProperty(title, value);
	      prop.store(new FileOutputStream("configuration.txt"), null);
		   
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();
	   }
	}
	
	public String getProperty(String title)
	{ 
		
		String value = "";
		try
		{ 
			prop.load(new FileInputStream("configuration.txt"));
		    value = prop.getProperty(title);	
		}
		catch(Exception e)
		{ 
			e.printStackTrace();
		}
		
		return value;
	}
}
