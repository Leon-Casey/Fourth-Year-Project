package OpenCV;

import com.amazonaws.regions.Regions;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static javax.swing.JOptionPane.showMessageDialog;

public class Credentials extends JFrame{
    private JPanel credsPanel;
    private JPasswordField accessKeyIdTxt;
    private JPasswordField secretAccessKeyIdTxt;
    private JLabel accessKeyIdLbl;
    private JLabel secretAccessKeyIdLbl;
    private JButton loginBtn;

    public static String accessKey, secretAccessKey, region;

    public Credentials() {
        //Swing frame config
        super("Please Enter AWS Credentials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(credsPanel);
        setSize(600, 500);
        setVisible(true);
        //On click event for login button
        loginBtn.addActionListener(e -> {
            //Add credentials to credentials file. This file is held in the users home directory.
            String dir = System.getProperty("user.home") + "\\.awsCreds";
            String file = dir + "\\credentials.txt";

            File dirPath = new File(dir);
            Path filePath = Paths.get(file);

            if(!filePath.toFile().exists()) {
                try {
                    dirPath.mkdir();
                    Files.createFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            accessKey = accessKeyIdTxt.getText();
            secretAccessKey = secretAccessKeyIdTxt.getText();
            region = Regions.EU_WEST_1.getName();

            String creds = "[default]\n" +
                    "aws_access_key_id = " + accessKey + "\n" +
                    "aws_secret_access_key = " + secretAccessKey + "\n" +
                    "region = " + region;

            try {
                Files.write(filePath, creds.getBytes());

                showMessageDialog(null, "Credentials saved - Successive executions of the program will use these credentials");

                setVisible(false);
                System.exit(0);

//                MainFrame mainFrame = new MainFrame();
//                mainFrame.displayCam();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static String[] getCredentialsFileAndParse() throws IOException {
        String file = System.getProperty("user.home") + "\\.awsCreds\\credentials.txt";
        Path filePath = Paths.get(file);

        String[] creds = new String[2];

        Files.lines(filePath).forEach(line -> {
            String[] tokens = line.split(" ");
            if(line.contains("_id")) {
                 creds[0] = tokens[2];
            }
            else if(line.contains("_secret")) {
                creds[1] = tokens[2];
            }
        });
        return creds;
    }
}
