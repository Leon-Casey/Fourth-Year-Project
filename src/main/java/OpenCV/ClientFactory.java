package OpenCV;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.IOException;

//AKIAILEURW2FYP7QIRTQ
//7P0FUqD4owSxN/EIqpmLA090a8KDCaqieNh5ehLe

public class ClientFactory {
    public static AmazonRekognition createRekognitionClient() throws IOException {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(30000);
        clientConfig.setRequestTimeout(60000);
        clientConfig.setProtocol(Protocol.HTTPS);

        String[] creds = Credentials.getCredentialsFileAndParse();
        String accessKey = creds[0];
        String secretAccessKey = creds[1];
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretAccessKey);

        String region = Regions.EU_WEST_1.getName();

        return AmazonRekognitionClientBuilder
                .standard()
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();
    }

    public static AmazonS3 createS3Client() throws IOException {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(30000);
        clientConfig.setRequestTimeout(60000);
        clientConfig.setProtocol(Protocol.HTTPS);

        String[] creds = Credentials.getCredentialsFileAndParse();
        String accessKey = creds[0];
        String secretAccessKey = creds[1];
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretAccessKey);

        String region = Regions.EU_WEST_1.getName();

        return AmazonS3ClientBuilder
                .standard()
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();
    }
}


