package inventory.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import inventory.model.Status.StatusCode;
import object.db.map.DbConfig;
import object.db.map.SqlCRUD;
import object.db.map.SqlCRUDFactory;

/**
 * An implementation of the IModel Interface.
 * <br>This Model uses an {@link SqlCRUD} object to perform CRUD operations onto items that are meant to be stores at a database.
 *  <br> Besides, it uses an {@code ArrayList} as the "Cart" to store a list of items.
 * @author thean
 *
 */
public class Model implements IModel {
	private SqlCRUD sqlCRUD;
	private LinkedList<Object> cart;
	
	/**
	 * Constructs a Model object
	 * @param conn a database connection
	 * @throws SQLException if database access error or other errors.
	 */
	public Model(Connection conn) throws SQLException {
		sqlCRUD = new SqlCRUDFactory().getSqlCRUD(conn);
		cart = new LinkedList<>();
	}
	
	@Override
	public <T> Status createItem(T item) {
		try {
			sqlCRUD.create(item);
			return new Status(StatusCode.SUCCESS);
		} catch (SQLException e) {
			return new Status(StatusCode.FAIL, e);
		}
	}
	
	@Override
	public <T> Status retrieveItem(List<T> itemList, boolean include){
		try {
			List<T> results = sqlCRUD.retrieve(itemList, include);
			return new Status(StatusCode.SUCCESS, results);
		} catch (Exception e) {
			return new Status(StatusCode.FAIL, e);
		}
	}
	
	@Override
	public <T> Status updateItem(T oldItem, T newItem) {
		DbConfig dbCfOld = new DbConfig(oldItem);
		DbConfig dbCfNew = new DbConfig(newItem);
		//Same ID check
		
		
		if(dbCfOld.hasId()) {
			Object oldIDValue = dbCfOld.getDataFields()
									   .stream()
									   .filter(df->df.isId())
									   .map(df-> df.getFieldValue())
									   .findFirst()
									   .get();
			
			Object newIDValue = dbCfNew.getDataFields()
					   				   .stream()
					   				   .filter(df->df.isId())
					   				   .map(df-> df.getFieldValue())
					   				   .findFirst()
									   .get();
			
			if(!oldIDValue.equals(newIDValue)) {
				return new Status(StatusCode.FAIL, "Both items ID are different. Same ID is required.");
			}
		}
		
		try {
			sqlCRUD.update(oldItem, newItem);
			return new Status(StatusCode.SUCCESS);
		} catch (SQLException e) {
			return new Status(StatusCode.FAIL, e);
		}
	}

	@Override
	public <T> Status deleteItem(T item){
		try {
			sqlCRUD.delete(item);
			return new Status(StatusCode.SUCCESS);
		} catch (SQLException e) {
			return new Status(StatusCode.FAIL, e);
		}
	}
	
	@Override
	public <T> Status createCart(T item) {
		boolean success = cart.add(item);
		if (success) {
			return new Status(StatusCode.SUCCESS);
		} else {
			return new Status(StatusCode.FAIL);
		}
	}
	
	@Override
	public Status retrieveCart() {
		return new Status(StatusCode.SUCCESS, cart);
	}
	
	@Override 
	public <T> Status deleteCart(T item) {
		boolean success = cart.remove(item);
		if (success) {
			return new Status(StatusCode.SUCCESS);
		} else {
			return new Status(StatusCode.FAIL);
		}
	}
	
}
