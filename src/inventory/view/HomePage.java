package inventory.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import inventory.controller.IController;

@SuppressWarnings("serial")
public class HomePage extends JFrame implements ActionListener {	
	private static HomePage homePage;
	private IController controller;
	
	private JButton btnSignUp, btnLogIn;
	private JLabel txtWelcomeToThe;

	private HomePage(IController controller) {
		this.controller = controller;
		setVisible(false);
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		setTitle("Home Page");
		
		txtWelcomeToThe = new JLabel();
		txtWelcomeToThe.setHorizontalAlignment(SwingConstants.CENTER);
		txtWelcomeToThe.setText("Welcome to the Book Inventory System");
		txtWelcomeToThe.setBounds(276, 65, 256, 66);
		getContentPane().add(txtWelcomeToThe);
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.setBounds(177, 379, 122, 36);
		btnSignUp.addActionListener(this);
		getContentPane().add(btnSignUp);
		
		btnLogIn = new JButton("Log In");
		btnLogIn.setBounds(531, 379, 122, 36);
		btnLogIn.addActionListener(this);
		getContentPane().add(btnLogIn);
		
		// Set Focus
		getRootPane().setDefaultButton(btnLogIn);
		btnLogIn.requestFocus();
	}
	
	@Override 
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == btnSignUp) {
			this.setVisible(false);
			SignUp.getSingleton(controller).setVisible(true);
		}
		
		if(evt.getSource() == btnLogIn) {
			this.setVisible(false);
			LogIn.getSingleton(controller).setVisible(true);
		}
	}
	
	/**
	 * Get a Singleton of the HomePage class
	 * @param controller an IController object
	 * @return a Singleton of the HomePage class
	 */
	public static HomePage getSingleton(IController controller) {
		if (homePage == null) {
			homePage = new HomePage(controller);
		}
		return homePage;
	}
}
