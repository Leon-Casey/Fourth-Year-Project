package OpenCV;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Detector {

    private CascadeClassifier cascadeClassifier;
    private MatOfRect detectedFaces;
    private Mat colouredImage;
    private Mat greyImage;
    private AmazonRekognition rekognitionClient;
    private AmazonS3 s3Client;
    private ObjectListing bucket;
    private List<S3ObjectSummary> summaries;
    private Connection conn;


    CompareFacesRequest compareRequest;
    int reqNum;

    public Detector() throws IOException, SQLException {
        this.detectedFaces = new MatOfRect();
        this.colouredImage = new Mat();
        this.greyImage = new Mat();
//        this.cascadeClassifier = new CascadeClassifier("home/lcasey/projects/OpenCVRekognition/opencv/data/haarcascade_frontalface_alt.xml");
//        this.cascadeClassifier = new CascadeClassifier("/home/lcasey/projects/OpenCVRekognition/opencv/data/haarcascades/haarcascade_frontalface_default.xml");
        // https://github.com/opencv/opencv/tree/master/data/lbpcascades
        this.cascadeClassifier = new CascadeClassifier(System.getProperty("user.dir") + "/lbpcascade_frontalface_improved.xml");
        rekognitionClient = ClientFactory.createRekognitionClient();
        s3Client = ClientFactory.createS3Client();
        bucket = s3Client.listObjects("rekog.faces");
        summaries = bucket.getObjectSummaries();

        String rootDirectory=System.getProperty("user.dir");
        String resourceDirectory=rootDirectory+"/resources/";

        Properties prop = new Properties();
        prop.load(new FileInputStream(resourceDirectory+"db.properties"));

        String db_hostname = prop.getProperty("db_hostname");
        String db_username = prop.getProperty("db_username");
        String db_password = prop.getProperty("db_password");
        String db_database = prop.getProperty("db_database");


        conn = DriverManager.getConnection("jdbc:mysql://fyp.co9aylc8lacr.eu-west-1.rds.amazonaws.com/FYP_Schema", db_username, db_password);

        while (bucket.isTruncated()) {
            bucket = s3Client.listNextBatchOfObjects(bucket);
            summaries.addAll(bucket.getObjectSummaries());
        }

        reqNum = 0;
    }

    public Mat detect(Mat inputFrame, int frameCounter) {
        inputFrame.copyTo(colouredImage);
        inputFrame.copyTo(greyImage);

        Imgproc.cvtColor(colouredImage, greyImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(greyImage, greyImage);

        cascadeClassifier.detectMultiScale(greyImage, detectedFaces);

//        File profileImgToImport = new File("test.jpg");
//        File profileImg = new File("testProfile.jpg");
//
//        BufferedImage profile = ImageIO.read(profileImgToImport);
//        ImageIO.write(profile, "jpg", profileImg);

//        BufferedImage newImage = new BufferedImage(
        //    in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

//        BufferedImage img1 = ImageIO.read(new File("Lenna50.jpg"));
//        BufferedImage img2 = ImageIO.read(new File("Lenna100.jpg"));

        showFacesOnScreen(detectedFaces, frameCounter);

        return colouredImage;
    }

    private void showFacesOnScreen(MatOfRect detectedFaces, int frameCounter) {
        for (Rect rect : detectedFaces.toArray()) {
            Imgproc.rectangle(colouredImage, new Point(rect.x, rect.y), new Point(
                    rect.x + rect.width, rect.y + rect.height), new Scalar(250, 80, 80), 2);
            if (frameCounter % 100 == 0) {
                new Thread(() -> {
                    //                File file = new File("test.png");
                    MatOfByte mob = new MatOfByte();
                    Imgcodecs.imencode(".png", colouredImage, mob);
                    byte[] ba = mob.toArray();


                    ByteBuffer byteBufferImg = ByteBuffer.wrap(ba);
                    try {
                        attemptRecognition(byteBufferImg, frameCounter);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

//            String image1 = "/home/lcasey/projects/OpenCVRekognition/test.png";
//            String image2 = "/home/lcasey/projects/OpenCVRekognition/testProfile.jpg";
//
//            byte[] bytesImg1;
//            try {
//                bytesImg1 = Files.readAllBytes(Paths.get(image1));
//            } catch (IOException e) {
//                System.err.println("Failed to load source image: " + e.getMessage());
//                return;
//            }
//
//            byte[] bytesImg2;
//            try {
//                bytesImg2 = Files.readAllBytes(Paths.get(image2));
//            } catch (IOException e) {
//                System.err.println("Failed to load target image: " + e.getMessage());
//                return;
//            }
//
//            ByteBuffer byteBufferImg1 = ByteBuffer.wrap(bytesImg1);
//            ByteBuffer byteBufferImg2 = ByteBuffer.wrap(bytesImg2);
//
//            CompareFacesRequest request = new CompareFacesRequest()
//                    .withSourceImage(new Image().withBytes(byteBufferImg1))
//                    .withTargetImage(new Image().withBytes(byteBufferImg2));

//            ByteBuffer byteBufferImg = ByteBuffer.wrap(ba);
//
//            CompareFacesRequest request = new CompareFacesRequest()
//                    .withSourceImage(new Image().withS3Object(new S3Object().withName("princeWill1.png").withBucket("rekog.faces")))
//                    .withTargetImage(new Image().withBytes(byteBufferImg2))
//                    .withSimilarityThreshold(80F);

//            CompareFacesResult result = rekognitionClient.compareFaces(request);
//
//
//            List<CompareFacesMatch> faceMatches = result.getFaceMatches();
//            for (CompareFacesMatch match : faceMatches) {
//                Float similarity = match.getSimilarity();
//                System.out.println("Similarity: " + similarity);
//            }
//            final BufferedImage profileImg = ImageIO.read(new File("testProfile.png"));
//            final BufferedImage img = ImageIO.read(new File("test.png"));
//            double p = compareFaces(profileImg, img);
//            System.out.println("diff percent: " + p);
        }
    }

    private void attemptRecognition(ByteBuffer faceInBytes, int frameCounter) throws SQLException, ClassNotFoundException {

        reqNum++;
        AtomicBoolean matchFound = new AtomicBoolean(false);

        while (matchFound.get() == false) {
            for (S3ObjectSummary object : summaries) {
                compareRequest = new CompareFacesRequest()
                        .withSourceImage(new Image().withS3Object(new S3Object().withBucket("rekog.faces").withName(object.getKey())))
                        .withTargetImage(new Image().withBytes(faceInBytes))
                        .withSimilarityThreshold(90F);

                CompareFacesResult result = rekognitionClient.compareFaces(compareRequest);

                if (!result.getFaceMatches().isEmpty()) {
                    matchFound.set(true);
                    System.out.println("REQ NUM: " + reqNum + " MATCHED WITH: " + object.getKey() + " ON FRAME " + frameCounter);

                    try {
                        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET lastVisit=? WHERE identifier=?");

                        stmt.setTimestamp(1, getCurrentTimeStamp());
                        stmt.setString(2, object.getKey().split("[.]")[0]);

                        int row = stmt.executeUpdate();
                        System.out.println("Updated row " + row);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static Timestamp getCurrentTimeStamp() {

        Date today = new Date();
        long time = today.getTime();
        Timestamp ts = new Timestamp(time);
        return ts;

    }
}



//
////                    List<CompareFacesMatch> faceMatches = result.getFaceMatches();
////                    for (CompareFacesMatch match : faceMatches) {
////                        Float similarity = match.getSimilarity();
////                        System.out.println("Request " + reqNum + ": MATCHED WITH " + match.toString() + " Similarity: " + similarity);
////                    }
//            }
//            });
