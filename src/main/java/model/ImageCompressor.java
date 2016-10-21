package model;

import config.Consts;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.kalenkevich on 21.10.2016.
 */
public class ImageCompressor {

    private BufferedImage image;
    private BufferedImage compressedImage;
    private List<ImageBlock> blocks;
    private double error;

    public ImageCompressor(BufferedImage image) {
        this.image = image;

        setDefaults();
    }

    private void setDefaults() {
        blocks = devideImage(Consts.BLOCK_WIDTH, Consts.BLOCK_HEIGHT);
        compressedImage = new BufferedImage(image.getWidth(), image.getHeight(), Consts.IMAGE_TYPE);
        error = 0;
    }

    public boolean canCompress() {
        //todo should be implemented
        return false;
    }

    public void compress() {
        for (ImageBlock block : blocks) {
            block.compress();
        }
    }

    public BufferedImage getCompressedImage() {
        uniteBlocks();

        return this.compressedImage;
    }

    public double getError() {
        calculateError();

        return error;
    }

    private List<ImageBlock> devideImage(int blockWidth, int blockHeight) {
        List<ImageBlock> blocks = new ArrayList<ImageBlock>();

        for (int x = 0; x < image.getWidth(); x += blockWidth) {
            for (int y = 0; y < image.getHeight(); y += blockHeight) {
                ImageBlock block = new ImageBlock(image.getSubimage(x, y, blockWidth, blockHeight));
                block.setPositionX(x);
                block.setPositionY(y);
                blocks.add(block);
            }
        }

        return blocks;
    }

    private BufferedImage uniteBlocks() {

        for (ImageBlock block : blocks) {
            compressedImage.getGraphics().drawImage(block.getSrcImage(), block.getPositionX(), block.getPositionY(), null);
        }

        return compressedImage;
    }

    private void calculateError() {
        for(ImageBlock block: blocks) {
            this.error += block.getError();
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
