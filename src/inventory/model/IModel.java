package inventory.model;

import java.util.List;


/**
 * An {@code interface} for the inventory system model.
 * The model performs the CRUD operations onto items (such as {@link Book} and {@link User}) and carts.
 * <br>The cart is assumed to be non-persistent and set to be empty for each session. Therefore,
 * the model has specialized CRD operations for carts. 
 * <br>A cart could contains different type of items.
 * 
 * @author thean
 *
 */
public interface IModel{
	
	/**
	 * Create/Insert an item into a data storage
	 * @param item an item to be added into a data storage
	 * @return a {@link Status} object recording the status of the transaction.
	 */
	<T> Status createItem(T item);
	
	/**
	 * Update an item stored in a data storage.
	 * <br> The two items must have the same id, if applicable.
	 * @param oldItem an item to be updated by {@code newItem}
	 * @param newItem an item to update {@code oldItem}
	 * @return a {@link Status} object recording the status of the transaction.
	 */
	<T> Status updateItem(T oldItem, T newItem);
	
	/**
	 * Delete an item from a data storage
	 * @param item an item to be removed from a data storage
	 * @return a {@link Status} object recording the status of the transaction.
	 */
	<T> Status deleteItem(T item);
	
	/**
	 * Retrieve items stored in a data storage matching condition(s).
	 * <br>Condition(s) is/are specified using {@code itemList} and {@code include}.
	 * 
	 * @param itemList a list of items. Their non-null, populated fields are used as filter criteria. 
	 * 				   Criteria specified by each item is combined.
	 * @param include whether to include (or exclude) results with criteria specified by {@code itemList}
	 * @return a {@link Status} object recording the status of the transaction. 
	 *         Its {@code statusValue} stores a list of items matching condition(s)
	 */
	<T> Status retrieveItem(List<T> itemList, boolean include);
	
	/**
	 * Create/Insert an item into a cart.
	 * @param item an item to be added into a cart
	 * @return a {@link Status} object recording the status of the transaction.
	 */
	<T> Status createCart(T item);
	
	/**
	 * Delete an item from a cart
	 * @param item an item to be removed from a cart
	 * @return a {@link Status} object recording the status of the transaction.
	 */
	<T> Status deleteCart(T item);
	
	/**
	 * Retrieve items stored in a cart.
	 * @return a {@link Status} object recording the status of the transaction. 
	 *         Its {@code statusValue} stores a list of items stored in a cart.
	 */
	Status retrieveCart();
	
}
