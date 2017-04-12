package pos;

import java.beans.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {		
	
    private String name;
    private Double unitPrice;
    private int quantity;
    private double price;
    private String unitSize;
    
    public Product()
    { 
        this.name = "";
        this.unitPrice = 0.00;
        this.quantity = 0;
        this.price = 0;
        this.unitSize = "";
        
    }
    
    public Product(String name, String unitSize, int quantity, double unitPrice)
    { 	
    	
       this.name = name;
       this.unitSize = unitSize;
       this.quantity = quantity;
       this.unitPrice = unitPrice;
       this.price = quantity * unitPrice;
       
    }
    
    public String getName()
    { 
    	return name;
    }
    
    public void setName(String name)
    { 
    	 this.name = name;
    }
    
    public String getUnitSize()
    { 
    	return unitSize;
    }
    
    public void setUnitSize(String unitSize)
    { 
    	 this.unitSize = unitSize;
    }
    
    public int getQuantity()
    { 
    	return quantity;
    }
    
    public void setQuantity(int quantity)
    { 
    	 this.quantity = quantity;
    }
    
    public double getUnitPrice()
    { 
    	return Receipt.setPrecision(unitPrice);
    }
    
    public void setUnitPrice(double unitPrice)
    { 
    	 this.unitPrice = unitPrice;
    }    
    
    public double getPrice()
    { 
    	return this.quantity * Receipt.setPrecision(this.unitPrice);
    }
    
    public void setPrice(double price)
    { 
    	 this.price = price;
    } 
    
    public static double computeTotal(String s, String t)
    { 
    	//get the tax rate
    	t = t.substring(0, t.length()-1);
    	
    	//convert to doubles
    	double subtotal = Double.parseDouble(s);
    	double tax = Double.parseDouble(t); 	
    	
    	return subtotal += subtotal * (tax / 100);
    }
    
    public static double computeSubTotal(ObservableList<Product> products, String d)
    { 
    	double discount = 0.0;
    	
    	//remove percentage 
    	if(!d.isEmpty() && d != null)
    	{	
    	   d = d.substring(0, d.length()-1);
    	   
       	   //convert to decimal
       	   discount = Double.parseDouble(d);
    	}
    	
    	double result = 0.0;
    	for(Product p : products)
    	{ 
    		if(!p.getUnitSize().equals("0") && (!p.getName().contains("send") ||
    		   !p.getName().contains("receive")))
    		{	
    	       result = result + p.getUnitPrice() * p.getQuantity();
    		}   	
    	}
    	
    	//apply discount, if any
    	if(discount > 0.0)
    	{ 
    		result = result - (discount/100 * result);
    	}	
    	
    	return result;
    }
    
    /*
     * Delete product from the database
     */
    public static void deleteProduct(String name)
    {
       String query = "CALL delete Product(?,?)";
       
       try
       {
    	  java.sql.Connection conn = Session.openDatabase();
    	  PreparedStatement ps = conn.prepareStatement(query);
    	  
    	  //set parameters
    	  ps.setString(1, name);
    	  ps.setString(2, Configs.getProperty("StoreCode"));
    	  
    	  //execute
    	  ps.execute();
    	  
    	  //close
    	  conn.close();
       }
       catch(Exception e)
       {
          e.printStackTrace();	   
       }
    }
  
}
