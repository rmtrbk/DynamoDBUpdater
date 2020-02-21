package updater.dynamodbupdater.service;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;

/**
 * Metadata extraction interface.
 * 
 * @author theja.kotuwella
 *
 */
public interface IMetadataExtractorService {
	Map<String, String> extractMetadata(List<S3EventNotificationRecord> records);
}
