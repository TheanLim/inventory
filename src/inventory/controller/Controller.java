package inventory.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import inventory.entity.Book;
import inventory.entity.User;
import inventory.model.IModel;
import inventory.model.Status;
import inventory.model.Status.StatusCode;
import inventory.view.IView;
import inventory.view.IView.Page;

/**
 * An implementation of the IController interface.
 * <br> This Controller is currently using console to show any possible errors.
 * @author thean
 *
 */
public class Controller implements IController {
	private IModel model;
	private IView view;
	
	/**
	 * Constructs a Controller and set itself as the view's controller
	 * @param model an IModel object
	 * @param view a IView object
	 */
	public Controller (IModel model, IView view) {
		 this.model = model;
		 this.view = view;
		 // Set the View's Controller to this controller
		 view.addController(this);
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
	
	/*
	 * Whether a user is an admin. 
	 * Currently assume that's only one admin in the system
	 * with the userName equals to "admin"
	 */
	private boolean isAdmin(String userName) {
		return userName.equals("admin");
	}

	@Override
	public void logIn(String userName, String password) {
			User user= new User(userName, password, null);
			
			Status status = model.retrieveItem(insertEmptyArrayList(user), true);
			if (status.getStatusCode()==StatusCode.SUCCESS) {
				@SuppressWarnings("unchecked")
				List<User> uList = (List<User>) status.getTransactionOutput();
				if (uList.size()>0) {
					// Refresh tables whenever someone login successfully
					getAllBooks();
					getAllUsers();
					view.showMessage("Successfully logged in!");
					if(isAdmin(userName)) {
						//view.showAdminPage();
						view.showPage(Page.ADMIN);
					} else {
						//view.showUserPage();
						view.showPage(Page.USER);
					}
				} else {
					view.showMessage("Wrong user/password.");
				}
			} else {
				System.out.println("Status fail at controller.logIn()");
				System.out.println(status.getError());
			}
	}
	
	@Override
	public void signUp(String userName, String password, String email) {
		Status status = model.createItem(new User(userName, password, email));
		
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			view.showMessage("Successfully signed up!");
			//view.showLogIn();
			view.showPage(Page.LOGIN);
			getAllUsers();
		} else {
			view.showMessage("Fail to sign up.");
			System.out.println("Status fail at controller.addUser()");
			System.out.println(status.getError());
		}
	}
	
	@Override
	public void addUser(String userName, String password, String email) {
		Status status = model.createItem(new User(userName, password, email));
		
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			view.showMessage("Successfully added user - "+ userName);
			getAllUsers();
		} else {
			view.showMessage("Fail to add user - " + userName);
			System.out.println("Status fail at controller.addUser()");
			System.out.println(status.getError());
		}
	}
	
	@Override
	public void getAllUsers() {
		
		Status status = model.retrieveItem(insertEmptyArrayList(new User()), true);
		
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			@SuppressWarnings("unchecked")
			List<User> uList= (List<User>) status.getTransactionOutput();
			
			if (uList.size()>0) {
				view.showUsers(uList);
			} else {
				System.out.println("No users to display.");
			}
		} else {
			System.out.println("Status fail at controller.getAllUsers()");
			System.out.println(status.getError());
		}
	}
	
	@Override
	public void updateUser(String userName, String newPassword, String newEmail) {
		updateUser(userName, newPassword, newEmail, true);
	}
	
	@Override
	public void updateUser(String userName, String newPassword, String newEmail, boolean refresh) {
		
		Status status = model.updateItem(new User(userName, null, null), new User(userName, newPassword, newEmail));
		
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			view.showMessage("Successfully updated user - "+userName);
			if(refresh) {
				getAllUsers();
			}
		} else {
			view.showMessage("Fail to update user - "+userName);
			System.out.println("Status fail at controller.updateUser()");
			System.out.println(status.getError());
		}
	}

	@Override
	public void deleteUser(String userName) {
		Status status = model.deleteItem(new User(userName, null, null));
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			view.showMessage("Successfully removed user - "+userName);
			getAllUsers();
		} else {
			view.showMessage("Fail to remove user - "+userName);
			System.out.println("Status fail at controller.deleteUser()");
			System.out.println(status.getError());
		}
	}
	
	@Override
	public void addBook(String bookName, Integer year) {
		Status status = model.createItem(new Book(bookName, year));
		
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			view.showMessage("Successfully added a book!");
			getAllBooks();
		} else {
			view.showMessage("Fail to add a book!");
			System.out.println("Status fail at controller.addBook()");
			System.out.println(status.getError());
		}
	}
	
	@Override
	public void getAllBooks() {
		Status status = model.retrieveItem(insertEmptyArrayList(new Book()), true);
		
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			@SuppressWarnings("unchecked")
			List<Book> bList= (List<Book>) status.getTransactionOutput();
			
			if (bList.size()>0) {
				view.showBooks(bList);
			} else {
				System.out.println("No books to display.");
			}
		} else {
			System.out.println("Status fail at controller.getAllBooks()");
			System.out.println(status.getError());
		}
	}
	
	@Override
	public void updateBook(String bookName, Integer newYear) {
		updateBook(bookName, newYear, true); 
	}
	
	@Override
	public void updateBook(String bookName, Integer newYear, boolean refresh) {

		Status status = model.updateItem(new Book(bookName, null), new Book(bookName, newYear));
		
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			view.showMessage("Successfully updated user - "+bookName);
			if(refresh) {
				getAllBooks();
			}
		} else {
			view.showMessage("Fail to update user - "+bookName);
			System.out.println("Status fail at controller.updateBook()");
			System.out.println(status.getError());
		}
	}
	
	@Override
	public void deleteBook(String bookName) {
		Status status = model.deleteItem(new Book(bookName, null));
		if (status.getStatusCode() == StatusCode.SUCCESS) {
			view.showMessage("Successfully removed user - "+bookName);
			getAllBooks();
		} else {
			view.showMessage("Fail to remove user - "+bookName);
			System.out.println("Status fail at controller.deleteBook()");
			System.out.println(status.getError());
		}
	}

	@Override
	public void addBookCart(String bookName, Integer bookYear) {
		Book b = new Book(bookName, bookYear);
		Status statusCart = model.createCart(b);
		if(statusCart.getStatusCode()== StatusCode.SUCCESS) {
			updateBookTable();
			view.addItemtoCart(b);
		} else {
			view.showMessage("Fail to add book to the cart");
			System.out.println("Status fail at controller.addBookCart()");
			System.out.println(statusCart.getError());
		}
	}

	@Override
	public void removeCart(Object item) {
		Status statusCart = model.deleteCart(item);
		if(statusCart.getStatusCode()== StatusCode.SUCCESS) {
			if(item instanceof Book) {
				updateBookTable();
			}
			view.removeItemFromCart(item);
		} else {
			view.showMessage("Fail to remove item from the cart");
			System.out.println("Status fail at controller.removeCart()");
			System.out.println(statusCart.getError());
		}
	}
	
	/*
	 * Repopulate the Book lists on the View
	 * but exclude books that are in the cart.
	 */
	private void updateBookTable() {
		Status statusCart = model.retrieveCart();
		
		if(statusCart.getStatusCode()== StatusCode.SUCCESS) {
			List<Book> cartBookList = statusCart.getTransactionOutput()
					.stream()
					.filter(o->o instanceof Book)
					.map(o-> (Book) o)
					.collect(Collectors.toList());
			
			if(cartBookList.size()>0) {
				Status status = model.retrieveItem(cartBookList, false);
				if (status.getStatusCode() == StatusCode.SUCCESS) {
					@SuppressWarnings("unchecked")
					List<Book> bookList = (List<Book>) status.getTransactionOutput();
					if (bookList.size()>0) {
						view.showBooks(bookList);
					} else {
						bookList.add(new Book());
						view.showBooks(bookList);
					}
				} else {
					System.out.println("Status fail at controller.updateBookTable()");
					System.out.println(status.getError());
				}
			} else {
				// normal populate all books
				getAllBooks();
			}	
		} else {
			System.out.println("Status fail at retrieving cart for controller.updateBookTable()");
			System.out.println(statusCart.getError());
		}
		
	}

	@Override
	public void checkoutCart() {
		Status statusCart = model.retrieveCart();
		if(statusCart.getStatusCode()== StatusCode.SUCCESS) {
			@SuppressWarnings("unchecked")
			List<Object> cart = (List<Object>) statusCart.getTransactionOutput();
			if (cart.size()==0) {
				view.showMessage("Please add in items before checking out.");
				return;
			}
			
			Iterator<?> cartIt = cart.iterator();
			while(cartIt.hasNext()) {
				Object cartItem = cartIt.next();
				Status status = model.deleteItem(cartItem);
				
				if (status.getStatusCode() == StatusCode.SUCCESS) {
					cartIt.remove();
					view.removeItemFromCart(cartItem);
				} else {
					view.showMessage("Fail to checkOut "+cartItem.toString()+". It is no longer exists in the system");
					view.showMessage("Please remove it from the cart manually.");
				}
			}
			// repopulate all items such as books
			getAllBooks();
			view.showMessage("Done checking out the cart.");
		} else {
			System.out.println("Status fail at retrieving cart for controller.checkoutCart()");
			System.out.println(statusCart.getError());
		}
	}

	@Override
	public void emptyCart() {
		Status statusCart = model.retrieveCart();
		if(statusCart.getStatusCode()== StatusCode.SUCCESS) {
			statusCart.getTransactionOutput().clear();
			view.emptyCart();
			// repopulate all items such as books
			getAllBooks();
		}else {
			System.out.println("Status fail at retrieving cart for controller.emptyCart()");
			System.out.println(statusCart.getError());
		}
	}
	
	@Override
	public String toString() {
		return "Model: " + model +
				", View: "+view;
	}
		
}
