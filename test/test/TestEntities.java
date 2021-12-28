package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import inventory.entity.Book;
import inventory.entity.User;

/**
 * A JUnit test for Entity User and Book
 * @author thean
 *
 */
public class TestEntities {
	private User user;
	private Book book;
	
	@Test
	public void testUserConstructor() {
		// No arg Constructor
		user = new User();
		assertEquals(user.toString(), "UserName: null, Password: null, Email: null");
		
		// Another constructor
		user = new User("Thean", "@10pqal", "thean@gmail.com");
		String uString = "UserName: Thean"+
    					 ", Password: @10pqal"+
    					 ", Email: thean@gmail.com";
		assertEquals(user.toString(), uString);
	}
	
	@Test
	public void testBookConstructor() {
		// No arg Constructor
		book = new Book();
		assertEquals(book.toString(), "BookName: null, Year: null");
		
		// Another constructor
		book = new Book("The Pets", 2021);
		String bString = "BookName: The Pets"+
						 ", Year: 2021";
		assertEquals(book.toString(), bString);
	}
	
	@Test
	public void testUserGetName() {
		user = new User("Thean", "@10pqal", "thean@gmail.com");
		assertEquals(user.getUserName(), "Thean");
	}
	
	@Test
	public void testBookGetName() {
		book = new Book("The Pets", 2021);
		assertEquals(book.getBookName(), "The Pets");
	}
	
	@Test
	public void testBookGetYear() {
		book = new Book("The Pets", 2021);
		assertEquals(book.getYear().intValue(), 2021);
	}
}
