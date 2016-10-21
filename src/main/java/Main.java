import config.Consts;
import model.ImageCompressor;
import model.ImageCompressorBuilder;

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
        setDefaults();

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(Consts.IMAGE_URL));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageCompressor imageCompressor = new ImageCompressorBuilder()
                .setImage(image)
                .setBlockWidth(Consts.BLOCK_WIDTH)
                .setBlockHeight(Consts.BLOCK_HEIGHT)
                .setNeuronsLength(Consts.NEURON_AMOUNT)
                .setMaxError(Consts.MAX_ERROR)
                .build();

        JLabel compressedImageLabel = new JLabel();
        JLabel uncompressedImageLabel = new JLabel(new ImageIcon(image));

        add(uncompressedImageLabel, BorderLayout.WEST);
        add(compressedImageLabel, BorderLayout.EAST);
        setVisible(true);

        int iteration = 1;

        //while (imageCompressor.canCompress()) {
        for (int i = 0; i < 100; i++) {
            imageCompressor.compress();
            double error = imageCompressor.getCurrentError();

            compressedImageLabel.setIcon(new ImageIcon(imageCompressor.getCompressedImage()));
            System.out.println("Iteration: " + iteration++);
            System.out.println("Error: " + error);
            System.out.println("----------------");
        }

        double compressCoef = imageCompressor.getCompressCoef();

        System.out.println("Compression finished");
        System.out.println("Compression coefficient: " + compressCoef);
    }

    private void setDefaults() {
        setSize(1000, 1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public static void main(String [] args) {
        new Main();
    }
}
