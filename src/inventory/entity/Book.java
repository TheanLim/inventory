package inventory.entity;

import object.db.map.Column;
import object.db.map.Table;

/**
 * This class represents a book.
 * A Book has a book name and a published year.
 * @author thean
 *
 */
@Table(tableName = "book")
public class Book {
	@Column(id = true)
	private String bookName;
	@Column(columnName = "publish_year")
	private Integer year;
	
	/*
	 * A No-arg constructor is needed to populate
	 * database results to object instances.
	 */
	public Book() {} 
	
	/**
	 * Constructs a Book object
	 * @param bookName the name of this book
	 * @param year the year when this book is published
	 */
	public Book(String bookName, Integer year) {
		this.bookName = bookName;
		this.year = year;
	}
	
	/**
	 * Get this book name
	 * @return this book name
	 */
	public String getBookName() {
		return bookName;
	}
	
	/**
	 * Get this book published year
	 * @return this book published year
	 */
	public Integer getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		return "BookName: "+bookName +
				", Year: "+ year;
	}
}
