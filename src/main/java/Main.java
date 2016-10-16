import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by korolkir on 16.10.16.
 */
public class Main extends JFrame {


    public Main() {
        setSize(1000, 1000);
        setLayout(new GridLayout(20,0));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("/Users/korolkir/Downloads/laby/лабы/мрз/MrzLab1/src/main/resources/image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(ImageBlock block: ImageDevider.devidePicture(image, Constants.BLOCK_WIDTH, Constants.BLOCK_HEIGHT)) {
            addeNewJLabel(block);
        }
        setVisible(true);
    }

    public static void main(String [] args) {
        new Main();
    }

    private void addeNewJLabel(ImageBlock block) {
        add(new JLabel(new ImageIcon(block.getSrc())));
    }


}
