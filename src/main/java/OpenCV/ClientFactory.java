package OpenCV;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class ClientFactory {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials("access_key_id", "secret_key_id");

    public static AmazonRekognition createRekognitionClient() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(30000);
        clientConfig.setRequestTimeout(60000);
        clientConfig.setProtocol(Protocol.HTTPS);

        AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

        return AmazonRekognitionClientBuilder
                .standard()
                .withClientConfiguration(clientConfig)
                .withCredentials(credentialsProvider)
                .withRegion("eu-west-1")
                .build();
    }

    public static AmazonS3 createS3Client() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(30000);
        clientConfig.setRequestTimeout(60000);
        clientConfig.setProtocol(Protocol.HTTPS);

        AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

        return AmazonS3ClientBuilder
                .standard()
                .withClientConfiguration(clientConfig)
                .withCredentials(credentialsProvider)
                .withRegion("eu-west-1")
                .build();
    }
}


