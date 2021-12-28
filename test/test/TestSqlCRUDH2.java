package test;


import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import inventory.entity.User;
import object.db.map.SqlCRUD;
import object.db.map.SqlCRUDFactory;

/**
 * A JUnit test for the SqlCRUDH2 class
 * @author thean
 *
 */
public class TestSqlCRUDH2 {
	private SqlCRUD sqlCRUD;
	private Connection conn;
	private User user1, user2;
	private ArrayList<User> allUser = insertEmptyArrayList(new User());
	
	/*
	 * Insert an item into an empty ArrayList and return it.
	 * This is useful for preparing data to pass into model.retrieve().
	 */
	private <T> ArrayList<T> insertEmptyArrayList(T item){
		ArrayList<T> uList = new ArrayList<>();
		uList.add(item);
		return uList;
	}
	
	private void dropTable() throws SQLException {
		conn.createStatement().execute("DROP ALL OBJECTS DELETE FILES;");
	}
	
	@Before
	public void setUp() throws SQLException {
		conn = DriverManager.getConnection("jdbc:h2:mem:testdb");
		dropTable();
		
		sqlCRUD = new SqlCRUDFactory().getSqlCRUD(conn);
		user1 = new User ("Thean", "pw", "email");
		user2 = new User ("Thean2", "pw2", "email2");
	}
	
	@Test
	public void testCreate() throws Exception {
		sqlCRUD.create(user1);
		List<User> uList = sqlCRUD.retrieve(allUser, true);
		assertEquals(1, uList.size());
		assertEquals(user1.toString(), uList.get(0).toString());
	}
	
	@Test
	public void testUpdate() throws Exception {
		sqlCRUD.create(user1);
		User user1Update = new User("Thean", "pwUpdate", "emailUpdate");
		sqlCRUD.update(user1, user1Update);
		List<User> uList = sqlCRUD.retrieve(allUser, true);
		assertEquals(1, uList.size());
		assertEquals(user1Update.toString(), uList.get(0).toString());
	}
	
	@Test
	public void testDelete() throws Exception {
		// Add and delete a user
		sqlCRUD.create(user1);
		sqlCRUD.delete(user1);
		List<User> uList =sqlCRUD.retrieve(allUser, true);
		assertEquals(0, uList.size());
		
		// Add two users. Delete the first user
		sqlCRUD.create(user1);
		sqlCRUD.create(user2);
		uList = sqlCRUD.retrieve(allUser, true);
		assertEquals(2, uList.size());
		assertEquals(user1.toString(), uList.get(0).toString());
		assertEquals(user2.toString(), uList.get(1).toString());
		
		// Delete the first user 
		sqlCRUD.delete(user1);
		uList = sqlCRUD.retrieve(allUser, true);
		assertEquals(1, uList.size());
		assertEquals(user2.toString(), uList.get(0).toString());
	}
	
	@Test
	public void testRetrieve() throws Exception {
		User user3 = new User("Thean3", "pw3", "email3");
		sqlCRUD.create(user1);
		sqlCRUD.create(user2);
		sqlCRUD.create(user3);
		
		// all users - no specification
		List<User> uList = sqlCRUD.retrieve(allUser, true);
		assertEquals(3, uList.size());
		assertEquals(user1.toString(), uList.get(0).toString());
		assertEquals(user2.toString(),uList.get(1).toString());
		assertEquals(user3.toString(), uList.get(2).toString());
		
		List<User> u1List = insertEmptyArrayList(user1);
		// include user1 only
		uList = sqlCRUD.retrieve(u1List, true);
		assertEquals(1, uList.size());
		assertEquals(user1.toString(), uList.get(0).toString());
		
		//exclude user1 only
		uList = sqlCRUD.retrieve(u1List, false);
		assertEquals(2, uList.size());
		assertEquals(user2.toString(), uList.get(0).toString());
		assertEquals(user3.toString(), uList.get(1).toString());
		
		List<User> u12List = insertEmptyArrayList(user1);
		u12List.add(user2);
		// include specifically both user1 and user2
		uList = sqlCRUD.retrieve(u12List, true);
		assertEquals(2, uList.size());
		assertEquals(user1.toString(), uList.get(0).toString());
		assertEquals(user2.toString(), uList.get(1).toString());
		
		//exclude specifically both user1 and user2
		uList = sqlCRUD.retrieve(u12List, false);
		assertEquals(1, uList.size());
		assertEquals(user3.toString(), uList.get(0).toString());
	}
}
