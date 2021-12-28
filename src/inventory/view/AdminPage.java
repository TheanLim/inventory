package inventory.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import inventory.controller.IController;
import inventory.entity.Book;
import inventory.entity.User;
import object.db.map.Utils;

@SuppressWarnings("serial")
public class AdminPage extends JFrame implements ActionListener {
	private static AdminPage adminPage;
	private IController controller;
	
	private final String SEARCH = "Search..  ";
	
	private String tableUserName, tablePassword, tableEmail, tableBookName;
	private Integer tableBookYear;
	private TableRowSorter<TableModel> sorterUser, sorterBook;
	private HashSet<Integer> hashSet = new HashSet<Integer>(), hashSetBook = new HashSet<Integer>(); 
	
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPaneUser, scrollPaneBook;
	private JPanel panelBook, panelUser;
	private JLabel lblUserName, lblPassword, lblEmail, lblBookName,lblBookYear;
	private JTextField textFieldUserName, textFieldPassword, textFieldEmail, textFieldSearchUser, textFieldBookName, textFieldBookYear, textFieldSearchBook;
	private JButton btnSaveUser, btnUpdateUser, btnDeleteUser, btnLogOutUser, btnSaveBook, btnUpdateBook, btnDeleteBook, btnLogOutBook;
	private JTable tableUser, tableBook, tableUserCopy = new JTable(), tableBookCopy = new JTable();
	
	
	private AdminPage(IController controller) {
		
		this.controller = controller;
		setVisible(false);
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		setTitle("Admin Page");
		
		/************************* Tabbed Pane *****************************/
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(20, 11, 704, 410);
		getContentPane().add(tabbedPane);
		
		/************************* User Panel *****************************/
		panelUser = new JPanel();
		tabbedPane.addTab("Users", null, panelUser, null);
		panelUser.setLayout(null);
		
		tableUser = new JTable() {
			@Override
            public void editingStopped(ChangeEvent e) {				
				int row = this.getSelectedRow();
				int col = this.getSelectedColumn();
				
				this.setValueAt(cellEditor.getCellEditorValue(), row, col);
				this.cellEditor.cancelCellEditing();
				
				// Keep Track of the unique affected rows
				String origField = (String) tableUserCopy.getValueAt(row, col);
				String editedField = (String) tableUser.getValueAt(row, col);
				
				if(!origField.equals(editedField)){
					hashSet.add(row);
					// Update the TextFields
					if(col==0) {
						textFieldUserName.setText(editedField);
					} else if (col==1) {
						textFieldPassword.setText(editedField);
					} else {
						textFieldEmail.setText(editedField);
					}
				}
				// If changes are made onto the table
				if(hashSet.size()>0) {
					// Disable delete and create buttons
					btnDeleteUser.setEnabled(false);
					btnSaveUser.setEnabled(false);
					// Disable all Text fields too
					textFieldUserName.setEnabled(false);
					textFieldPassword.setEnabled(false);
					textFieldEmail.setEnabled(false);
				}
			}
		};
		tableUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Populate the selected table row to the textfields
				int row = tableUser.getSelectedRow();
				tableUserName = (String) tableUser.getValueAt(row, 0);
				tablePassword = (String) tableUser.getValueAt(row, 1);
				tableEmail = (String) tableUser.getValueAt(row, 2);
				
				textFieldUserName.setText(tableUserName);
				textFieldPassword.setText(tablePassword);
				textFieldEmail.setText(tableEmail);
			}
		});
		
		scrollPaneUser = new JScrollPane(tableUser);
		scrollPaneUser.setBounds(216, 43, 436, 266);
		panelUser.add(scrollPaneUser);
		

		textFieldSearchUser = new JTextField();
		textFieldSearchUser.setForeground(Color.LIGHT_GRAY);
		textFieldSearchUser.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldSearchUser.setText(SEARCH);
		textFieldSearchUser.addFocusListener(new FocusListener() {
			@Override
		    public void focusGained(FocusEvent e) {
		    	textFieldSearchUser.setText("");
		    	textFieldSearchUser.setForeground(Color.BLACK);
		    }
			
			@Override
		    public void focusLost(FocusEvent e) {}
		});
		textFieldSearchUser.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						TableFilter.regexAndFilter(sorterUser, textFieldSearchUser);
					}
		
					@Override
					public void removeUpdate(DocumentEvent e) {
						TableFilter.regexAndFilter(sorterUser, textFieldSearchUser);
					}
		
					@Override
					public void changedUpdate(DocumentEvent e) {
						TableFilter.regexAndFilter(sorterUser, textFieldSearchUser);
					}
				});
		textFieldSearchUser.setBounds(447, 8, 205, 23);
		panelUser.add(textFieldSearchUser);
		textFieldSearchUser.setColumns(10);
		
		lblUserName = new JLabel("Username");
		lblUserName.setBounds(10, 67, 76, 23);
		panelUser.add(lblUserName);
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 101, 76, 23);
		panelUser.add(lblPassword);
		
		lblEmail = new JLabel("Email");
		lblEmail.setBounds(10, 135, 76, 23);
		panelUser.add(lblEmail);
		
		textFieldUserName = new JTextField();
		textFieldUserName.setBounds(82, 68, 124, 20);
		panelUser.add(textFieldUserName);
		textFieldUserName.setColumns(10);
		
		textFieldPassword = new JTextField();
		textFieldPassword.setColumns(10);
		textFieldPassword.setBounds(82, 102, 124, 20);
		panelUser.add(textFieldPassword);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setColumns(10);
		textFieldEmail.setBounds(82, 136, 124, 20);
		panelUser.add(textFieldEmail);
		
		btnSaveUser = new JButton("Save");
		btnSaveUser.setBounds(51, 183, 115, 35);
		btnSaveUser.addActionListener(this);
		panelUser.add(btnSaveUser);
		
		btnUpdateUser = new JButton("Update");
		btnUpdateUser.setBounds(51, 229, 115, 35);
		btnUpdateUser.addActionListener(this);
		panelUser.add(btnUpdateUser);
		
		btnDeleteUser = new JButton("Delete");
		btnDeleteUser.setBounds(51, 275, 115, 35);
		btnDeleteUser.addActionListener(this);
		panelUser.add(btnDeleteUser);
		
		btnLogOutUser = new JButton("Log Out");
		btnLogOutUser.setBounds(563, 332, 89, 23);
		btnLogOutUser.addActionListener(this);
		panelUser.add(btnLogOutUser);
		/************************* End User Panel *****************************/
		
		/************************* Book Panel *****************************/
		panelBook = new JPanel();
		tabbedPane.addTab("Books", null, panelBook, null);
		panelBook.setLayout(null);
		
		tableBook = new JTable() {
			@Override
            public void editingStopped(ChangeEvent e) {				
				int row = this.getSelectedRow();
				int col = this.getSelectedColumn();
				
				this.setValueAt(cellEditor.getCellEditorValue(), row, col);
				this.cellEditor.cancelCellEditing();
				
				// Keep Track of the unique affected rows
				Object origField = tableBookCopy.getValueAt(row, col);
				Object editedField = tableBook.getValueAt(row, col);
				
				if(!origField.equals(editedField)){
					hashSetBook.add(row);
					// Update the TextFields
					if (col==0) {
						textFieldBookName.setText(editedField.toString());
					} else {
						textFieldBookYear.setText(editedField.toString());
					}
				}
				// If changes are made onto the table
				if(hashSetBook.size()>0) {
					// Disable delete and create buttons
					btnDeleteBook.setEnabled(false);
					btnSaveBook.setEnabled(false);
					// Disable all Text fields too
					textFieldBookName.setEnabled(false);
					textFieldBookYear.setEnabled(false);
				}
			}
		};
		tableBook.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tableBook.getSelectedRow();
				tableBookName = (String) tableBook.getValueAt(row, 0);
				tableBookYear = Integer.parseInt(tableBook.getValueAt(row, 1).toString());
				
				textFieldBookName.setText(tableBookName);
				textFieldBookYear.setText(tableBookYear.toString());
			}
		});
		
		scrollPaneBook = new JScrollPane(tableBook);
		scrollPaneBook.setBounds(216, 43, 436, 266);
		panelBook.add(scrollPaneBook);
		
		textFieldSearchBook = new JTextField();
		textFieldSearchBook.setForeground(Color.LIGHT_GRAY);
		textFieldSearchBook.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldSearchBook.setText(SEARCH);
		textFieldSearchBook.addFocusListener(new FocusListener() {
		    @Override
			public void focusGained(FocusEvent e) {
		    	textFieldSearchBook.setText("");
		    	textFieldSearchBook.setForeground(Color.BLACK);
		    }

		    @Override
			public void focusLost(FocusEvent e) {}
		});
		textFieldSearchBook.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						TableFilter.regexAndFilter(sorterBook, textFieldSearchBook);
					}
		
					@Override
					public void removeUpdate(DocumentEvent e) {
						TableFilter.regexAndFilter(sorterBook, textFieldSearchBook);
					}
		
					@Override
					public void changedUpdate(DocumentEvent e) {
						TableFilter.regexAndFilter(sorterBook, textFieldSearchBook);
					}
				});
		textFieldSearchBook.setBounds(440, 11, 205, 23);
		panelBook.add(textFieldSearchBook);
		textFieldSearchBook.setColumns(10);
		
		lblBookName = new JLabel("Bookname");
		lblBookName.setBounds(10, 67, 76, 23);
		panelBook.add(lblBookName);
		
		lblBookYear = new JLabel("Book Year");
		lblBookYear.setBounds(10, 102, 76, 23);
		panelBook.add(lblBookYear);
		
		textFieldBookName = new JTextField();
		textFieldBookName.setBounds(82, 68, 124, 20);
		panelBook.add(textFieldBookName);
		textFieldBookName.setColumns(10);
		
		textFieldBookYear = new JTextField();
		textFieldBookYear.setColumns(10);
		textFieldBookYear.setBounds(82, 102, 124, 20);
		panelBook.add(textFieldBookYear);
		
		btnSaveBook = new JButton("Save");
		btnSaveBook.setBounds(51, 183, 115, 35);
		btnSaveBook.addActionListener(this);
		panelBook.add(btnSaveBook);
		
		btnUpdateBook = new JButton("Update");
		btnUpdateBook.setBounds(51, 229, 115, 35);
		btnUpdateBook.addActionListener(this);
		panelBook.add(btnUpdateBook);
		
		btnDeleteBook = new JButton("Delete");
		btnDeleteBook.setBounds(51, 275, 115, 35);
		btnDeleteBook.addActionListener(this);
		panelBook.add(btnDeleteBook);
		
		btnLogOutBook = new JButton("Log Out");
		btnLogOutBook.setBounds(563, 332, 89, 23);
		btnLogOutBook.addActionListener(this);
		panelBook.add(btnLogOutBook);
		/************************* End Book Panel *****************************/
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		/************************* User Panel *****************************/
		if (e.getSource()== btnLogOutUser||e.getSource()== btnLogOutBook) {
			this.setVisible(false);
			HomePage.getSingleton(controller).setVisible(true);
			LogIn.getSingleton(controller).resetInput();
			return;
		}
		
		String userName = textFieldUserName.getText();
		String password = textFieldPassword.getText();
		String email = textFieldEmail.getText();
		
		if(e.getSource()==btnDeleteUser){
			if(userName.isEmpty()){
				JOptionPane.showMessageDialog(null, "Fill Up the UserName field");
				return;
			}
			controller.deleteUser(userName);
		}
		
		if(e.getSource()==btnSaveUser){
			if(userName.isEmpty()||password.isEmpty()||email.isEmpty()){
				JOptionPane.showMessageDialog(null, "Fill Up the text fields");
				return;
			}
			controller.addUser(userName, password,email);
		}
		
		if(e.getSource()==btnUpdateUser) {
			// if making updates through the table itself
			if(!btnSaveUser.isEnabled()||!btnDeleteUser.isEnabled()){
				// for each row, make update but don't refresh the table yet
				for(Integer rowIdx: hashSet) {
					controller.updateUser((String) tableUser.getValueAt(rowIdx, 0),
										  (String) tableUser.getValueAt(rowIdx, 1),
										  (String) tableUser.getValueAt(rowIdx, 2),
										  false);
				}
				//Repopulate User table
				controller.getAllUsers();
				
				// Reset tracker
				hashSet = new HashSet<Integer>();
				
				// Enable again
				btnDeleteUser.setEnabled(true);
				btnSaveUser.setEnabled(true);
				textFieldUserName.setEnabled(true);
				textFieldPassword.setEnabled(true);
				textFieldEmail.setEnabled(true);
			} else {
				if(userName.isEmpty()||password.isEmpty()||email.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Fill Up the text fields");
					return;
				}
				controller.updateUser(userName, password,email);
			}
		}
		
		/************************* End User Panel *****************************/
		/************************* For Book Panel *****************************/
		String bookName = textFieldBookName.getText();
		Integer bookYear=null;
		if(!textFieldBookYear.getText().isEmpty()) {
			bookYear = Integer.parseInt(textFieldBookYear.getText());
		}
		
		if(e.getSource()==btnDeleteBook) {
			if(bookName.isEmpty() || bookYear == null) {
				JOptionPane.showMessageDialog(null, "Fill Up the BookName field");
				return;
			}
			controller.deleteBook(bookName);
		}
		
		if(e.getSource()==btnSaveBook) {
			if(bookName.isEmpty()|| bookYear == null){
				JOptionPane.showMessageDialog(null, "Fill Up the text fields");
				return;
			}
			controller.addBook(bookName, bookYear);
		}
		
		if(e.getSource()==btnUpdateBook) {
			// if making updates through the table itself
			if(!btnSaveBook.isEnabled()||!btnDeleteBook.isEnabled()){
				// for each row, make update but dont refresth the table yet
				for(Integer rowIdx: hashSetBook) {
					controller.updateBook((String) tableBook.getValueAt(rowIdx, 0),
										  Integer.parseInt(tableBook.getValueAt(rowIdx, 1).toString()),
										  false);
				}
				//Repopulate table
				controller.getAllBooks();
				
				// Reset tracker
				hashSetBook = new HashSet<Integer>();
				
				// Enable again
				btnDeleteBook.setEnabled(true);
				btnSaveBook.setEnabled(true);
				textFieldBookName.setEnabled(true);
				textFieldBookYear.setEnabled(true);
			} else {
				if(bookName.isEmpty()|| bookYear == null){
					JOptionPane.showMessageDialog(null, "Fill Up the text fields");
					return;
				}
				controller.updateBook(tableBookName, bookYear);
			}
		}
		/************************* End Book Panel *****************************/
	}
	
	/**
	 * Populate a list of Users onto a table
	 * @param results a list of Users
	 */
	public void setTableUserResults(List<User> results) {
		// Set a model with a sorter
		tableUser.setModel(Utils.listToTableModel(results));
		sorterUser = new TableRowSorter<TableModel>(tableUser.getModel());
		tableUser.setRowSorter(sorterUser);
		reformatTable();
		
		tableUserCopy.setModel(Utils.listToTableModel(results));
		tableUserCopy.setRowSorter(sorterUser);
	}
	
	/**
	 * Populate a list of Books onto a table
	 * @param results a list of Books
	 */
	public void setTableBookResults(List<Book> results) {
		// Set a model with a sorter
		tableBook.setModel(Utils.listToTableModel(results));
		sorterBook = new TableRowSorter<TableModel>(tableBook.getModel());
		tableBook.setRowSorter(sorterBook);
		reformatTable();
		
		tableBookCopy.setModel(Utils.listToTableModel(results));
		tableBookCopy.setRowSorter(sorterBook);
	}
	
	/*
	 * Reformat the book and user tables
	 */
	private void reformatTable() {
		if(tableUser.getColumnModel().getColumnCount()>0) {
			tableUser.getColumnModel().getColumn(0).setHeaderValue("Username");
			tableUser.getColumnModel().getColumn(1).setHeaderValue("Password");
			tableUser.getColumnModel().getColumn(2).setHeaderValue("Email");
			tableUser.getTableHeader().setOpaque(false);
			tableUser.getTableHeader().setBackground(Color.decode("#a5cdff"));
			tableUser.getTableHeader().repaint();
		}
		
		if(tableBook.getColumnModel().getColumnCount()>0) {
			tableBook.getColumnModel().getColumn(0).setHeaderValue("Book Name");
			tableBook.getColumnModel().getColumn(1).setHeaderValue("Published Year");
			tableBook.getTableHeader().setOpaque(false);
			tableBook.getTableHeader().setBackground(Color.decode("#a5cdff"));
			tableBook.getTableHeader().repaint();	
		}
	}
	
	/**
	 * Get a Singleton of the AdminPage class
	 * @param controller an IController object
	 * @return a Singleton of the AdminPage class
	 */
	public static AdminPage getSingleton(IController controller) {
		if(adminPage == null) {
			adminPage = new AdminPage(controller);
		}
		return adminPage;
	}
}
