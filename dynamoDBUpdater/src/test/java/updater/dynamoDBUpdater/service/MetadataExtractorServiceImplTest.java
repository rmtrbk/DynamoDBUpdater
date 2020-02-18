package updater.dynamoDBUpdater.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification.S3BucketEntity;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.event.S3EventNotification.S3ObjectEntity;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import junit.framework.TestCase;
import updater.dynamoDBUpdater.util.ClientBuilderManager;

/**
 * Test class for {@link:MetadataExtractorServiceImpl}
 * 
 * @author theja.kotuwella
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientBuilderManager.class)
public class MetadataExtractorServiceImplTest extends TestCase {
	MetadataExtractorServiceImpl extractor = null;
	
	private static final String METADATA_ISBN 		= "isbn";
	private static final String METADATA_AUTHOR 	= "author";
	private static final String METADATA_S3_PATH 	= "s3path";
	
	private static final String S3_BUCKET_NAME 		= "s3-crud-bucket";
	private static final String BOOK_TITLE 			= "MyBook.pdf";
	private static final String ISBN 				= "978-1-56619-909-4";
	private static final String AUTHOR 				= "Theja Kotuwella";
	private static final String S3_PATH				= "https://" + S3_BUCKET_NAME 
														+ ".s3.amazonaws.com/" 
														+ BOOK_TITLE;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		LambdaLogger logger = Mockito.mock(LambdaLogger.class);
		Mockito.doNothing().when(logger).log(Mockito.anyString());
		
		extractor = new MetadataExtractorServiceImpl(logger);
	}
	
	@Test
	public void testExtractMetadata() {
		S3BucketEntity s3bucketEntity = Mockito.mock(S3BucketEntity.class);
		Mockito.when(s3bucketEntity.getName()).thenReturn(S3_BUCKET_NAME);
		
		S3ObjectEntity s3objectEntity = Mockito.mock(S3ObjectEntity.class);
		Mockito.when(s3objectEntity.getKey()).thenReturn(BOOK_TITLE);
		
		S3Entity s3entity = Mockito.mock(S3Entity.class);
		Mockito.when(s3entity.getBucket()).thenReturn(s3bucketEntity);
		Mockito.when(s3entity.getObject()).thenReturn(s3objectEntity);
		
		ObjectMetadata objectMetadata = Mockito.mock(ObjectMetadata.class);
		Mockito.when(objectMetadata.getUserMetaDataOf(METADATA_ISBN)).thenReturn(ISBN);
		Mockito.when(objectMetadata.getUserMetaDataOf(METADATA_AUTHOR)).thenReturn(AUTHOR);
		Mockito.when(objectMetadata.getUserMetaDataOf(METADATA_S3_PATH)).thenReturn(S3_PATH);
				
		S3Object s3object = Mockito.mock(S3Object.class);
		Mockito.when(s3object.getObjectMetadata()).thenReturn(objectMetadata);
		
		AmazonS3 s3client = Mockito.mock(AmazonS3.class);
		Mockito.when(s3client.getObject(new GetObjectRequest(S3_BUCKET_NAME, BOOK_TITLE)))
															.thenReturn(s3object);
		
		PowerMockito.mockStatic(ClientBuilderManager.class);
		PowerMockito.when(ClientBuilderManager.s3Client()).thenReturn(s3client);
		
		S3EventNotificationRecord record = Mockito.mock(S3EventNotificationRecord.class);
		Mockito.when(record.getS3()).thenReturn(s3entity);
		
		List<S3EventNotificationRecord> records = new ArrayList<>();
		records.add(record);
		
		// Expectation
		Map<String, String> expected = new HashMap<>();
        expected.put(METADATA_ISBN, ISBN);
        expected.put(METADATA_AUTHOR, AUTHOR);
        expected.put(METADATA_S3_PATH, S3_PATH);
        
        // Result
		Map<String, String> results = extractor.extractMetadata(records);
		
		// Assertions
		assertEquals("Comparing result with expected failed", expected, results);
	}
}
