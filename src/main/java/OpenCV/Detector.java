package OpenCV;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Detector {

    private CascadeClassifier cascadeClassifier;
    private MatOfRect detectedFaces;
    private Mat colouredImage;
    private Mat greyImage;
    private AmazonRekognition rekognitionClient;
    private AmazonS3 s3Client;

    public Detector() {
        this.detectedFaces = new MatOfRect();
        this.colouredImage = new Mat();
        this.greyImage = new Mat();
//        this.cascadeClassifier = new CascadeClassifier("home/lcasey/projects/OpenCVRekognition/opencv/data/haarcascade_frontalface_alt.xml");
//        this.cascadeClassifier = new CascadeClassifier("/home/lcasey/projects/OpenCVRekognition/opencv/data/haarcascades/haarcascade_frontalface_default.xml");
        // https://github.com/opencv/opencv/tree/master/data/lbpcascades
        this.cascadeClassifier = new CascadeClassifier(System.getProperty("user.dir") + "/lbpcascade_frontalface_improved.xml");
//        rekognitionClient = ClientFactory.createRekognitionClient();
//        s3Client = ClientFactory.createS3Client();
//        ObjectListing bucket = s3Client.listObjects("rekog.faces");
//        List<S3ObjectSummary> summaries = bucket.getObjectSummaries();
//
//        while (bucket.isTruncated()) {
//            bucket = s3Client.listNextBatchOfObjects(bucket);
//            summaries.addAll(bucket.getObjectSummaries());
//        }
    }

    public Mat detect(Mat inputFrame) throws IOException {
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

        showFacesOnScreenAndCaptureFace(detectedFaces);

        return colouredImage;
    }

    private void showFacesOnScreenAndCaptureFace(MatOfRect detectedFaces) throws IOException {
        for (Rect rect : detectedFaces.toArray()) {
            Imgproc.rectangle(colouredImage, new Point(rect.x, rect.y), new Point(
                    rect.x + rect.width, rect.y + rect.height), new Scalar(250, 80, 80), 2);
            File file = new File("test.png");
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".png", colouredImage, mob);
            byte[] ba = mob.toArray();

            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(ba));
            try {
                ImageIO.write(bi.getSubimage(rect.x + 2, rect.y + 2, rect.width - 4, rect.height - 4), "png", file);
            } catch (IOException ex) {
                Logger.getLogger(Detector.class.getName()).log(Level.SEVERE, null, ex);
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
}
