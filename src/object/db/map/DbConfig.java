package object.db.map;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * DbConfig takes in an object or a {@code Class} object, extracts and stores its database-relevant configurations.
 * <br>This includes storing a database table name, a list of {@link DataField} objects and 
 * specifying whether an object or a {@code Class} object has an id or not.
 * 
 * @author thean
 *
 */
public class DbConfig {
	private String tblName;
	private List<DataField> dataFields;
	private boolean hasId;
	
	/**
	 * Constructs a DbConfig instance using an object
	 * @param data an object
	 */
	public DbConfig(Object data){
		Class<?> dataClass = data.getClass();
		setTableName(dataClass);
		
		dataFields = new ArrayList<>();
		// Collect field values
		Field[] fields = dataClass.getDeclaredFields();
		for (Field field: fields) {
			if(field.isAnnotationPresent(Column.class)){
	            Column column=field.getAnnotation(Column.class);
	            field.setAccessible(true);
	            Object fieldValue;
				try {
					fieldValue = (field.get(data) instanceof String)?"\'"+field.get(data)+"\'":field.get(data);
					DataField df = new DataField(column.id(), 
												(column.columnName().isEmpty())?field.getName():column.columnName(), 
												field.getName(),field.getType(), fieldValue);
		            dataFields.add(df);
		            
		            if(column.id()) {
		            	hasId = true;
		            }
		            
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Constructs a DbConfig instance using a {@code Class} object
	 * @param dataClass a {@code Class} object
	 */
	public DbConfig(Class<?> dataClass) {
		setTableName(dataClass);
		
		dataFields = new ArrayList<>();
		// Collect field values
		Field[] fields = dataClass.getDeclaredFields();
		for (Field field: fields) {
			if(field.isAnnotationPresent(Column.class)){
	            Column column=field.getAnnotation(Column.class);
	            DataField df = new DataField(column.id(), 
	            							(column.columnName().isEmpty())?field.getName():column.columnName(), 
	            							field.getName(),
	            							field.getType());
	            dataFields.add(df);
	            
	            if(column.id()) {
	            	hasId = true;
	            }
			}
		}
	}
	
	/*
	 * Set the table name.
	 * This is done using the {@link Table} annotation.
	 * @param dataClass a {@code Class} object.
	 */
	private void setTableName(Class<?> dataClass) {
		Table tbl = dataClass.getAnnotation(Table.class);
		tblName = (tbl.tableName().isEmpty())?dataClass.getSimpleName():tbl.tableName();
	}
	
	/**
	 * Get the table name.
	 * @return the table name
	 */
	public String getTableName() {
		return this.tblName;
	}
	
	/**
	 * Get the list of DataField objects.
	 * @return the list of DataField objects.
	 */
	public List<DataField> getDataFields(){
		return dataFields;
	}
	
	/**
	 * Whether parsed object/ {@code Class} object has an id.
	 * @return true if the parsed object/ {@code Class} object has an id.
	 */
	public boolean hasId() {
		return hasId;
	}
	
	@Override
	public String toString() {
		String out = "";
		out +="TableName: "+tblName;
		
		for(DataField df: dataFields) {
			out+="\n"+df.getDbColName() + "\n\t";
			out+="IsId: "+ df.isId()+ "\n\t";
			out+="FieldName: "+ df.getFieldName()+ "\n\t";
			out+="FieldDataType: "+ df.getFieldDataType()+ "\n\t";
			out+="FieldValue: "+ df.getFieldValue()+ "\n\t";
		}
		return out;
	}
}
