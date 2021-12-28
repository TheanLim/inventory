package inventory.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import inventory.controller.IController;

/**
 * A Log In Page.
 * @author thean
 *
 */
@SuppressWarnings("serial")
public class LogIn extends JFrame implements ActionListener {
	private static LogIn logIn;
	private IController controller;
	
	private JTextField textUserName;
	private JButton btnLogIn, btnBack;
	private JLabel lblUserName, lblPassword;
	private JPasswordField passwordField;
	
	private LogIn(IController controller) {
		this.controller = controller;
		setVisible(false);
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setTitle("Log In");
		
		lblUserName = new JLabel("UserName");
		lblUserName.setHorizontalAlignment(SwingConstants.LEFT);
		lblUserName.setBounds(208, 114, 112, 24);
		getContentPane().add(lblUserName);
		
		lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setBounds(208, 165, 112, 24);
		getContentPane().add(lblPassword);
		
		textUserName = new JTextField();
		textUserName.setBounds(378, 116, 185, 20);
		getContentPane().add(textUserName);
		textUserName.setColumns(10);
		
		btnLogIn = new JButton("Log In");
		btnLogIn.setBounds(474, 284, 89, 23);
		btnLogIn.addActionListener(this);
		getContentPane().add(btnLogIn);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(231, 284, 89, 23);
		btnBack.addActionListener(this);
		getContentPane().add(btnBack);
		
		passwordField = new JPasswordField();
		passwordField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// Show password
				passwordField.setEchoChar((char)0);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// Hide password
				passwordField.setEchoChar((Character) UIManager.get("PasswordField.echoChar"));
			}
		});
		passwordField.setBounds(378, 167, 185, 20);
		getContentPane().add(passwordField);
		
		getRootPane().setDefaultButton(btnLogIn);
		btnLogIn.requestFocus();
	}
	
	@Override 
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == btnLogIn) {
			String pw = new String(passwordField.getPassword());
			
			if(textUserName.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Input the User Name");
			}
			else if(pw.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Input the Password");
			} else {
				controller.logIn(textUserName.getText(), pw);
			}
		}
		
		if(evt.getSource() == btnBack) {
			this.setVisible(false);
			HomePage.getSingleton(controller).setVisible(true);
		}
	}
	
	/**
	 * Set the user name and password textfields into empty strings
	 */
	public void resetInput() {
		textUserName.setText("");
		passwordField.setText("");
	}
	
	/**
	 * Get a Singleton of the LogIn class
	 * @param controller an IController object
	 * @return a Singleton of the LogIn class
	 */
	public static LogIn getSingleton(IController controller) {
		if(logIn == null) {
			logIn = new LogIn(controller);
		}
		return logIn;
	}
}
