package updater.dynamodbupdater.event;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;

import updater.dynamodbupdater.service.DatabaseTransactionManagerSeviceImpl;
import updater.dynamodbupdater.service.IDatabaseTransactionManagerSevice;
import updater.dynamodbupdater.service.IMetadataExtractorService;
import updater.dynamodbupdater.service.MetadataExtractorServiceImpl;
import updater.dynamodbupdater.util.PropertyManager;

/**
 * Event driven class for S3 notification (trigger message). 
 * Any S3 message triggers a notification here and takes the next action from there.
 *
 */
public class S3RequestEventHandler implements RequestHandler<S3Event, Boolean> {
	@Override
	public Boolean handleRequest(S3Event event, Context context) {
		PropertyManager.loadProperties();
		
		return updateBookInventoryInDynamoDB(event.getRecords(), context.getLogger());
	}
	
	private boolean updateBookInventoryInDynamoDB(List<S3EventNotificationRecord> records, LambdaLogger logger) {
		IMetadataExtractorService extractorService = new MetadataExtractorServiceImpl(logger);
		Map<String, String> metadata = extractorService.extractMetadata(records);

		IDatabaseTransactionManagerSevice databaseService = new DatabaseTransactionManagerSeviceImpl(logger);
		boolean isSuccessfull = databaseService.writeBookDetailsToDatabase(metadata.get("isbn"), 
				metadata.get("author"), 
				metadata.get("s3path"));

		return isSuccessfull;
	}
}
