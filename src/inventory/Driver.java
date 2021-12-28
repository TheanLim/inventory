package inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import inventory.controller.Controller;
import inventory.model.IModel;
import inventory.model.Model;
import inventory.view.IView;
import inventory.view.JFrameView;

/**
 * A Driver for the inventory system.
 * @author thean
 *
 */
public class Driver {
	public static void main(String[] args){
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:file:./mockDatabase/bookstore", "sa", ""); 
			IModel model = new Model(conn);
			IView view = new JFrameView();
			new Controller(model, view);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
