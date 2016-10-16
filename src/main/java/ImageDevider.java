import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by korolkir on 16.10.16.
 */
public class ImageDevider {

    static public List<ImageBlock> devidePicture(BufferedImage image, int blockWidth, int blockHeight) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        List<ImageBlock> blocks = new ArrayList<ImageBlock>();

        int deltaX = image.getWidth() / blockWidth;
        int deltaY = image.getHeight() / blockHeight;

        for (int x = 0; x < image.getWidth(); x += blockWidth) {
            for (int y = 0; y < image.getHeight(); y += blockHeight) {
                blocks.add(new ImageBlock(image.getSubimage(x, y, blockWidth, blockHeight)));
            }
        }
        return blocks;
    }
}
