package object.db.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements {@code interface} {@link SqlCRUD} with H2 SQL syntax grammar.
 * @author thean
 *
 */
public class SqlCRUDImplH2 implements SqlCRUD{
	Connection conn;
	DbConfig dbCf;
	
	/**
	 * Constructs a SqlCRUDH2 object.
	 * @param conn a database connection.
	 */
	public SqlCRUDImplH2(Connection conn) {
		this.conn = conn;
	}
	
	/*
	 * Whether a table exists or not
	 */
	private boolean existTable(String tableName) {
		try {
			// check if the table is already exists
			// if not exists go to catch block and return false
			String sql = "SELECT * FROM "+ tableName;
			conn.createStatement().executeQuery(sql);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/*
	 * Create Table Using an object class information.
	 * The object class should have been annotated accordingly.
	 */
	private void createTable(Class<?> dataClass) throws SQLException {
		DbConfig dbCf = new DbConfig(dataClass);
		String tblName = dbCf.getTableName();
		
		String createString = "CREATE TABLE "+tblName+" ( ";
		
		for(DataField df: dbCf.getDataFields()) {
			// Currently Assuming each field is not null
            createString+=df.getDbColName()+" " +DataTypeTranslation.getSqlDataType(df.getFieldDataType())+" NOT NULL,";
            if(df.isId()) {
            	//Remove the last comma
            	createString = createString.replaceAll(",$", "");
            	createString+=" PRIMARY KEY,";
            }
		}
		
		// Remove last comma
		createString = createString.replaceAll(",$", "") + ");";
		
		conn.createStatement().executeUpdate(createString);
	
	}
	
	@Override
	public <T> void create(T data) throws SQLException{
		dbCf = new DbConfig(data);
		String tblName = dbCf.getTableName();
		
		if(!existTable(tblName)) {
			createTable(data.getClass());
		}
		
		String columnNames = "";
		String values = "";
		for(DataField df: dbCf.getDataFields()) {
			columnNames+= df.getDbColName()+",";
			values+= df.getFieldValue()+",";
		}
		
		// Remove the last commas and add ()
		columnNames = "(" + columnNames.replaceAll(",$", "") + ")";
		values = "(" + values.replaceAll(",$", "") + ")";
		
		// Combine pieces into the final SQL insert string
		String insertString = "INSERT INTO "+tblName + columnNames + " values " + values + ";";
		
		int create = conn.createStatement().executeUpdate(insertString);
		if (create == 0) {
			throw new SQLException("Nothing is created");
		}
	}

	@Override
	public <T> List<T> retrieve(List<T> dataList, boolean include) throws Exception{
		
		if(dataList.size()==0) {
			throw new IllegalArgumentException("The first argument cannot be an empty list.");
		}
		
		dbCf = new DbConfig(dataList.get(0));
		String tblName = dbCf.getTableName();
		
		if(!existTable(tblName)) {
			throw new Exception("No table - " +  tblName);
		}
		
		String selectString = "SELECT * FROM "+ tblName;
		String whereString = "";
		for (int i =0; i<dataList.size(); i++) {
			dbCf = new DbConfig(dataList.get(i));
			for(DataField df: dbCf.getDataFields()) {
				if(df.getFieldValue()!=null) {
					whereString+=df.getDbColName()+ "=" + df.getFieldValue() + " AND ";
				}
			}
			if (whereString !="") {
				// Remove last " AND "
				whereString = whereString.replaceAll(" AND $", "");
				whereString = "("+whereString+") OR ";
			}
		}
		
		if(whereString !="") {
			whereString = whereString.replaceAll(" OR $", "");
			// Add NOT if to exclude
			if(!include) {
				whereString = " WHERE NOT (" +whereString+ ");";
			} else {
				whereString=" WHERE " + whereString + ";";
			}
		}
		
		// Add in where clause
		selectString+=whereString;
		
		List<T> outList = new ArrayList<>();
		List<DataField> df = dbCf.getDataFields();
		
		Constructor<?> c = dataList.get(0).getClass().getConstructor();
		ResultSet rs = conn.createStatement().executeQuery(selectString);
		
		while(rs.next()){
			@SuppressWarnings("unchecked")
			T object = (T) c.newInstance();
			Class<? extends Object> objectC = object.getClass();
			
			for(int i = 0; i<df.size(); i++) {
				Field field = objectC.getDeclaredField(df.get(i).getFieldName());
				field.setAccessible(true);
				field.set(object, rs.getObject(df.get(i).getDbColName()));
			}
			outList.add(object);
		}
		rs.close();
		return outList;
	}
	
	@Override
	public <T> void update(T oldData, T newData) throws SQLException {
		DbConfig newDbCf = new DbConfig(newData);
		DbConfig oldDbCf = new DbConfig(oldData);
		String tblName = newDbCf.getTableName();
		
		if(!existTable(tblName)) {
			throw new SQLException("No table - " +  tblName);
		}
		
		String updateString = "UPDATE "+ tblName + " ";
		String setString = "SET ";
		
		for(DataField df: newDbCf.getDataFields()) {
			if(df.getFieldValue()!=null&& !df.isId()) { // cannot update ID
				setString+= df.getDbColName()+ "=" + df.getFieldValue() + ",";
			}
		}
		// Remove the last comma
		setString = setString.replaceAll(",$", "");
		
		String whereString = "";
		for(DataField df: oldDbCf.getDataFields()) {
			if(df.getFieldValue()!=null) {
				if(whereString =="") {
					whereString+=" WHERE ";
				}
				whereString+=df.getDbColName()+ "=" + df.getFieldValue() + " AND ";
			}
		}
		// Remove last " AND "
		whereString = whereString.replaceAll(" AND $", "")+";";
		//Combine parts into one
		updateString+=setString+whereString;
		
		int update = conn.createStatement().executeUpdate(updateString);
		if (update == 0) {
			throw new SQLException("Nothing is updated");
		}
	}
	
	@Override
	public <T> void delete(T data) throws SQLException {
		dbCf = new DbConfig(data);
		String tblName = dbCf.getTableName();
		
		if(!existTable(tblName)) {
			throw new SQLException("No table - " +  tblName);
		}
		
		String deleteString = "DELETE FROM "+ tblName+" WHERE ";
		for(DataField df: dbCf.getDataFields()) {
			if(df.getFieldValue()!=null) {
				deleteString+=df.getDbColName()+ "=" + df.getFieldValue() + " AND ";
			}
		}
		// Remove last " AND "
		deleteString = deleteString.replaceAll(" AND $", "")+";";
		
		int delete = conn.createStatement().executeUpdate(deleteString);
		if (delete == 0) {
			throw new SQLException("Nothing is deleted");
		}
	}
}
