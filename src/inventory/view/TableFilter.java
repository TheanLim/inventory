package inventory.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * A class with methods used to filter a JTable.
 * @author thean
 *
 */
public class TableFilter {
	/**
	 * Filter a {@link JTable} table contents uses regular expressions.
	 * <br> Multiple inputs from the textField can be separated by a blank space. The first input is used
	 * to filter the first table column, the second input for the second table column, and so forth.
	 * Multiple inputs/search criteria are chained together using the AND clause.
	 * <br> Inputs/Search criteria with incorrect regular expression syntax is ignored.
	 * Besides, the search criteria is set to be case insensitive.
	 * @param sorter a {@link TableRowSorter} sorter
	 * @param textField a {@code JTextField} textfield which the input is used as filtering criteria.
	 */
	// revised from https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#sorting
	// and https://stackoverflow.com/a/10135257
	public static void regexAndFilter(TableRowSorter<?extends TableModel>sorter, JTextField textField) {
		if (sorter == null) {
			return;
		}
		
	    RowFilter<TableModel, Object> rf = null;
	    List<RowFilter<Object,Object>> rfs = 
	            new ArrayList<RowFilter<Object,Object>>();
	    try {
	    	String text = textField.getText();
	    	String[] textArray = text.split(" ");
	    	int colNum = sorter.getModel().getColumnCount();
	    	int upperBound = Math.min(textArray.length, colNum);
	    	
	    	for (int i = 0; i < upperBound; i++) {
	    		rfs.add(RowFilter.regexFilter("(?i)" + textArray[i], i));
	    	}
	        rf = RowFilter.andFilter(rfs);
	    } catch (java.util.regex.PatternSyntaxException e) {
	        return;
	    }
	    sorter.setRowFilter(rf);
	}
}
