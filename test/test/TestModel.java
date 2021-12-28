package test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import inventory.entity.Book;
import inventory.model.Model;
import inventory.model.Status;
import inventory.model.Status.StatusCode;

/**
 * A JUnit test for the Model
 * @author thean
 *
 */
public class TestModel {
	private Model model;
	private Connection conn;
	private Book book1, book2, book3;
	private ArrayList<Book> allBooks = insertEmptyArrayList(new Book());
	
	private void dropTable() throws SQLException {
		conn.createStatement().execute("DROP ALL OBJECTS DELETE FILES;");
	}
	
	/*
	 * Insert an item into an empty ArrayList and return it.
	 * This is useful for preparing data to pass into model.retrieve().
	 */
	private <T> ArrayList<T> insertEmptyArrayList(T item){
		ArrayList<T> uList = new ArrayList<>();
		uList.add(item);
		return uList;
	}
	
	@Before
	public void setUp() throws SQLException {
		conn = DriverManager.getConnection("jdbc:h2:mem:testdb");
		dropTable();
		
		model = new Model(conn);
		book1 = new Book("book1", 2001);
		book2 = new Book("book2", 2002);
		book3 = new Book("book3", 2003);
	}
	
	@Test
	public void testCreate() {
		Status status = model.createItem(book1);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		
		// Duplicate book
		status = model.createItem(book1);
		assertEquals(StatusCode.FAIL, status.getStatusCode());
	}
	
	@Test
	public void testDelete() {
		model.createItem(book1);
		model.createItem(book2);
		//legal delete
		Status status = model.deleteItem(book1);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		status = model.retrieveItem(allBooks, true);
		assertEquals(1, status.getTransactionOutput().size());
		assertEquals(book2.toString(), status.getTransactionOutput().get(0).toString());
		
		// illegal delete
		status = model.deleteItem(book3);
		assertEquals(StatusCode.FAIL, status.getStatusCode());
	}
	
	@Test
	public void testUpdate() throws SQLException{
		// Legal Update
		model.createItem(book1);
		Book bookUpd = new Book(book1.getBookName(), 1999);
		Status status = model.updateItem(book1, bookUpd);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		status = model.retrieveItem(allBooks, true);
		assertEquals(1, status.getTransactionOutput().size());
		assertEquals(bookUpd.toString(), status.getTransactionOutput().get(0).toString());
		
		//reset
		dropTable();
		
		//Illegal Update - different ID
		model.createItem(book1);
		bookUpd = new Book("RandomName", 1999);
		status = model.updateItem(book1, bookUpd);
		assertEquals(StatusCode.FAIL, status.getStatusCode());
		// book1 should be unaffected
		status = model.retrieveItem(allBooks, true);
		assertEquals(1, status.getTransactionOutput().size());
		assertEquals(book1.toString(), status.getTransactionOutput().get(0).toString());
		
		// Illegal Update - no such record to update
		status = model.updateItem(book2, book2);
		assertEquals(StatusCode.FAIL, status.getStatusCode());
	}
	
	@Test
	public void testRetrieve() {
		model.createItem(book1);
		model.createItem(book2);
		model.createItem(book3);
		
		//Retrieve all books
		Status status = model.retrieveItem(allBooks, true);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		List<?> results = status.getTransactionOutput();
		List<Book> books = insertEmptyArrayList(book1);
		books.add(book2);
		books.add(book3);
		
		for (int i = 1; i< results.size(); i++) {
			Object b = results.get(i);
			Book castedBook = (Book) b;
			assertEquals(books.get(i).toString(), castedBook.toString());
		}
		
		List<Book> b1List = insertEmptyArrayList(book1);
		// include book1 only
		status = model.retrieveItem(b1List, true);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		assertEquals(1, status.getTransactionOutput().size());
		assertEquals(book1.toString(), status.getTransactionOutput().get(0).toString());
		
		//exclude book1 only
		status = model.retrieveItem(b1List, false);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		assertEquals(2, status.getTransactionOutput().size());
		assertEquals(book2.toString(), status.getTransactionOutput().get(0).toString());
		assertEquals(book3.toString(), status.getTransactionOutput().get(1).toString());
		
		List<Book> b12List = insertEmptyArrayList(book1);
		b12List.add(book2);
		// Include book1 and book2
		status = model.retrieveItem(b12List, true);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		assertEquals(2, status.getTransactionOutput().size());
		assertEquals(book1.toString(), status.getTransactionOutput().get(0).toString());
		assertEquals(book2.toString(), status.getTransactionOutput().get(1).toString());
		
		// Exclude book1 and book2
		status = model.retrieveItem(b12List, false);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		assertEquals(1, status.getTransactionOutput().size());
		assertEquals(book3.toString(), status.getTransactionOutput().get(0).toString());
	}
	
	@Test
	public void testCreateRetrieveCart() {
		Status status = model.retrieveCart();
		assertEquals(0, status.getTransactionOutput().size());
		
		// Add a book
		status = model.createCart(book1);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		
		status = model.retrieveCart();
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		assertEquals(1, status.getTransactionOutput().size());
		assertEquals(book1.toString(), status.getTransactionOutput().get(0).toString());
		
		// Add another book
		model.createCart(book2);
		status = model.retrieveCart();
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		assertEquals(2, status.getTransactionOutput().size());
		assertEquals(book1.toString(), status.getTransactionOutput().get(0).toString());
		assertEquals(book2.toString(), status.getTransactionOutput().get(1).toString());
		
	}
	
	@Test
	public void testDeleteCart() {
		model.createCart(book1);
		model.createCart(book2);
		// legal delete
		Status status = model.deleteCart(book1);
		assertEquals(StatusCode.SUCCESS, status.getStatusCode());
		status = model.retrieveCart();
		assertEquals(1, status.getTransactionOutput().size());
		assertEquals(book2.toString(), status.getTransactionOutput().get(0).toString());
		
		//illegal delete
		status = model.deleteCart(book3);
		assertEquals(StatusCode.FAIL, status.getStatusCode());
	}
}
