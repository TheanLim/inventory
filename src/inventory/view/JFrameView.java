package inventory.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import inventory.controller.IController;
import inventory.entity.Book;
import inventory.entity.User;

/**
 * This is an implementation of the IView interface.
 * @author thean
 *
 */
public class JFrameView implements IView {
	private HomePage homePage;
	private SignUp signUp;
	private LogIn logIn;
	private AdminPage adminPage;
	private UserPage userPage;
	private ArrayList<JFrame>frames = new ArrayList<>();
	private IController controller;
	
	/*
	 * Initialize the view.
	 */
	private void init() {
		// Initialize all pages
		homePage = HomePage.getSingleton(controller);
		signUp = SignUp.getSingleton(controller);
		logIn = LogIn.getSingleton(controller);
		adminPage = AdminPage.getSingleton(controller);
		userPage = UserPage.getSingleton(controller);
		
		frames.add(homePage);
		frames.add(signUp);
		frames.add(logIn);
		frames.add(adminPage);
		frames.add(userPage);
		
		// Show homepage
		homePage.setVisible(true);
	}
	
	/*
	 * Hide all the pages
	 */
	private void hideAllPages() {
		for (JFrame f: frames) {
			f.setVisible(false);
		}
	}

	@Override
	public void addController(IController controller) {
		this.controller=controller;
		init();
	}
	
	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	@Override
	public void showUsers(List<User> results) {
		adminPage.setTableUserResults(results);
	}
	
	@Override
	public void showBooks(List<Book> results) {
		adminPage.setTableBookResults(results);
		userPage.setTableBookResults(results);
	}

	@Override
	public void addItemtoCart(Object item) {
		userPage.addItemtoCart(item);
	}

	@Override
	public void removeItemFromCart(Object item) {
		userPage.removeItemFromCart(item);
	}

	@Override
	public void emptyCart() {
		userPage.emptyCart();
	}

	@Override
	public void showPage(Page page) {
		hideAllPages();
		switch(page) {
		case HOME:
			homePage.setVisible(true);
			break;
		case LOGIN:
			logIn.setVisible(true);
			break;
		case SIGNUP:
			signUp.setVisible(true);
			break;
		case USER:
			userPage.setVisible(true);
			break;
		case ADMIN:
			adminPage.setVisible(true);
		default:
			break;
		}
	}

}
