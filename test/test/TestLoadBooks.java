package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

import inventory.entity.Book;
import object.db.map.SqlCRUD;
import object.db.map.SqlCRUDFactory;

/**
 * This class is loading lots of books to the 
 * system to see whether it's performance is affected greatly or not.
 * @author thean
 *
 */
public class TestLoadBooks {
	private final int TOTALBOOK = 10000;
	private final int STRINGLENGTH = 7;
	private SqlCRUD sqlCRUD;
	private Connection conn;
	
	// Revised from https://www.programiz.com/java-programming/examples/generate-random-string
	private String randomStringGen(int length) {
	    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    
		for(int i = 0; i < length; i++) {
	      int index = random.nextInt(alphabet.length());
	      char randomChar = alphabet.charAt(index);
	      // append the character to string builder
	      sb.append(randomChar);
	    }
		return sb.toString();
	}
	
	public TestLoadBooks() throws SQLException {
		this.conn = DriverManager.getConnection("jdbc:h2:file:./mockDatabase/bookstore", "sa", ""); ;
	}
	
	public void loadBooks() throws SQLException {
		sqlCRUD = new SqlCRUDFactory().getSqlCRUD(conn);
		// Generate random string for bookname
		for (int i = 0; i<TOTALBOOK; i++) {		
			try {
				sqlCRUD.create(new Book(randomStringGen(STRINGLENGTH), 9999));
			} catch (SQLException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void deleteBooks() {
			try {
				conn.createStatement().executeUpdate("DELETE FROM BOOK WHERE publish_year = 9999 OR bookName like 'Sprint%';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) throws SQLException{
		// 1. Load books into system
		TestLoadBooks test = new TestLoadBooks();
		//test.loadBooks();
		
		// 2. Test run the program
		
		// 3. Delete all the loaded books
		test.deleteBooks();
	}
}
