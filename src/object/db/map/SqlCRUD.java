package object.db.map;

import java.sql.SQLException;
import java.util.List;

/**
 * This is an interface to perform the CRUD operations onto SQL databases.
 * @author thean
 *
 */
public interface SqlCRUD {
	/**
	 * Create/Insert a data into a database
	 * @param data a data to be inserted into a database
	 * @throws SQLException if a database access error or other error occurs,
	 * 						OR if nothing is created.
	 */
	<T> void create(T data) throws SQLException;
	
	/**
	 * Retrieve data stored in a database matching condition(s).
	 * <br>Condition(s) is/are specified using {@code dataList} and {@code include}.
	 * Only with fields {@link Column} annotation are used as criteria for filtering.
	 * 
	 * @param dataList a list of data/objects. Their non-null, {@link Column} annotated fields 
	 * 				  will be used as filter criteria. Criteria specified by each data is combined.
	 * @param include whether to include (or exclude) results with criteria specified by {@code dataList}
	 * @return data stored in a database matching condition(s)
	 * @throws Exception if a database access error occurs, OR, the underlying data/object class doesn't have 
	 * 					 a no-arg constructor
	 */
	<T> List<T> retrieve(List<T> dataList, boolean include) throws Exception;
		
	/**
	 * Update a data stored in a database.
	 * <br> if exists, the data ID field is ignored. 
	 * This means that the old and new data do not need to have the same ID.
	 * <br>Only the {@code newData} non-null, non-ID fields are used for updates, and the {@code oldData} ID field is remained unchanged.
	 * 
	 * @param oldData a data to be updated by {@code newData}
	 * @param newData a data to update {@code oldData}
	 * @throws SQLException if a database access error or other error occurs, 
	 * 						OR if nothing are updated.
	 */
	<T> void update(T oldData, T newData) throws SQLException;
	
	/**
	 * Delete a data stored in a database
	 * @param data a data to be deleted
	 * @throws SQLException if a database access error or other error occurs,
	 * 						OR if nothing are updated.
	 */
	<T> void delete(T data) throws SQLException;
}
