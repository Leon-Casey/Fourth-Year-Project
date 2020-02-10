package OpenCV;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EmotionDetection {

    public static AmazonRekognition rekognitionClient;
    public static AmazonS3 s3Client;

    public EmotionDetection() {
            try {
                rekognitionClient = ClientFactory.createRekognitionClient();
                s3Client = ClientFactory.createS3Client();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static String detect(String img) {

            byte[] bytesImg = new byte[0];
            try {
                bytesImg = Files.readAllBytes(Paths.get(img));
            } catch (IOException e) {
                System.err.println("Failed to load source image: " + e.getMessage());
            }

            ByteBuffer byteBufferImg = ByteBuffer.wrap(bytesImg);

            DetectFacesRequest request = new DetectFacesRequest()
                    .withImage(new Image()
                    .withBytes(byteBufferImg))
                    .withAttributes(Attribute.ALL);

                    DetectFacesResult result = rekognitionClient.detectFaces(request);

                    List<FaceDetail> faceDetails = result.getFaceDetails();

                    String emotion = "";
                    float maxConfidence = 0;

                    if(!faceDetails.isEmpty()) {
                        for (FaceDetail face : faceDetails) {
                            for(Emotion e : face.getEmotions()) {
                                if(e.getConfidence() > maxConfidence) {
                                    maxConfidence = e.getConfidence();
                                    emotion = e.getType();
                                }
                            }
                        }
                    }
                    else {
                        emotion = "no emotion detected";
                    }
                    return emotion;
    }
}
