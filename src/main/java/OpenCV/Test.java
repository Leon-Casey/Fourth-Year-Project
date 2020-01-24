package OpenCV;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.opencv.core.Core;

import java.util.List;

public class Test {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    static AmazonS3 s3Client = ClientFactory.createS3Client();


    public static void main(String[] args) {
        System.out.println("Version: " + Core.VERSION);
        System.out.println(System.getProperty("user.dir"));

        ObjectListing bucket = s3Client.listObjects("rekog.faces");
        List<S3ObjectSummary> summaries = bucket.getObjectSummaries();

        while (bucket.isTruncated()) {
            bucket = s3Client.listNextBatchOfObjects(bucket);
            summaries.addAll (bucket.getObjectSummaries());
        }

        System.out.println(summaries);
    }
}
