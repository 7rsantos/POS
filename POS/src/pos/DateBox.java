package pos;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.LocalDate;
public class DateBox {

	/*
	 * Create layout containing a date picker
	 */
	@SuppressWarnings("unused")
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
	            
	            setDisable(true);

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
	     layout.setAlignment(Pos.BOTTOM_LEFT);
	     layout.setPadding(new Insets(20,140,20, 0));
	     layout.getChildren().addAll(date, current_date);
	     return layout;
	}
	          
	
}
	
	

