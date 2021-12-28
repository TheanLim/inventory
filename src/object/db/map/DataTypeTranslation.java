package object.db.map;

/**
 * The {@code DataTypeTranslation} class supports method for translating 
 * data type in Java to the corresponding data type in SQL.
 * @author thean
 *
 */
public class DataTypeTranslation {
	/**
	 * Get the corresponding SQL data type given a Java data type
	 * @param dataTypeClass a {@code Class} object identifying the Java data type
	 * @return
	 */
	public static String getSqlDataType(Class<?> dataTypeClass) {
		if(dataTypeClass==String.class) {
			return "VARCHAR(255)";
		}
		if(dataTypeClass==Integer.class|| dataTypeClass==int.class) {
			return "INT";
		}
		if(dataTypeClass==Long.class|| dataTypeClass==long.class) {
			return "BIGINT";
		}
		if(dataTypeClass==Double.class|| dataTypeClass==double.class) {
			return "DOUBLE";
		}
		return null;
	}
}