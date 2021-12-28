package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import inventory.controller.Controller;
import inventory.controller.IController;
import inventory.entity.Book;
import inventory.entity.User;
import inventory.model.IModel;
import inventory.model.Status;
import inventory.model.Status.StatusCode;
import inventory.view.IView;

public class TestController {
	class MockModel implements IModel{
		private StringBuilder log;
		private Status statusBook, statusUser, statusDefault;
		public MockModel(StringBuilder log, Status statusDefault){
			this.log = log;
			this.statusDefault = statusDefault;
		}
		public void setStatusBook(Status statusBook) {
			this.statusBook = statusBook;
		}
		public void setStatusUser(Status statusUser) {
			this.statusUser = statusUser;
		}
		public void setStatusDefault(Status statusDefault) {
			this.statusDefault = statusDefault;
		}
		@Override
		public String toString() {
			return "MockModel";
		}
		@Override
		public <T> Status createItem(T item) {
			log.append("\nInvoke: createItem()");
			log.append("\nInput: "+item);
			return statusDefault;
		}
		@Override
		public <T> Status updateItem(T oldItem, T newItem) {
			log.append("\nInvoke: updateItem()");
			log.append("\nInput: "+oldItem+", "+newItem);
			return statusDefault;
		}
		@Override
		public <T> Status deleteItem(T item) {
			log.append("\nInvoke: deleteItem()");
			log.append("\nInput: "+item);
			return statusDefault;
		}
		@Override
		public <T> Status retrieveItem(List<T> itemList, boolean include) {
			log.append("\nInvoke: retrieveItem()");
			log.append("\nInput: ");
			itemList.forEach(item -> log.append(item + ", "));
			log.append(include);
			if(itemList.get(0) instanceof Book) {
				return statusBook;
			} else {
				return statusUser;
			}
		}
		@Override
		public <T> Status createCart(T item) {
			log.append("\nInvoke: createCart()");
			log.append("\nInput: "+item);
			return statusDefault;
		}
		@Override
		public <T> Status deleteCart(T item) {
			log.append("\nInvoke: deleteCart()");
			log.append("\nInput: "+item);
			return statusDefault;
		}
		@Override
		public Status retrieveCart() {
			log.append("\nInvoke: retrieveCart()");
			return statusBook;
		}
	}
	
	class MockView implements IView{
		private StringBuilder log;
		public MockView(StringBuilder log) {
			this.log = log;
		}
		@Override
		public String toString() {
			return "MockView";
		}
		@Override
		public void addController(IController controller) {
			log.append("\nInvoke: addController()");
			log.append("\nInput: "+controller);
		}
		@Override
		public void showMessage(String message) {
			log.append("\nInvoke: showMessage()");
			log.append("\nInput: "+message);
		}
		@Override
		public void showUsers(List<User> results) {
			log.append("\nInvoke: showUsers()");
			log.append("\nInput: ");
			results.forEach(user -> log.append(user + ", "));
		}
		@Override
		public void showBooks(List<Book> results) {
			log.append("\nInvoke: showBooks()");
			log.append("\nInput: ");
			results.forEach(book -> log.append(book + ", "));
		}
		@Override
		public void addItemtoCart(Object item) {
			log.append("\nInvoke: addItemtoCart()");
			log.append("\nInput: "+item);
		}
		@Override
		public void removeItemFromCart(Object item) {
			log.append("\nInvoke: removeItemFromCart()");
			log.append("\nInput: "+item);
		}
		@Override
		public void emptyCart() {
			log.append("\nInvoke: emptyCart()");
		}
		@Override
		public void showPage(Page page) {
			log.append("\nInvoke: showPage()");
			log.append("\nInput: "+page);
		}
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
	
	
	private MockModel model;
	private MockView view;
	private Controller controller;
	private StringBuilder log;
	private Status userSuccessPop = new Status(StatusCode.SUCCESS, insertEmptyArrayList(new User("Thean", "pw", "email")));
	private Status bookSuccessPop = new Status(StatusCode.SUCCESS, insertEmptyArrayList(new Book("The Pets", 2000)));
	private Status userFail = new Status(StatusCode.FAIL);
	private Status bookFail = new Status(StatusCode.FAIL);
	
	@Before
	public void setUp() {
		log = new StringBuilder();
		
		model = new MockModel(log, new Status(StatusCode.SUCCESS));
		view = new MockView(log);
		controller = new Controller(model, view);
	}
	
	@Test
	public void testLogIn() {
		Status userSuccessEmpty = new Status(StatusCode.SUCCESS, new ArrayList<User>());
		// 1. LogIn transaction success but no such user
		model.setStatusUser(userSuccessEmpty);
		controller.logIn("Thean", "pw");
		String expected = "\nInvoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: Thean, Password: pw, Email: null, true\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Wrong user/password.";
		assertEquals(expected,log.toString());
		
		// 2. LogIn transaction success - Non Admin User
		setUp();
		model.setStatusUser(userSuccessPop);
		model.setStatusBook(bookSuccessPop);
		controller.logIn("Thean", "pw");
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: Thean, Password: pw, Email: null, true\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: BookName: null, Year: null, true\n"
				+ "Invoke: showBooks()\n"
				+ "Input: BookName: The Pets, Year: 2000, \n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true\n"
				+ "Invoke: showUsers()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email, \n"
				+ "Invoke: showMessage()\n"
				+ "Input: Successfully logged in!\n"
				+ "Invoke: showPage()\n"
				+ "Input: USER";
		assertEquals(expected,log.toString());
		
		//3. LogIn transaction success - Admin User
		setUp();
		model.setStatusUser(userSuccessPop);
		model.setStatusBook(bookSuccessPop);
		controller.logIn("admin", "pw");
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: admin, Password: pw, Email: null, true\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: BookName: null, Year: null, true\n"
				+ "Invoke: showBooks()\n"
				+ "Input: BookName: The Pets, Year: 2000, \n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true\n"
				+ "Invoke: showUsers()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email, \n"
				+ "Invoke: showMessage()\n"
				+ "Input: Successfully logged in!\n"
				+ "Invoke: showPage()\n"
				+ "Input: ADMIN";
		assertEquals(expected,log.toString());
		
		//4. LogIn transaction fail
		setUp();
		model.setStatusUser(userFail);
		controller.logIn("Thean", "pw");
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: Thean, Password: pw, Email: null, true";
	}
	
	@Test
	public void testSignUp() {
		//1. SignUp transaction success
		model.setStatusUser(userSuccessPop);
		controller.signUp("Thean", "pw", "email");
		String expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: createItem()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Successfully signed up!\n"
				+ "Invoke: showPage()\n"
				+ "Input: LOGIN\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true\n"
				+ "Invoke: showUsers()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email, ";
		assertEquals(expected,log.toString());
		
		//SignUp transaction fail
		setUp();
		model.setStatusDefault(userFail);
		controller.signUp("Thean", "pw", "email");
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: createItem()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Fail to sign up.";
		assertEquals(expected,log.toString());
	}
	
	@Test
	public void testAddUser() {
		//1. SignUp transaction success
		model.setStatusUser(userSuccessPop);
		controller.addUser("Thean", "pw", "email");
		String expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: createItem()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Successfully added user - Thean\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true\n"
				+ "Invoke: showUsers()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email, ";
		assertEquals(expected,log.toString());
		
		//SignUp transaction fail
		setUp();
		model.setStatusDefault(userFail);
		controller.addUser("Thean", "pw", "email");
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: createItem()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Fail to add user - Thean";
		assertEquals(expected,log.toString());
	}
	
	@Test
	public void testGetAllUsers() {
		//1. transaction success
		model.setStatusUser(userSuccessPop);
		controller.getAllUsers();
		String expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true\n"
				+ "Invoke: showUsers()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email, ";
		assertEquals(expected,log.toString());
		
		//2. transaction fail
		setUp();
		model.setStatusUser(userFail);
		controller.getAllUsers();
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true";
		assertEquals(expected,log.toString());
	}
	
	@Test
	public void testUpdateUser() {
		//1. transaction success
		model.setStatusUser(userSuccessPop);
		controller.updateUser("Thean", "pw2", "email2");
		String expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: updateItem()\n"
				+ "Input: UserName: Thean, Password: null, Email: null, UserName: Thean, Password: pw2, Email: email2\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Successfully updated user - Thean\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true\n"
				+ "Invoke: showUsers()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email, ";
		assertEquals(expected,log.toString());
		
		//2. transaction fail
		setUp();
		model.setStatusDefault(userFail);
		controller.updateUser("Thean", "pw2", "email2");
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: updateItem()\n"
				+ "Input: UserName: Thean, Password: null, Email: null, UserName: Thean, Password: pw2, Email: email2\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Fail to update user - Thean";
		assertEquals(expected,log.toString());
	}
	
	@Test
	public void testDeleteUser() {
		//1. transaction success
		model.setStatusUser(userSuccessPop);
		controller.deleteUser("Thean");
		String expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: deleteItem()\n"
				+ "Input: UserName: Thean, Password: null, Email: null\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Successfully removed user - Thean\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: UserName: null, Password: null, Email: null, true\n"
				+ "Invoke: showUsers()\n"
				+ "Input: UserName: Thean, Password: pw, Email: email, ";
		assertEquals(expected,log.toString());
		
		//2. transaction fail
		setUp();
		model.setStatusDefault(userFail);
		controller.deleteUser("Thean");
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: deleteItem()\n"
				+ "Input: UserName: Thean, Password: null, Email: null\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Fail to remove user - Thean";
		assertEquals(expected,log.toString());
	}

	@Test
	public void testAddBookCart() {
		//1. transaction success
		model.setStatusBook(bookSuccessPop);
		controller.addBookCart("The Pets", 2020);
		String expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: createCart()\n"
				+ "Input: BookName: The Pets, Year: 2020\n"
				+ "Invoke: retrieveCart()\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: BookName: The Pets, Year: 2000, false\n"
				+ "Invoke: showBooks()\n"
				+ "Input: BookName: The Pets, Year: 2000, \n"
				+ "Invoke: addItemtoCart()\n"
				+ "Input: BookName: The Pets, Year: 2020";
		assertEquals(expected,log.toString());
		
		//2. transaction fail
		setUp();
		model.setStatusDefault(bookFail);
		controller.addBookCart("The Pets", 2020);
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: createCart()\n"
				+ "Input: BookName: The Pets, Year: 2020\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Fail to add book to the cart";
		assertEquals(expected,log.toString());
	}
	
	@Test
	public void testRemoveCart() {
		//1. transaction success
		model.setStatusBook(bookSuccessPop);
		controller.removeCart(new Book("The Pets", 2000));
		String expected ="\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: deleteCart()\n"
				+ "Input: BookName: The Pets, Year: 2000\n"
				+ "Invoke: retrieveCart()\n"
				+ "Invoke: retrieveItem()\n"
				+ "Input: BookName: The Pets, Year: 2000, false\n"
				+ "Invoke: showBooks()\n"
				+ "Input: BookName: The Pets, Year: 2000, \n"
				+ "Invoke: removeItemFromCart()\n"
				+ "Input: BookName: The Pets, Year: 2000";
		assertEquals(expected,log.toString());
		
		//2. transaction fail
		setUp();
		model.setStatusDefault(bookFail);
		controller.removeCart(new Book("The Pets", 2000));
		expected = "\n"
				+ "Invoke: addController()\n"
				+ "Input: Model: MockModel, View: MockView\n"
				+ "Invoke: deleteCart()\n"
				+ "Input: BookName: The Pets, Year: 2000\n"
				+ "Invoke: showMessage()\n"
				+ "Input: Fail to remove item from the cart";
		assertEquals(expected,log.toString());
	}
}
