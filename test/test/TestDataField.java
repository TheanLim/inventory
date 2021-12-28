package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import object.db.map.DataField;

/**
 * A JUnit test for the DataField class
 * @author thean
 *
 */
public class TestDataField {
	private DataField df;
	@Test
	public void testConstructors() {
		df = new DataField(true, "user_name", "username", String.class, "Thean");
		String expected = 
				"isId: true"+
				"\ndbColName: user_name"+
				"\nfieldName: username"+
				"\nfieldDataType: class java.lang.String"+
				"\nfieldValue: Thean";
		assertEquals(expected, df.toString());
		
		// Without Field value
		df = new DataField(true, "user_name", "username", String.class);
		expected = 
				"isId: true"+
				"\ndbColName: user_name"+
				"\nfieldName: username"+
				"\nfieldDataType: class java.lang.String"+
				"\nfieldValue: null";
		assertEquals(expected, df.toString());
	}
	
	@Test
	public void testGetters() {
		df = new DataField(true, "user_name", "username", String.class, "Thean");
		assertTrue(df.isId());
		assertEquals("user_name", df.getDbColName());
		assertEquals("username", df.getFieldName());
		assertEquals(String.class,df.getFieldDataType());
		assertEquals("Thean", df.getFieldValue());
		
		// Without Field value
		df = new DataField(true, "user_name", "username", String.class);
		assertTrue(df.isId());
		assertEquals("user_name", df.getDbColName());
		assertEquals("username", df.getFieldName());
		assertEquals(String.class, df.getFieldDataType());
		assertEquals(null, df.getFieldValue());
	}

}
