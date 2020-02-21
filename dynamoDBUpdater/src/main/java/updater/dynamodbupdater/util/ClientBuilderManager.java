package updater.dynamodbupdater.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Builds the AWS clients.
 * 
 * @author theja.kotuwella
 *
 */
public class ClientBuilderManager {
	private static AmazonDynamoDB dynamoDBclient = null;
	private static AmazonS3 s3Client 	= null;
	private static int SOCKET_TIMEOUT 	= 60000;
	
	public static AmazonDynamoDB dynamoDBClient() {
		if(dynamoDBclient == null) {
			dynamoDBclient = AmazonDynamoDBClientBuilder.standard()
								.withRegion(Regions.AP_SOUTHEAST_2)
								.build();
		}
		return dynamoDBclient;
	}
	
	public static AmazonS3 s3Client() {
		if(s3Client == null) {
			try {
				ClientConfiguration clientConfig = new ClientConfiguration();
				clientConfig.setSocketTimeout(SOCKET_TIMEOUT);
				
				s3Client = AmazonS3ClientBuilder.standard()
								.withRegion(Regions.AP_SOUTHEAST_2)
								.build();

			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return s3Client;
	}
}
