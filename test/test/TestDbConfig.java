package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import inventory.entity.Book;
import inventory.entity.User;
import object.db.map.DbConfig;
import object.db.map.Table;

/**
 * A JUnit for the DbConfig class
 * @author thean
 *
 */
public class TestDbConfig {
	private DbConfig dbCfUser, dbCfBook;
	private User user;
	private Book book;
	
	// A class that has no Table.tableName() and no fields (thus no id).
	@Table
	private class Foo{};
	
	@Before
	public void setUp() {
		user = new User("Thean", "@pw", "@email");
		book = new Book ("The Pets", 2021);
	}
	
	@Test
	public void testObjectConstructor() {
		dbCfUser = new DbConfig(user);
		dbCfBook = new DbConfig(book);
		/*
		 * Only User.userName (as user_name) and Book.year (as published_year) has Column.columnName specified.
		 * Testing whether the the default columnName (if not specified) is taken
		 * directly from the field name or not.
		 */
		String uString = 
				"TableName: users"+
				"\nuser_name\n\t"+
				"IsId: true\n\t"+
				"FieldName: userName\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: \'Thean\'\n\t"+
				"\npassword\n\t"+
				"IsId: false\n\t"+
				"FieldName: password\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: \'@pw\'\n\t"+
				"\nemail\n\t"+
				"IsId: false\n\t"+
				"FieldName: email\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: \'@email\'\n\t";		
		
		assertEquals(uString, dbCfUser.toString());
		
		String bString = 
				"TableName: book"+
				"\nbookName\n\t"+
				"IsId: true\n\t"+
				"FieldName: bookName\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: \'The Pets\'\n\t"+
				"\npublish_year\n\t"+
				"IsId: false\n\t"+
				"FieldName: year\n\t"+
				"FieldDataType: class java.lang.Integer\n\t"+
				"FieldValue: 2021\n\t";
		assertEquals(bString, dbCfBook.toString());		
	}
	
	@Test
	public void testClassConstructor() {
		dbCfUser = new DbConfig(User.class);
		dbCfBook = new DbConfig(Book.class);
		
		/*
		 * Only User.userName (as user_name) and Book.year (as published_year) has Column.columnName specified.
		 * Testing whether the the default columnName (if not specified) is taken
		 * directly from the field name or not.
		 */
		
		String uString = 
				"TableName: users"+
				"\nuser_name\n\t"+
				"IsId: true\n\t"+
				"FieldName: userName\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: null\n\t"+
				"\npassword\n\t"+
				"IsId: false\n\t"+
				"FieldName: password\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: null\n\t"+
				"\nemail\n\t"+
				"IsId: false\n\t"+
				"FieldName: email\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: null\n\t";		
		
		assertEquals(uString, dbCfUser.toString());
		
		String bString = 
				"TableName: book"+
				"\nbookName\n\t"+
				"IsId: true\n\t"+
				"FieldName: bookName\n\t"+
				"FieldDataType: class java.lang.String\n\t"+
				"FieldValue: null\n\t"+
				"\npublish_year\n\t"+
				"IsId: false\n\t"+
				"FieldName: year\n\t"+
				"FieldDataType: class java.lang.Integer\n\t"+
				"FieldValue: null\n\t";
		assertEquals(bString, dbCfBook.toString());		
	}
	
	@Test
	public void testGetTableName() {
		// Table.tableName is not empty
		dbCfUser = new DbConfig(User.class);
		dbCfBook = new DbConfig(book);
		
		assertEquals("users", dbCfUser.getTableName());
		assertEquals("book", dbCfBook.getTableName());
		
		//Foo's Table.tableName is empty; it should use the default class name
		DbConfig dbCfFoo = new DbConfig(Foo.class);
		assertEquals("Foo", dbCfFoo.getTableName());
	}
	
	@Test
	public void testHasId() {
		dbCfUser = new DbConfig(user);
		dbCfBook = new DbConfig(Book.class);
		
		assertTrue(dbCfUser.hasId());
		assertTrue(dbCfBook.hasId());
		
		// Foo has no fields thus no id
		DbConfig dbCfFoo = new DbConfig(Foo.class);
		assertFalse(dbCfFoo.hasId());
	}
	
	@Test
	public void testGetFields() {
		dbCfBook = new DbConfig(book);
		// only two fields
		assertEquals(2,dbCfBook.getDataFields().size());
		
		String firstField = "isId: true\n"
				+ "dbColName: bookName\n"
				+ "fieldName: bookName\n"
				+ "fieldDataType: class java.lang.String\n"
				+ "fieldValue: 'The Pets'";
		String secondField = "isId: false\n"
				+ "dbColName: publish_year\n"
				+ "fieldName: year\n"
				+ "fieldDataType: class java.lang.Integer\n"
				+ "fieldValue: 2021";
		String listFields = "["+firstField+", "+secondField+"]";
		assertEquals(listFields, dbCfBook.getDataFields().toString());
	}

}
