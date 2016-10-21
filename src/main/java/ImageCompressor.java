import config.Consts;
import model.ImageBlock;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.kalenkevich on 21.10.2016.
 */
public class ImageCompressor {

    private BufferedImage image;

    public ImageCompressor(BufferedImage image) {
        this.image = image;
    }

    public boolean canCompress() {
        //todo should be implemented
        return false;
    }

    public BufferedImage compress() {
        List<ImageBlock> blocks = devideImage(image, Consts.BLOCK_WIDTH, Consts.BLOCK_HEIGHT);

        for (ImageBlock block : blocks) {
            block.compress();
        }

        return unite(blocks);
    }

    public List<ImageBlock> devideImage(BufferedImage image, int blockWidth, int blockHeight) {
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

    public BufferedImage unite(List<ImageBlock> blocks) {
        int blockWidth = Consts.BLOCK_WIDTH;
        int blockHeight = Consts.BLOCK_HEIGHT;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        BufferedImage compressedImage = new BufferedImage(imageWidth, imageHeight, Consts.IMAGE_TYPE);

        for (ImageBlock block : blocks) {
            int []array = block.getSrcImage().getRGB(0, 0, blockWidth, blockHeight, null, 0, blockWidth);
            compressedImage.setRGB(block.getPositionX(), block.getPositionY(), blockWidth, blockHeight, array, 0, 1);
        }

        return compressedImage;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
