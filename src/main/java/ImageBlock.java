import Jama.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by korolkir on 16.10.16.
 */
public class ImageBlock {

    private BufferedImage src;
    private final int N;
    private Matrix weights;
    private Matrix transposeWeights;

    public ImageBlock(BufferedImage src) {
        this.src = src;
        this.N = src.getWidth() * src.getHeight() * 3;
    }

    public BufferedImage getSrc() {
        return src;
    }

    public void compress() {
        calculateWeights();
        decodeImage();
    }

    private void calculateWeights() {
        Matrix matrixX = getRGBArray();

        weights = createWeightsMatrix();
        transposeWeights = weights.transpose();

        Matrix matrixY = matrixX.arrayTimes(weights);m
        Matrix transposeX = matrixY.arrayTimes(weights);
        Matrix deltaX = transposeX.minus(matrixX);

        double alphaX = 0.001;
        double alphaY = 0.001;

        Matrix w1 = matrixX.transpose();
        Matrix w2 = w1.times(alphaX);
        Matrix w3 = w2.arrayTimes(deltaX);
        Matrix w4 = transposeWeights.transpose();//todo check it with weights
        Matrix w5 = w3.plus(w4);

        weights = weights.minus(w5);

        Matrix tw1 = matrixY.transpose();
        Matrix tw2 = tw1.times(alphaY);
        Matrix tw3 = tw2.arrayTimes(deltaX);

        transposeWeights = transposeWeights.minus(tw3);
        weights.norm1();
        transposeWeights.norm1();
    }

    private void decodeImage() {
        Matrix matrixX = getRGBArray();
        Matrix matrixY = matrixX.arrayTimes(weights);
        Matrix transposeMatrixX = matrixY.arrayTimes(transposeWeights);
        int k = 0;

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                Color colorPixel = new Color(src.getRGB(x, y));
                for (int l = 0; l<3 ; l++, y++) {
                    int pixel  = (int)(transposeMatrixX.get(0, k++) + 1) * 255 / 2;
                    src.setRGB(x,y, encodePixelColor(pixel));
                }
            }
        }

    }

    public double getError() {
        Matrix matrixX = getRGBArray();
        Matrix matrixY = matrixX.arrayTimes(weights);
        Matrix transposeMatrixX = matrixY.arrayTimes(transposeWeights);
        double error = 0;

        for (int i=0;i < N; i++ ) {
            error += Math.pow((matrixX.get(0, i) - transposeMatrixX.get(0, i)), 2);
        }

        return error;
    }

    //1 formula
    private Matrix getRGBArray(){
        Matrix X = new Matrix(2, N);
        int k = 0;
        for (int x = 0; x < this.src.getWidth(); x++) {
            for (int y = 0; y<this.src.getHeight(); y++) {
                Color pixelColor = new Color(src.getRGB(x,y));
                X.set(0, k++, decodePixelColor(pixelColor.getRed()));
                X.set(0, k++, decodePixelColor(pixelColor.getGreen()));
                X.set(0, k++,decodePixelColor(pixelColor.getBlue()));
            }
        }

        return X;
    }

    //formula 2
    private Matrix createWeightsMatrix() {
        Matrix matrix = new Matrix(Constants.NEIRON_AMOUNT, N);
        for(int i = 0; i < Constants.NEIRON_AMOUNT; i++) {
            for(int j = 0; j < N; j++) {
                matrix.set(i, j, Math.random() * 2 - 1 );
            }
        }

        return matrix;
    }

    private double decodePixelColor(int value) {
        return 2 * value / 255 - 1;
    }

    private int encodePixelColor(int value) {
        return value > 255 ? 255 : value < 0 ? 0 : value;
    }
}
