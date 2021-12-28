package inventory.view;

import java.util.List;

import inventory.controller.IController;
import inventory.entity.Book;
import inventory.entity.User;

public interface IView {
	
	/**
	 *  An enum of pages provided by the view
	 * @author thean
	 *
	 */
	public enum Page{
		HOME, LOGIN, SIGNUP, USER, ADMIN;
	}
	
	/**
	 * Show a page
	 * @param page the page to be shown
	 */
	void showPage(Page page);
	
	/**
	 * Add a Controller to the View.
	 * @param controller an IController object
	 */
	void addController(IController controller);
	
	/**
	 * Show a message on the view
	 * @param message a string of message
	 */
	void showMessage(String message);
	
	/**
	 * Show a list of users
	 * @param results a list of users
	 */
	void showUsers(List<User> results);
	
	/**
	 * Show a list of books
	 * @param results a list of books
	 */
	void showBooks(List<Book> results);
	
	/**
	 * Add an item to the cart
	 * @param item an item to be added to the cart
	 */
	void addItemtoCart(Object item);
	
	/**
	 * Remove an item from the cart
	 * @param item an item to be removed to the cart
	 */
	void removeItemFromCart(Object item);
	
	/**
	 * Empty the cart
	 */
	void emptyCart();
	
}


