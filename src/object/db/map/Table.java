package object.db.map;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that specifies a class to be stored in a database.
 * @author thean
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	/**
	 * The table name in the database. 
	 * Note to not use SQL keyword in table name.
	 * The default is the class name and could clash with SQL keyword.
	 * A class that has this annotation should also have a no arg constructor,
	 * which will be invoked to construct a class object populated with data 
	 * retrieved from database.
	 */
    public String tableName() default "";
}