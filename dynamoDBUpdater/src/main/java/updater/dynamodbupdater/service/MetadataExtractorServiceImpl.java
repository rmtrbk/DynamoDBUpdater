package updater.dynamodbupdater.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import updater.dynamodbupdater.util.ClientBuilderManager;

/**
 * Implementation of {link:IMetadataExtractorService}
 * Deals with all metadata related work sent in any event payload.
 * 
 * @author theja.kotuwella
 *
 */
public class MetadataExtractorServiceImpl implements IMetadataExtractorService {
	LambdaLogger logger = null;
	
	private static final String METADATA_ISBN 		= "isbn";
	private static final String METADATA_AUTHOR 	= "author";
	private static final String METADATA_S3_PATH 	= "s3path";
	
	public MetadataExtractorServiceImpl(LambdaLogger logger) {
		this.logger = logger;
	}
	
	/**
	 * Extracts metadata from a given list and arranges them in key/value pairs
	 *
	 * @param records List of of S3EventNotificationRecord
	 * @return All extracted metadata in key/value pairs
	 */
	@Override
	public Map<String, String> extractMetadata(List<S3EventNotificationRecord> records) {
		Map<String, String> metadata = new HashMap<>();
		
		// We only need the most recently added document to the S3 (i.e. first record)
		S3EventNotificationRecord record = records.get(0);

		String s3Bucket = record.getS3().getBucket().getName();
		String s3Key 	= record.getS3().getObject().getKey();
		
		logger.log("MetadataExtractorServiceImpl: S3 Bucket" + s3Bucket);
		logger.log("MetadataExtractorServiceImpl: S3 Key" + s3Key);

		S3Object document = ClientBuilderManager.s3Client()
							.getObject(new GetObjectRequest(s3Bucket, s3Key));

		ObjectMetadata metadataObj = document.getObjectMetadata();

		String isbn 	= metadataObj.getUserMetaDataOf(METADATA_ISBN);
		String author 	= metadataObj.getUserMetaDataOf(METADATA_AUTHOR);
		String s3path 	= "https://" + s3Bucket + ".s3.amazonaws.com/" + s3Key;
		
		logger.log("MetadataExtractorServiceImpl: ISBN: " + isbn);
		logger.log("MetadataExtractorServiceImpl: Author: " + author);
		logger.log("MetadataExtractorServiceImpl: S3 Path: " + s3Key);
		
		metadata.put(METADATA_ISBN, isbn);
		metadata.put(METADATA_AUTHOR, author);
		metadata.put(METADATA_S3_PATH, s3path);
		
		return metadata;
	}
}
