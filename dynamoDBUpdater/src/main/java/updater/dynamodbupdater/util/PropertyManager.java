package updater.dynamodbupdater.util;

/**
 * Reads the properties from the environment.
 * 
 * @author theja.kotuwella
 *
 */
public class PropertyManager {
	private static final String DATABASE_TABLE_NAME_PROP 	= "dynamoDB_table_bookInventory";
	private static final String DATABASE_TABLE_KEY_PROP 	= "dynamoDB_table_bookInventory_primaryKey";
	
	public static String DATABASE_TABLE_NAME_VALUE	= null;
	public static String DATABASE_TABLE_KEY_VALUE	= null;
	
	public static void loadProperties() {
		DATABASE_TABLE_NAME_VALUE 	= System.getenv(DATABASE_TABLE_NAME_PROP);
		DATABASE_TABLE_KEY_VALUE 	= System.getenv(DATABASE_TABLE_KEY_PROP);
	}
}
