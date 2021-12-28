package object.db.map;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * A Utilities class.
 * @author thean
 *
 */
public class Utils {
	
	/**
	 * Drop a table from a database.
	 * @param conn a database connection
	 * @param dataClass the {@code Class} which the table name will be extracted from.
	 * 					The table name should be annotated using {@link Column} annotation.
	 * @throws SQLException if a database access error or other error occurs
	 */
	public static void dropTable(Connection conn,  Class<?> dataClass) throws SQLException {
		DbConfig dbCf = new DbConfig(dataClass);
		String tblName = dbCf.getTableName();
		
		String dropString = "DROP TABLE "+ tblName + ";";
		conn.createStatement().executeUpdate(dropString);
	}
	
	/**
	 * Populate a list of objects into a {@code TableModel}.
	 * The {@code TableModel} can be set to a {@code JTable} for display.
	 * <br> Note: The table's cells are made to be uneditable if the corresponding object's field 
	 * is annotated with {@link Column} {@code id = true}
	 * @param dataList a list of object to be populated
	 * @return a {@code TableModel} object populated by {@code dataList}
	 */
	@SuppressWarnings("serial")
	public static <T> TableModel listToTableModel(List<T> dataList) {
		T instance = dataList.get(0);
		Field[] fields = instance.getClass().getDeclaredFields();
		
		int colNum = fields.length;
		Vector<String> columnNames = new Vector<String>();
		
		for (int col = 0; col < colNum; col++) {
            columnNames.addElement(fields[col].getName());
        }
		
		 Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

        for (int i = 0; i < dataList.size(); i++) {
        	Vector<Object> aRow = new Vector<Object>();
            T objInstance = dataList.get(i);

            for (int j = 0; j < colNum; j++) {
            	fields[j].setAccessible(true);
                try {
                	aRow.addElement(fields[j].get(objInstance));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
            }
            rows.addElement(aRow);
        }
        
        // Collect a list of id index
        ArrayList<Integer> idIndexList = new ArrayList<Integer>();
        int colInd = 0;
		for (Field field: fields) {
			if(field.isAnnotationPresent(Column.class)){
	            Column col=field.getAnnotation(Column.class);
	            if(col.id()) {
	            	idIndexList.add(colInd);
	            }
			}
			colInd++;
		}
        
        return new DefaultTableModel(rows, columnNames) {
        	// Make the ID column uneditable
        	@Override
     	    public boolean isCellEditable(int row, int column) {
        		if(idIndexList.contains(column)) {
        			return false;
        		} else {
        			return true;
        		}
     	    }
        };
    }
}
