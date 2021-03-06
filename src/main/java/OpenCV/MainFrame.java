package OpenCV;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private Detector detector;
    private CameraPanel cameraPanel;
    private Mat profileImage;
    private int frameCounter = 0;

    public MainFrame() throws IOException, SQLException, ClassNotFoundException {
        super("FYP");
        //        Loader.load(opencv_java.class);
        OpenCV.loadLocally();
        //        System.loadLibrary(NATIVE_LIBRARY_NAME);
        detector = new Detector();
        cameraPanel = new CameraPanel();
        setContentPane(cameraPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        setSize(600, 500);
        setVisible(true);
    }

    public void displayCam(int camIndex) throws IOException {
        Mat webcamImage = new Mat();
        VideoCapture videoCapture = new VideoCapture(camIndex);
        videoCapture.set(Videoio.CAP_PROP_FPS, 30);

        if (videoCapture.isOpened()) {
            while (true) {
                videoCapture.read(webcamImage);
                setSize(webcamImage.width(), webcamImage.height());
                if (!webcamImage.empty()) {
                    frameCounter++;
                    webcamImage = detector.detect(webcamImage, frameCounter);
                    cameraPanel.convertMatToImage(webcamImage);
                    cameraPanel.repaint();
                } else {
                    System.out.println("Error");
                    break;
                }
            }
        }
        else {
            displayCam(camIndex + 1);
        }
    }
}
