package model;

import java.awt.image.BufferedImage;

/**
 * Created by a.kalenkevich on 22.10.2016.
 */
public class ImageCompressorBuilder {

    private BufferedImage image;
    private int blockWidth;
    private int blockHeight;
    private int neuronsLength;
    private int maxError;

    public ImageCompressorBuilder setImage(BufferedImage image) {
        this.image = image;

        return this;
    }

    public ImageCompressorBuilder setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;

        return this;
    }

    public ImageCompressorBuilder setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;

        return this;
    }

    public ImageCompressorBuilder setNeuronsLength(int neuronsLength) {
        this.neuronsLength = neuronsLength;

        return this;
    }

    public ImageCompressorBuilder setMaxError(int maxError) {
        this.maxError = maxError;

        return this;
    }

    public ImageCompressor build(){
        return new ImageCompressor(image, blockWidth, blockHeight, neuronsLength, maxError);
    }
}
