package updater.dynamoDBUpdater.service;

/**
 * Database transaction management interface.
 * 
 * @author theja.kotuwella
 *
 */
public interface IDatabaseTransactionManagerSevice {
	boolean writeBookDetailsToDatabase(String isbn, String author, String s3path);
}
