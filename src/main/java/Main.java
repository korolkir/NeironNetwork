import config.Consts;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by korolkir on 16.10.16.
 */
public class Main extends JFrame {

    public Main() {
        setSize(1000, 1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(Consts.IMAGE_URL));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageCompressor imageCompressor = new ImageCompressor(image);
        JLabel compressedImageLabel = new JLabel();
        JLabel uncompressedImageLabel = new JLabel(new ImageIcon(image));

        add(uncompressedImageLabel, BorderLayout.WEST);
        add(compressedImageLabel, BorderLayout.EAST);
        setVisible(true);

        //while(imageCompressor.canCompress()) {
        compressedImageLabel.setIcon(new ImageIcon(imageCompressor.compress()));
        //}
    }

    public static void main(String [] args) {
        new Main();
    }
}
