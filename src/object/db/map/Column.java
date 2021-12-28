package object.db.map;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that specifies a class field and maps it into a database column.
 * <br> An update onto this annotation requires updates on {@link DbConfig} and {@link DataField};
 * @author thean
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	/**
	 * The column name in the database. 
	 * <br>Do not use SQL keyword in table name.
	 * The default uses the class field's name and could
	 * clash with SQL keyword.
	 */
    String columnName() default "";
    
    /**
     * Whether a class field is an id or not.
     * The default is false.
     */
    boolean id() default false;
}