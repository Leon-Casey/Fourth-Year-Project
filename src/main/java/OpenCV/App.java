package OpenCV;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class App {
    public App() {
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, SQLException {

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }

        String file = System.getProperty("user.home") + "\\.awsCreds\\credentials.txt";
        Path path = Paths.get(file);
        //Check if credentials file exists
        if(path.toFile().exists()) {
            MainFrame mainFrame = new MainFrame();
            mainFrame.displayCam();
        }
        else {
            JFrame credsFrame = new Credentials();
        }
//        MainFrame mainFrame = new MainFrame();
//        mainFrame.displayCam();
    }
}