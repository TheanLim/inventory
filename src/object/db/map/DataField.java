package object.db.map;

/**
 * Each field of an object could be used to construct an instance of DataField.
 * <br>  A DataField instance stores the field's name and its corresponding database column name,
 * the field's data type and value, and specifies whether the field is an id or not.
 * 
 * <br>This is closely related to {@link Column}.
 * @author thean
 *
 */
public class DataField { 
	private boolean isId; 
	private String dbColName; 
	private Class<?> fieldDataType;  
	private String fieldName; 
	private Object fieldValue; 
	
	/**
	 * Constructs a DataField object
	 * @param isId true if the object field is an id.
	 * @param dbColName the corresponding database column name of the object field. 
	 * @param fieldName the object field name. 
	 * @param fieldDataType a {@code Class} object identifying the declared type of the field.
	 * @param fieldValue the object field value.
	 */
	public DataField(boolean isId, String dbColName, String fieldName, Class<?> fieldDataType,  Object fieldValue) {
		this.isId = isId;
		this.dbColName = dbColName;
		this.fieldName = fieldName;
		this.fieldDataType = fieldDataType;
		this.fieldValue = fieldValue;
	}
	
	/**
	 * Constructs a DataField object
	 * @param isId true if the object field is an id.
	 * @param dbColName the corresponding database column name of the object field. 
	 * @param fieldName the object field name. 
	 * @param fieldDataType a {@code Class} object identifying the declared type of the field.
	 */
	public DataField(boolean isId, String dbColName, String fieldName, Class<?> fieldDataType) {
		this.isId = isId;
		this.dbColName = dbColName;
		this.fieldName = fieldName;
		this.fieldDataType = fieldDataType;
	}
	
	/**
	 * Whether the field is an id or not.
	 * @return true if the field is an id.
	 */
	public boolean isId() {
		return isId;
	}
	
	/**
	 * Get the corresponding database column name of the field.
	 * @return the corresponding database column name of the field.
	 */
	public String getDbColName() {
		return dbColName;
	}
	
	/**
	 * Get the field's name.
	 * @return the field's name.
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * Get the field data type.
	 * @return the field data type, represented by a {@code Class} object
	 */
	public Class<?> getFieldDataType() {
		return fieldDataType;
	}
	
	/**
	 * Get the field's value.
	 * @return the field's value.
	 */
	public Object getFieldValue() {
		return fieldValue;
	}
	
	@Override
	public String toString() {
		return "isId: "+ isId+
				"\ndbColName: "+dbColName+
				"\nfieldName: "+fieldName+
				"\nfieldDataType: "+ fieldDataType+
				"\nfieldValue: "+ fieldValue;
	}
}
