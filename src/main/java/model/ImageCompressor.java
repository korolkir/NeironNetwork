package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;

/**
 * Created by a.kalenkevich on 21.10.2016.
 */
public class ImageCompressor {

    private static final int MAX_THREAD_LENGTH = 10;

    private BufferedImage image;
    private BufferedImage compressedImage;
    private List<ImageBlock> blocks;
    private double currentError;
    private double compressCoef;
    private final int blockWidth;
    private final int blockHeight;
    private final int neuronsLength;
    private final int maxError;

    //todo it's hack
    private boolean wasFirstIteration;

    public ImageCompressor(BufferedImage image, int blockWidth, int blockHeight, int neuronsLength, int maxError) {
        this.image = image;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.neuronsLength = neuronsLength;
        this.maxError = maxError;

        setDefaults();
    }

    private void setDefaults() {
        blocks = devideImage(blockWidth, blockHeight);
        compressedImage = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_ARGB_PRE);
        currentError = 0;
        wasFirstIteration = false;
        compressCoef = (double) (blockWidth * blockHeight * 3 * blockWidth * blockHeight) / ((blockWidth * blockHeight * 3 + blockWidth * blockHeight) * neuronsLength + 2);
    }

    public boolean canCompress() {
        return this.currentError < this.maxError;
    }

    public void compress() {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_LENGTH);
        for (final ImageBlock block : blocks) {
            executorService.execute(new Runnable() {
                public void run() {
                    block.compress();
                }
            });
        }
        executorService.shutdown();
    }

    public BufferedImage getCompressedImage() {
        uniteBlocks();

        return this.compressedImage;
    }

    public double getCurrentError() {
        calculateError();

        return currentError;
    }

    public double getCompressCoef() {
        return this.compressCoef;
    }

    private List<ImageBlock> devideImage(int blockWidth, int blockHeight) {
        List<ImageBlock> blocks = new ArrayList<ImageBlock>();

        for (int x = 0; x < image.getWidth(); x += blockWidth) {
            for (int y = 0; y < image.getHeight(); y += blockHeight) {
                ImageBlock block = new ImageBlock(image.getSubimage(x, y, blockWidth, blockHeight), neuronsLength);
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
            this.currentError += block.getError();
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
