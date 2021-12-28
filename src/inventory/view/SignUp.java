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
 * A Sign Up page.
 * @author thean
 *
 */
@SuppressWarnings("serial")
public class SignUp extends JFrame implements ActionListener {
	private static SignUp signUp;
	private IController controller;
	
	private JTextField textUserName, textEmail;
	private JButton btnSignUp, btnBack;
	private JLabel lblUserName, lblPassword, lblEmail;
	private JPasswordField passwordField;
	
	private SignUp(IController controller) {
		this.controller = controller;
		setVisible(false);
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setTitle("Sign Up");
		
		lblUserName = new JLabel("UserName");
		lblUserName.setHorizontalAlignment(SwingConstants.LEFT);
		lblUserName.setBounds(208, 33, 112, 24);
		getContentPane().add(lblUserName);
		
		lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setBounds(208, 84, 112, 24);
		getContentPane().add(lblPassword);
		
		lblEmail = new JLabel("Email");
		lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
		lblEmail.setBounds(208, 131, 112, 24);
		getContentPane().add(lblEmail);
		
		textUserName = new JTextField();
		textUserName.setBounds(378, 35, 185, 20);
		getContentPane().add(textUserName);
		textUserName.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(378, 86, 185, 20);
		passwordField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				passwordField.setEchoChar((char)0);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				passwordField.setEchoChar((Character) UIManager.get("PasswordField.echoChar"));
			}
		});
		getContentPane().add(passwordField);
		
		textEmail = new JTextField();
		textEmail.setColumns(10);
		textEmail.setBounds(378, 133, 185, 20);
		getContentPane().add(textEmail);
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.setBounds(474, 284, 89, 23);
		btnSignUp.addActionListener(this);
		getContentPane().add(btnSignUp);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(231, 284, 89, 23);
		btnBack.addActionListener(this);
		getContentPane().add(btnBack);
		
		getRootPane().setDefaultButton(btnSignUp);
		btnSignUp.requestFocus();
	}
	
	@Override 
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == btnSignUp) {
			String pw = new String(passwordField.getPassword());
			
			if(textUserName.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Input the User Name");
			}
			else if(pw.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Input the PassWord");
			}
			else if(textEmail.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Input the Email");
			} else {
				controller.signUp(textUserName.getText(), pw, textEmail.getText());
			}
		}
		
		if(evt.getSource() == btnBack) {
			this.setVisible(false);
			HomePage.getSingleton(controller).setVisible(true);
		}
	}
	
	/**
	 * Get a Singleton of the SignUp class
	 * @param controller an IController object
	 * @return a Singleton of the SignUp class
	 */
	public static SignUp getSingleton(IController controller) {
		if(signUp == null) {
			signUp = new SignUp(controller);
		}
		return signUp;
	}
}
