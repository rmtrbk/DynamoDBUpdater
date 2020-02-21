package updater.dynamodbupdater.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import updater.dynamodbupdater.util.ClientBuilderManager;
import updater.dynamodbupdater.util.PropertyManager;

/**
 * Implementation of {@link:IDatabaseTransactionManagerSevice}.
 * Manages all DynamoDB database transactions.
 * 
 * @author theja.kotuwella
 *
 */
public class DatabaseTransactionManagerSeviceImpl implements IDatabaseTransactionManagerSevice {
	private  LambdaLogger logger = null;
	
	public DatabaseTransactionManagerSeviceImpl(LambdaLogger logger) {
		this.logger = logger;
	}
	
	/**
	 * Writes given values to the database table
	 *
	 * @param isbn ISBN of the book
	 * @param author Author of the book
	 * @param s3path S3 path of the book object 
	 * @return Writing record to database was successful or not
	 */
	@Override
	public boolean writeBookDetailsToDatabase(String isbn, String author, String s3path) {
		boolean isSuccessful = false;
		
		DynamoDB dynamoDB = new DynamoDB(ClientBuilderManager.dynamoDBClient());
		
		if(isConfigurationDetailsAvailable()) {
			Table table = dynamoDB.getTable(PropertyManager.DATABASE_TABLE_NAME_VALUE);
			
			Item item = new Item().withPrimaryKey(PropertyManager.DATABASE_TABLE_KEY_VALUE, isbn);
			
			item.withString("author", author);
			item.withString("s3path", s3path);

			table.putItem(item);
			
			logger.log("DatabaseTransactionManagerSeviceImpl: ISBN: " + isbn);
			logger.log("DatabaseTransactionManagerSeviceImpl: Author: " + author);
			logger.log("DatabaseTransactionManagerSeviceImpl: S3 Path: " + s3path);
			
			isSuccessful = true;
		}
		return isSuccessful;
	}
	
	private boolean isConfigurationDetailsAvailable() {
		logger.log("DatabaseTransactionManagerSeviceImpl: Table: " 
									+ PropertyManager.DATABASE_TABLE_NAME_VALUE);
		logger.log("DatabaseTransactionManagerSeviceImpl: Primary Key: " 
									+ PropertyManager.DATABASE_TABLE_KEY_VALUE);

		return PropertyManager.DATABASE_TABLE_NAME_VALUE != null && 
				!PropertyManager.DATABASE_TABLE_NAME_VALUE.isEmpty() &&
				PropertyManager.DATABASE_TABLE_KEY_VALUE != null &&
				!PropertyManager.DATABASE_TABLE_KEY_VALUE.isEmpty();
	}
}
