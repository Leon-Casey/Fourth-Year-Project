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
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });


//        Statement statement = conn.createStatement();
//        String sql = "SELECT * FROM users";
//        ResultSet rs = statement.executeQuery(sql);
//        ResultSetMetaData rsmd = rs.getMetaData();
//        int columnsNumber = rsmd.getColumnCount();
//        while (rs.next()) {
//            for (int i = 1; i <= columnsNumber; i++) {
//                if (i > 1) System.out.print(",  ");
//                String columnValue = rs.getString(i);
//                System.out.print(columnValue + " " + rsmd.getColumnName(i));
//            }
//            System.out.println("");
//        }
    }

    public void displayCam() throws IOException {
        Mat webcamImage = new Mat();
        //todo: index 0 or 1
        VideoCapture videoCapture = new VideoCapture(0);
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
    }
}
