package pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Properties;

public class Configs {

	public static Properties prop = new Properties();
	public static Properties prop2 = new Properties();
	

	public static void saveProperty(String title, String value)  
	{ 
	   try  {
		  
		  //save property on external file
	      prop.setProperty(title, value);
	      prop.store(new FileOutputStream("config.properties"), null);
	      
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();
	   }
	}
	
	public static String getProperty(String title)
	{ 
		
		String value = "";
		
		//if file exists
		try
		
		{ 
			//get the property
			prop.load(new FileInputStream("config.properties"));
		    value = prop.getProperty(title);	
		    
		}
		catch(Exception e)
		{ 
			e.printStackTrace();
		}
		
		return value;
	}
	
	public static void saveTempValue(String title, String value)
	{ 
	   try  {
		  
		  //save property on external file
	      prop2.setProperty(title, value);
	      prop2.store(new FileOutputStream("temp.txt"), null);
		   
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();
	   }
	}
	
	public static String getTempValue(String title)
	{ 
		
		String value = "";
		try
		{ 
			prop2.load(new FileInputStream("temp.txt"));
		    value = prop2.getProperty(title);	
		}
		catch(Exception e)
		{ 
			e.printStackTrace();
		}
		
		return value;
	}
}
