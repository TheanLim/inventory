package inventory.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import inventory.controller.IController;
import inventory.entity.Book;
import object.db.map.Utils;

/**
 * A User Page. This is the page seen by users.
 * @author thean
 *
 */
@SuppressWarnings("serial")
public class UserPage extends JFrame implements ActionListener {
	private static UserPage userPage;
	private IController controller;
	
	private final String SEARCH = "Search..  ";
	private final ImageIcon BOOKICON = new ImageIcon("./img/book.png");
	
	private String tableBookName;
	private Integer tableBookYear;
	private TableRowSorter<TableModel> sorterBook;
	
	private JDesktopPane desktopPane;
	private JPanel panelMain, panelCartContainer, panelCartCheckOut, panelCartItem;
	private JScrollPane scrollPaneTable, scrollPaneBookName, scrollPaneCartItem;
	private JInternalFrame internalFrameCart;
	private JLabel lblBookName, lblBookYear;
	private JTable tableBook;
	private JTextArea textAreaBookName;
	private JTextField textFieldSearchBook, textFieldBookYear;
	private JButton btnAdd, btnLogOut, btnCheckOut;

	private UserPage(IController controller) {
		this.controller = controller;
		setVisible(false);
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		setTitle("User Page");
		
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(UIManager.getColor("InternalFrame.activeTitleBackground"));
		desktopPane.setBounds(6, 6, 788, 460);
		desktopPane.setLayout(null);
		getContentPane().add(desktopPane);
		
		panelMain = new JPanel();
		panelMain.setBounds(6, 6, 776, 448);
		panelMain.setLayout(null);
		
		tableBook = new JTable();
		tableBook.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Show the Book that are clicked on the table
				int row = tableBook.getSelectedRow();
				tableBookName = (String) tableBook.getValueAt(row, 0);
				tableBookYear = Integer.parseInt(tableBook.getValueAt(row, 1).toString());
				
				textAreaBookName.setText(tableBookName);
				textFieldBookYear.setText(tableBookYear.toString());
			}
		});
		
		scrollPaneTable = new JScrollPane(tableBook);
		scrollPaneTable.setBounds(316, 44, 454, 350);
		panelMain.add(scrollPaneTable);
		
		textFieldSearchBook = new JTextField();
		textFieldSearchBook.setBounds(594, 6, 176, 26);
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
		textFieldSearchBook.setColumns(10);
		panelMain.add(textFieldSearchBook);
		
		lblBookName = new JLabel("Bookname");
		lblBookName.setBounds(9, 79, 76, 23);
		panelMain.add(lblBookName);
		
		lblBookYear = new JLabel("Book Year");
		lblBookYear.setBounds(9, 128, 76, 23);
		panelMain.add(lblBookYear);
		
		scrollPaneBookName = new JScrollPane();
		scrollPaneBookName.setBounds(113, 71, 161, 35);
		panelMain.add(scrollPaneBookName);
		
		textAreaBookName = new JTextArea();
		textAreaBookName.setRows(2);
		textAreaBookName.setWrapStyleWord(true);
		textAreaBookName.setLineWrap(true);
		textAreaBookName.setEditable(false);
		scrollPaneBookName.setViewportView(textAreaBookName);
		
		textFieldBookYear = new JTextField();
		textFieldBookYear.setEditable(false);
		textFieldBookYear.setBounds(113, 129, 135, 20);
		textFieldBookYear.setColumns(10);
		panelMain.add(textFieldBookYear);
		
		btnAdd = new JButton("Add To Cart");
		btnAdd.setBounds(47, 188, 115, 35);
		btnAdd.addActionListener(this);
		panelMain.add(btnAdd);
		
		btnLogOut = new JButton("Log Out");
		btnLogOut.setBounds(681, 419, 89, 23);
		btnLogOut.addActionListener(this);
		btnLogOut.requestFocus();
		panelMain.add(btnLogOut);
		
		/************************* Cart *****************************/
		panelCartCheckOut = new JPanel();
		panelCartCheckOut.setBorder(null);
		panelCartCheckOut.setMaximumSize(new Dimension(788, 100));
		panelCartCheckOut.setPreferredSize(new Dimension(788, 100));
		panelCartCheckOut.setMinimumSize(new Dimension(788, 100));
		
		panelCartContainer = new JPanel();
		panelCartContainer.setLayout(new BoxLayout(panelCartContainer, BoxLayout.PAGE_AXIS));
		panelCartContainer.add(panelCartCheckOut);
		panelCartCheckOut.setLayout(null);
		
		btnCheckOut = new JButton("Check Out");
		btnCheckOut.setBounds(600, 35, 127, 35);
		panelCartCheckOut.add(btnCheckOut);
		btnCheckOut.addActionListener(this);
		
		internalFrameCart = new JInternalFrame("Cart");
		internalFrameCart.setBounds(-5, 400, 246, 53);
		internalFrameCart.setMaximizable(true);
		internalFrameCart.setResizable(true);
		internalFrameCart.setVisible(true);
		internalFrameCart.getContentPane().setLayout(new BoxLayout(internalFrameCart.getContentPane(), BoxLayout.PAGE_AXIS));
		internalFrameCart.getContentPane().add(panelCartContainer);
		
		panelCartItem = new JPanel();
		panelCartItem.setBorder(null);
		panelCartItem.setLayout(new BoxLayout(panelCartItem, BoxLayout.PAGE_AXIS));
		scrollPaneCartItem = new JScrollPane(panelCartItem);
		panelCartContainer.add(scrollPaneCartItem);
		/************************* End Cart *****************************/
		
		desktopPane.add(internalFrameCart);
		desktopPane.add(panelMain);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==btnAdd) {
			String bookName = textAreaBookName.getText();
			Integer bookYear=null;
			
			if(!textFieldBookYear.getText().isEmpty()) {
				bookYear = Integer.parseInt(textFieldBookYear.getText());
			}
			
			if(bookName.isEmpty()||bookYear==null) 
			{
				JOptionPane.showMessageDialog(null, "Double click on the book of interest first.");
				return;
			}
			controller.addBookCart(bookName, bookYear);
			// Clear the textField
			textAreaBookName.setText("");
			textFieldBookYear.setText("");
			//Clear the textFieldSearchBook
			textFieldSearchBook.setText("");
		}
		
		if(e.getSource() ==btnLogOut) {
			setVisible(false);
			HomePage.getSingleton(controller).setVisible(true);
			LogIn.getSingleton(controller).resetInput();
			// Move the cart floating window back to where it was
			internalFrameCart.setBounds(-5, 400, 246, 53);
			controller.emptyCart();
		}
		
		if(e.getSource()==btnCheckOut) {
			controller.checkoutCart();
			try {
				// Hide Cart
				internalFrameCart.setMaximum(false);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}
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
	}
	
	/**
	 * Add an item to the cart
	 * @param item an item to be added to the cart
	 */
	public <T> void addItemtoCart(T item) {
		ImageIcon icon = null;
		if(item instanceof Book) {
			icon = BOOKICON;
		}
		CartItemPanel cartItemPanel = new CartItemPanel(item, icon, item.toString());
		addCartItemPanelBtnListener(cartItemPanel);
		panelCartItem.add(cartItemPanel);
	}
	
	/**
	 * Remove an item from the cart.
	 * @param item an item to be removed from the cart.
	 */
	public void removeItemFromCart(Object item) {
		Component [] allComp = panelCartItem.getComponents();
		Optional<CartItemPanel> comp = Arrays.stream(allComp)
							   				.map(c-> (CartItemPanel)c)
							   				.filter(c -> c.getItemObject()==item)
							   				.findFirst();
		if(comp.isPresent()) {
			panelCartItem.remove(comp.get());
			panelCartItem.repaint();
			panelCartItem.validate();
		}
		// if no components left, hide the cart
		if (panelCartItem.getComponentCount()==0) {
			try {
				internalFrameCart.setMaximum(false);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Empty the Cart.
	 */
	public void emptyCart() {
		panelCartItem.removeAll();
		panelCartItem.repaint();
		panelCartItem.validate();
	}
	
	/*
	 * Add ActionListener to the button on the CartItemPanel Object
	 */
	private void addCartItemPanelBtnListener(CartItemPanel item) {
		item.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//remove from cart when the button X is clicked
				controller.removeCart(item.getItemObject());
			}
		});
	}
	
	/*
	 * Reformat the book table
	 */
	private void reformatTable() {
		if(tableBook.getColumnModel().getColumnCount()>0) {
			tableBook.getColumnModel().getColumn(0).setHeaderValue("Book Name");
			tableBook.getColumnModel().getColumn(1).setHeaderValue("Published Year");
			tableBook.getTableHeader().setOpaque(false);
			tableBook.getTableHeader().setBackground(Color.decode("#a5cdff"));
			tableBook.getTableHeader().repaint();	
		}
	}
	
	/**
	 * Get a Singleton of the UserPage class
	 * @param controller an IController object
	 * @return a Singleton of the UserPage class
	 */
	public static UserPage getSingleton(IController controller) {
		if(userPage == null) {
			userPage = new UserPage(controller);
		}
		return userPage;
	}
}
