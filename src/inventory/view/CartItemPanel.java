package inventory.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is a JPanel that is used to represent an 
 * item in the cart. It has an item added to the cart,
 * an image icon, an item description, and a button.
 * @author thean
 *
 */
@SuppressWarnings("serial")
public class CartItemPanel extends JPanel{
	
	// The button should be configured to remove
	// the item from the cart when it's clicked.
	private JButton btnRemove;
	private Object item;
	
	/**
	 * Construct a CartItem Panel
	 * @param item the item added to the cart
	 * @param itemIcon an Icon representing the item.
	 * @param itemDesc a description of the item.
	 */
	public CartItemPanel(Object itemObject, ImageIcon itemIcon, String itemDesc) {
		this.item = itemObject;
		setName("CartItemPanel");
		setLayout(null);
		setMaximumSize(new Dimension(700, 200));
		setMinimumSize(new Dimension(700, 200));
		setPreferredSize(new Dimension(700, 200));
		
		btnRemove = new JButton();
		btnRemove.setIcon(iconResize(new ImageIcon("./img/remove.png"), 30, 40));
		btnRemove.setBounds(611, 79, 30, 40);
		btnRemove.setBorderPainted(false);
		add(btnRemove);
		
		JLabel lblIcon = new JLabel("");
		if (itemIcon !=null) {
			lblIcon.setIcon(iconResize(itemIcon, 100, 100));
		}
		lblIcon.setBounds(40, 50, 100, 100);
		add(lblIcon);
		
		JLabel lblDesc = new JLabel(itemDesc);
		lblDesc.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 15));
		lblDesc.setBounds(154, 50, 384, 100);
		add(lblDesc);
	}
	
	/**
	 * Get the remove button
	 * @return the remove button
	 */
	public JButton getButton() {
		return btnRemove;
	}
	
	/**
	 * Get the Item
	 * @return the item.
	 */
	public Object getItemObject() {
		return item;
	}

	/**
	 * Resize the input icon to the specified width and height
	 * @param icon an image icon
	 * @param width the width to which to scale the icon
	 * @param height the height to which to scale the icon
	 * @return the resized image icon
	 */
	// Revised from //https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	private ImageIcon iconResize(ImageIcon icon, int width, int height) {
		return new ImageIcon(
				icon
				.getImage()
				.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH)
			);
	}
}
