package pos;


import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Date;
public class DateBox {

	/*
	 * Create layout containing a date picker
	 */
	public static VBox createDateBox()
	{ 
		DatePicker current_date = new DatePicker();
		
	    current_date.setValue(LocalDate.now());
	    
	     Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
	      @Override
	      public DateCell call(final DatePicker datePicker) {
	        return new DateCell() {
	          @Override
	          public void updateItem(LocalDate item, boolean empty) {
	            super.updateItem(item, empty);

	            if (item.isBefore(current_date.getValue().plusDays(1))) {
	              setDisable(true);
	              setStyle("-fx-background-color: #EEEEEE;");
	            }
	          }
	        };
	      } 
	     };
	     
	     Label date = new Label("Date:");
	     VBox layout = new VBox();
	     layout.getChildren().addAll(date, current_date);
	     return layout;
	}
	          
	
}
	
	

