package pos;

import java.beans.*;
import java.util.ArrayList;

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
    	t = t.substring(0, t.length()-2);
    	
    	//convert to doubles
    	double subtotal = Double.parseDouble(s);
    	double tax = Double.parseDouble(t); 	
    	
    	return Receipt.setPrecision(subtotal * (tax / 100) + subtotal);
    }
    
    public static double computeSubTotal(ObservableList<Product> products, String d)
    { 
    	//remove percentage 
    	d = d.substring(0, d.length()-1);
    	
    	//convert to decimal
    	double discount = Double.parseDouble(d);
    	
    	double result = 0.0;
    	for(Product p : products)
    	{ 
    	    result = result + p.getUnitPrice() * p.getQuantity();	
    	}
    	
    	//apply discount, if any
    	if(discount > 0.0)
    	{ 
    		result = result - (discount/100 * result);
    	}	
    	
    	return result;
    }
  
}
