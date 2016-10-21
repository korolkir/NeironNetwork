package model;

import Jama.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by korolkir on 16.10.16.
 */
public class ImageBlock {
    private BufferedImage srcImage;

    //todo should be renamed
    private final int N;

    private static final int IMAGE_COLOR_TYPE_LENGTH = 3;
    private static final double LEARNING_COEFFICIENT = 0.001;

    private Matrix xMatrix;
    private Matrix yMatrix;
    private Matrix xTransposeMatrix;
    private Matrix deltaXMatrix;
    private Matrix firstLayerWeightsMatrix;
    private Matrix secondLayerWeightsMatrix;
    private double error;
    private final int neuronsLength;
    private int positionX;
    private int positionY;

    public ImageBlock(BufferedImage src, int neuronsLength) {
        this.srcImage = src;
        this.neuronsLength = neuronsLength;
        this.N = getLength();

        preCompressCalculations();
    }

    private int getLength() {
        return srcImage.getWidth() * srcImage.getHeight() * IMAGE_COLOR_TYPE_LENGTH;
    }

    private void preCompressCalculations() {
        xMatrix = encodeImage();
        firstLayerWeightsMatrix = createWeightsMatrix();
        secondLayerWeightsMatrix = firstLayerWeightsMatrix.transpose();
    }

    private Matrix encodeImage() {
        Matrix rgbMatrix = new Matrix(1, N);
        int k = 0;
        int imageWidth = this.srcImage.getWidth();
        int imageHeight = this.srcImage.getWidth();

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color pixelColor = new Color(srcImage.getRGB(x, y));
                rgbMatrix.set(0, k++, encodePixelColor(pixelColor.getRed()));
                rgbMatrix.set(0, k++, encodePixelColor(pixelColor.getGreen()));
                rgbMatrix.set(0, k++, encodePixelColor(pixelColor.getBlue()));
            }
        }

        return rgbMatrix;
    }

    private Matrix createWeightsMatrix() {
        Matrix matrix = new Matrix(N, neuronsLength);

        for ( int i = 0; i < N; i++ ) {
            for ( int j = 0; j < neuronsLength; j++ ) {
                matrix.set(i, j, Math.random() * 2 - 1 );
            }
        }

        return matrix;
    }

    public void compress() {
        calculateWeights();
        calculateError();
        decodeImage();
    }

    private void calculateWeights() {
        preLayersCalculations();
        calculateLayers();
    }

    private void preLayersCalculations() {
        yMatrix = xMatrix.times(firstLayerWeightsMatrix);  //Y(i) = X(i)*W
        xTransposeMatrix = yMatrix.times(secondLayerWeightsMatrix); //X`(i) = Y(i)*W`
        deltaXMatrix = xTransposeMatrix.minus(xMatrix); //∆X(i) = X`(i)–X(i)
    }

    private void calculateLayers() {
        double alpha = getLearningCoefficient();

        calculateFirstLayer(alpha);
        calculateSecondLayer(alpha);
    }

    //W(t + 1) = W(t) – alpha*[X(i)]T*∆X(i)*[W`(t)]T
    private void calculateFirstLayer(double alpha) {
        Matrix tempMatrix = xMatrix.transpose();
        tempMatrix = tempMatrix.times(alpha);
        tempMatrix = tempMatrix.times(deltaXMatrix);
        tempMatrix = tempMatrix.times(secondLayerWeightsMatrix.transpose());

        firstLayerWeightsMatrix = firstLayerWeightsMatrix.minus(tempMatrix);
    }

    //W`(t + 1) = W`(t) – alpha*[Y(i)]T*∆X(i)
    private void calculateSecondLayer(double alpha) {
        Matrix temp = yMatrix.transpose();
        temp.timesEquals(alpha);
        Matrix temp2 = temp.times(deltaXMatrix);
        secondLayerWeightsMatrix = secondLayerWeightsMatrix.minus(temp2);
    }

    private void decodeImage() {
        int k = 0;

        for (int x = 0; x < srcImage.getWidth(); x++) {
            for (int y = 0; y < srcImage.getHeight(); y++) {
                int r = decodePixelColor(xTransposeMatrix.get(0, k++));
                int g = decodePixelColor(xTransposeMatrix.get(0, k++));
                int b = decodePixelColor(xTransposeMatrix.get(0, k++));
                srcImage.setRGB(x, y, new Color(r,g,b).getRGB());
            }
        }
    }

    private double getLearningCoefficient() {
        //if you have adaptive step you should implement alpha = 1 / ∑ (Y(i))2

        return LEARNING_COEFFICIENT;
    }

    private void calculateError() {
        double error = 0;

        for (int i = 0; i < N; i++) {
            error += Math.pow((xMatrix.get(0, i) - xTransposeMatrix.get(0, i)), 2);
        }

        this.error = error;
    }

    public double getError() {
        return this.error;
    }

    private double encodePixelColor(double value) {
        return 2 * value / 255 - 1;
    }

    private int decodePixelColor(double value) {
        int encodedValue  = (int) ((value + 1)  * 255 / 2);

        if (encodedValue > 255) {
            return 255;
        } else if (encodedValue < 0) {
            return 0;
        }

        return encodedValue;
    }

    public BufferedImage getSrcImage() {
        return srcImage;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}