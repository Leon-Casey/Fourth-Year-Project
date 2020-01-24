package OpenCV;

import org.opencv.core.Mat;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.swing.JPanel;


public class CameraPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage image;

    public CameraPanel() {
    }

    public boolean convertMatToImage(Mat matBGR) throws IOException {
        int width = matBGR.width();
        int height = matBGR.height();
        int channels = matBGR.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        matBGR.get(0, 0, sourcePixels);
        this.image = new BufferedImage(width, height, 5);
        byte[] targetPixels = ((DataBufferByte)this.image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        return true;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.image != null) {
            g.drawImage(this.image, 10, 10, this.getWidth(), this.image.getHeight(), (ImageObserver)null);
        }
    }
}
