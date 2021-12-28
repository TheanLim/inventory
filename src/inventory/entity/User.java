package inventory.entity;

import object.db.map.Column;
import object.db.map.Table;

/**
 * This class represents a User.
 * A User has a user name, a password and an email.
 * @author thean
 *
 */
@Table(tableName="users")
public class User {
	
    @Column(columnName="user_name", id = true)
    private String userName;
    @Column
    private String password;
    @Column
    private String email;
    
    /*
	 * A No-arg constructor is needed to populate
	 * database results to object instances.
	 */
    public User() {}
    
    /**
     * Constructs a User object
     * @param userName the user name
     * @param password the user password
     * @param email the user email
     */
    public User(String userName, String password, String email) {
    	this.userName = userName;
    	this.password = password;
    	this.email = email;
    }
    
    /**
     * Get this User username
     * @return this User username
     */
    public String getUserName() {
    	return userName;
    }
    
    @Override
    public String toString() {
    	return "UserName: "+userName+
    			", Password: "+password+
    			", Email: "+email;
    }
}