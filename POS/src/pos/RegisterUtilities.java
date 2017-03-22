package pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.sql.Connection;

public class RegisterUtilities {

	/*
	 *  Increase cash after each cash sale
	 */
	public static void increaseCash(double amount)
	{ 
	   String query1 = "CALL updateExpectedCash(?,?)";
	   double oldAmount = getExpectedCash();
	   
	   try
	   { 
	       java.sql.Connection conn = Session.openDatabase();
	       
	       PreparedStatement ps = conn.prepareCall(query1);
	       
	       //set parameters
	       //ps.setString(1, dateFormat);
	       ps.setDouble(1, amount + oldAmount);
	       ps.setInt(2, Integer.parseInt(Configs.getProperty("Register")));
	       
	       //execute query
	       ps.executeUpdate();
	       	       
	       //close the connection
	       conn.close();
	       
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();   
	   }
	}
	
	/*
	 * Get the expected cash
	 */
	public static double getExpectedCash()
	{ 
		String query1 = "SELECT expectedCash FROM auditcashhistory WHERE auditCashHistory.registerID = (SELECT Register.`ID` FROM Register WHERE Register.registerStoreCode = ?) ";
		double expected = 0.0;
		   
	    try
		{ 
	       Connection conn = Session.openDatabase();
		      
 	       PreparedStatement ps = conn.prepareCall(query1);
		      
		   //set parameters
		   //ps.setInt(1, Integer.parseInt(Configs.getProperty("Register")));
		   ps.setString(1, Configs.getProperty("StoreCode"));
		      
		   //execute query
		   ResultSet rs = ps.executeQuery();
		      
		   while(rs.next())
		   { 
		      expected = rs.getDouble(1); 	  
		   }
		   
		   ps.close();
		}
	    catch(Exception e)
	    { 
	       e.printStackTrace();	
	    }
	    	    
	    return expected;
	}
	
	/*
	 * Store the totals in the audit cash history
	 */
	public static void auditCash(double total, String notes)
	{ 
	   double expected = 0.0;
	   
	   try
	   {
		  // get expected cash
		   expected = getExpectedCash();
		   
	      //execute query
	      String query = "CALL createAuditCash(?, ?, ?, ?, ?,?)";
	      
	      Connection conn = Session.openDatabase();
	      
	      PreparedStatement ps = conn.prepareCall(query);
	      
	      //set parameters
	      ps.setDouble(1, total);
	      ps.setDouble(2, total);
	      ps.setDouble(3, total - expected);
	      ps.setInt(4, Integer.parseInt(Configs.getProperty("Register")));
	      ps.setString(5, Configs.getProperty("CurrentUser"));
	      ps.setString(6, notes);
	      
	      //execute query
	      ps.execute();
	      
	      //close the connection
	      ps.close();
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();   
	   }
	}
	
	/*
	 *  Increase cash after each cash sale
	 */
	public static void transferCash(double amount, String storeCode)
	{ 
	   String query1 = "CALL transferCash(?,?,?)";
	   double oldAmount = RegisterUtilities.getExpectedCash();
	   
	   try
	   { 
	       java.sql.Connection conn = Session.openDatabase();
	       
	       PreparedStatement ps = conn.prepareCall(query1);
	       
	       //set parameters
	       //ps.setString(1, dateFormat);
	       ps.setDouble(1, amount + oldAmount);
	       ps.setInt(2, Integer.parseInt(Configs.getProperty("Register")));
	       ps.setString(3, storeCode);
	       
	       //execute query
	       ps.executeUpdate();
	       	       
	       //close the connection
	       conn.close();
	       
	   }
	   catch(Exception e)
	   { 
		  e.printStackTrace();   
	   }
	}
}
