package inventory.controller;

import inventory.model.IModel;
import inventory.view.IView;

/**
 * The Controller interface for the inventory system.
 * This interface lists out the features supported by the controller.
 * <br>The controller methods do not return anything. Instead, the controller
 * is responsible to invoke the appropriate {@link IModel} and {@link IView} methods
 * during it's operations.
 * @author thean
 *
 */
public interface IController {

	/**
	 * Log in to the inventory system
	 * @param userName the user name. The user has to exist in the system to log in successfully
	 * @param password the user password
	 */
	void logIn(String userName, String password);
	
	/**
	 * Sign up as a new user .
	 * <br> If the sign up was successful, the operation ends by invoking {@code getAllUsers()} to repopulate the view.
	 * @param userName the user name. The user name is an ID and has to be unique.	
	 * @param password the user password
	 * @param email the user email
	 */
	void signUp(String userName, String password, String email);
	
	/**
	 * Add a new user
	 * <br> Contrasting to {@code signUp}, this is meant to be accessed by the admin.
	 * <br> If a new user was successfully added, the operation ends by invoking {@code getAllUsers()} to repopulate the view.
	 * @param userName the user name. The user name and ID and has to be unique.
	 * @param password the user password
	 * @param email the user email
	 */
	void addUser(String userName, String password, String email);
	
	/**
	 * Get all users stored in the system.
	 * <br> This method is responsible to invoke the appropriate 
	 * {@link IView} methods to populate results if needed.
	 */
	void getAllUsers();
	
	/**
	 * Update a user information.
	 * <br> If the user was successfully updated, the operation ends by invoking {@code getAllUsers()} to repopulate the view.
	 * @param userName the user name. The user has to exist in the system to be updated.
	 * @param newPassword the new user password
	 * @param newEmail the new user email
	 */
	void updateUser(String userName, String newPassword, String newEmail);
	
	/**
	 * Update a user information.
	 * @param userName the user name. The user has to exist in the system to be updated.
	 * @param newPassword the new user password
	 * @param newEmail the new user email
	 * @param refresh whether to invoke {@code getAllUsers()} to repopulate the view.
	 */
	void updateUser(String userName, String newPassword, String newEmail, boolean refresh);
	
	/**
	 * Delete a user
	 * <br> If the user was successfully deleted, the operation ends by invoking {@code getAllUsers()} to repopulate the view.
	 * @param userName the user name. The user has to exist in the system to be deleted.
	 */
	void deleteUser(String userName);
	
	/**
	 * Add a new book.
	 * <br> If the book was successfully added, the operation ends by invoking {@code getAllBooks()} to repopulate the view.
	 * @param bookName the book name. The book is an ID and has to be unique.
	 * @param year the book published year
	 */
	void addBook(String bookName, Integer year);
	
	/**
	 * Get all books stored in the system
	 * <br> This method is responsible to invoke the appropriate 
	 * {@link IView} methods to populate results if needed.
	 */
	void getAllBooks();
	
	/**
	 * Update a book information
	 * <br> If the book was successfully updated, the operation ends by invoking {@code getAllBooks()} to repopulate the view.
	 * @param bookName the book name. The book has to exist in the system to be updated.
	 * @param oldYear the book published year to be updated
	 * @param newYear the new book published year
	 */
	void updateBook(String bookName, Integer newYear);
	
	/**
	 * Update a book information
	 * @param bookName the book name. The book has to exist in the system to be updated.
	 * @param oldYear the book published year to be updated
	 * @param newYear the new book published year
	 * @param refresh  whether to invoke {@code getAllBooks()} to repopulate the view.
	 */
	void updateBook(String bookName, Integer newYear, boolean refresh);
	
	/**
	 * Delete a book.
	 * <br> If the book was successfully deleted, the operation ends by invoking {@code getAllBooks()} to repopulate the view.
	 * @param bookName the book name. The book has to exist in the system to be deleted.
	 */
	void deleteBook(String bookName);

	/**
	 * Add a book to the cart
	 * @param bookName the book name
	 * @param bookYear the book published year
	 */
	void addBookCart(String bookName, Integer bookYear);
	
	/**
	 * Remove an item from the cart.
	 * @param item an item to be removed from a cart
	 */
	void removeCart(Object item);
	
	/**
	 * Check out the items in a cart. Items that are checked out are removed from the system.
	 */
	void checkoutCart();
	
	
	/**
	 * Empty the cart. Reset the cart to have no items in it.
	 */
	void emptyCart();
	
}
