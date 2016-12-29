package pos;

import javafx.scene.control.TextField;

public class NumericTextField extends TextField {

	@Override
	public void replaceText(int start, int end, String text)
	{ 
	   if(validateInteger(text) || validatePoint(text))
	   { 
	      super.replaceText(start, end, text);	   
	   }	   
	}
	
	@Override
	public void replaceSelection(String text)
	{ 
	   super.replaceSelection(text);	
	}
	
	public boolean validateInteger(String text)
	{ 
		return text.matches("[0-9]*");
	}
	
	public boolean validatePoint(String text)
	{ 
		return text.matches("[.]*");
	}
	
}
