package object.db.map;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SqlCRUDFactory creates and returns the matching implementation
 * of {@link SqlCRUD} {@code interface} based on the type of SQL database.
 * @author thean
 *
 */
public class SqlCRUDFactory {
	
	/**
	 * Get a matching implementation of {@link SqlCRUD} {@code interface} based on the type of SQL database.
	 * @param conn a database connection
	 * @return a matching implementation of {@link SqlCRUD} {@code interface}
	 * @throws SQLException if a database access error or other error occurs
	 */
	public SqlCRUD getSqlCRUD(Connection conn) throws SQLException {
		String dbProduct = conn.getMetaData().getDatabaseProductName();
		if(dbProduct == "H2") {
			return new SqlCRUDImplH2(conn);
		}
		return null;
	}
}
