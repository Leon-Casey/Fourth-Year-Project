package OpenCV;

import nu.pattern.OpenCV;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_java;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.JFrame;
import java.io.IOException;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Detector detector;
    private CameraPanel cameraPanel;
    private Mat profileImage;

    public MainFrame() {
        super("FYP Facial Detection Test");
//        Loader.load(opencv_java.class);
        OpenCV.loadLocally();
        //        System.loadLibrary(NATIVE_LIBRARY_NAME);
        detector = new Detector();
        cameraPanel = new CameraPanel();
        setContentPane(cameraPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setVisible(true);
    }

    public void displayScreen() throws IOException {
        Mat webcamImage = new Mat();
        VideoCapture videoCapture = new VideoCapture(0);
        videoCapture.set(Videoio.CAP_PROP_FPS, 10);

        if (videoCapture.isOpened()) {
            while (true) {
                videoCapture.read(webcamImage);
                setSize(webcamImage.width(), webcamImage.height());
                if (!webcamImage.empty()) {
                    webcamImage = detector.detect(webcamImage);
                    cameraPanel.convertMatToImage(webcamImage);
                    cameraPanel.repaint();
                } else {
                    System.out.println("Error");
                    break;
                }
            }
        }
    }
}
