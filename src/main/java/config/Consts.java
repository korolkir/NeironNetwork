package config;


import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Created by korolkir on 16.10.16.
 */
public interface Consts {
    int BLOCK_HEIGHT  = 2;
    int BLOCK_WIDTH  = 2;
    int IMAGE_TYPE = TYPE_INT_ARGB;
    int NEURON_AMOUNT = 100;

    //todo should be renamed
    int IMAGE_COLOR_TYPE_LENGTH = 3; // "RGB" = 3, "RGBA" = 4

    double LEARNING_COEFFICIENT = 0.001;
    String IMAGE_URL = "src/main/resources/image.jpg";
}
