package model;

import Jama.Matrix;
import config.Consts;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by korolkir on 16.10.16.
 */
public class ImageBlock {

    private BufferedImage srcImage;

    //todo should be renamed
    private final int N;

    private Matrix xMatrix;
    private Matrix yMatrix;
    private Matrix xTransposeMatrix;
    private Matrix deltaXMatrix;
    private Matrix weightsMatrix;
    private Matrix secondLayerWeightsMatrix;
    private double error;

    int positionX;
    int positionY;

    public ImageBlock(BufferedImage src) {
        this.srcImage = src;
        this.N = getLength();

        preCompressCalculations();
    }

    private int getLength() {
        return srcImage.getWidth() * srcImage.getHeight() * Consts.IMAGE_COLOR_TYPE_LENGTH;
    }

    private void preCompressCalculations() {
        xMatrix = encodeImage();
        weightsMatrix = createWeightsMatrix();
        secondLayerWeightsMatrix = weightsMatrix.transpose();
    }

    private Matrix encodeImage() {
        Matrix rgbMatrix = new Matrix(1, N);
        int k = 0;
        int imageWidth = this.srcImage.getWidth();
        int imageHeight = this.srcImage.getWidth();

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color pixelColor = new Color(srcImage.getRGB(x, y));
                //todo should be refactored
                rgbMatrix.set(0, k++, encodePixelColor(pixelColor.getRed()));
                rgbMatrix.set(0, k++, encodePixelColor(pixelColor.getGreen()));
                rgbMatrix.set(0, k++, encodePixelColor(pixelColor.getBlue()));
            }
        }

        return rgbMatrix;
    }

    private Matrix createWeightsMatrix() {
        Matrix matrix = new Matrix(Consts.NEURON_AMOUNT, N);

        for ( int i = 0; i < Consts.NEURON_AMOUNT; i++ ) {
            for ( int j = 0; j < N; j++ ) {
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
        normalizeLayers();
    }

    private void preLayersCalculations() {
        yMatrix = xMatrix.arrayTimes(weightsMatrix);  //Y(i) = X(i)*W
        xTransposeMatrix = yMatrix.arrayTimes(secondLayerWeightsMatrix.transpose()); //X`(i) = Y(i)*W` //todo check: secondLayerWeightsMatrix should be  without transpose()
        deltaXMatrix = xTransposeMatrix.minus(xMatrix); //∆X(i) = X`(i)–X(i)
    }

    private void calculateLayers() {
        double alpha = getLearningCoefficient();

        calculateFirstLayer(alpha);
        calculateSecondLayer(alpha);
    }

    //W(t + 1) = W(t) – alpha*[X(i)]T*∆X(i)*[W`(t)]T
    private void calculateFirstLayer(double alpha) {
        Matrix tempMatrix = xMatrix.transpose().times(alpha);
        tempMatrix.timesEquals(alpha);
        tempMatrix.arrayTimesEquals(deltaXMatrix.transpose()); //todo check: deltaXMatrix should be without transpose()
        tempMatrix.arrayTimesEquals(secondLayerWeightsMatrix); // todo check: secondLayerWeightsMatrix should be with transpose()

        weightsMatrix.minusEquals(tempMatrix.transpose()); //todo check tempMatrix should be without transpose()
    }

    //W`(t + 1) = W`(t) – alpha*[Y(i)]T*∆X(i)
    private void calculateSecondLayer(double alpha) {
        Matrix temp = yMatrix.transpose();
        temp.timesEquals(alpha);
        temp.arrayTimesEquals(deltaXMatrix.transpose()); //todo check tempMatrix should be without transpose()

        secondLayerWeightsMatrix.minusEquals(temp);
    }

    //todo should be implemented according formulas
    private void normalizeLayers() {
        weightsMatrix.norm1();
        secondLayerWeightsMatrix.norm1();
    }

    private void decodeImage() {
        int k = 0;

        for (int x = 0; x < srcImage.getWidth(); x++) {
            for (int y = 0; y < srcImage.getHeight(); y++) {
                for (int l = 0; l<3 ; l++, y++) {
                    srcImage.setRGB(x,y, decodePixelColor(xTransposeMatrix.get(0, k++)));
                }
            }
        }
    }

    private double getLearningCoefficient() {
        //if you have adaptive step you should implement alpha = 1 / ∑ (Y(i))2

        return Consts.LEARNING_COEFFICIENT;
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

    private double encodePixelColor(int value) {
        return 2 * value / 255 - 1;
    }

    private int decodePixelColor(double value) {
        int encodedValue  = (int) ((value + 1)  * 255 / 2);

        return encodedValue > 255 ? 255 : encodedValue < 0 ? 0 : encodedValue;
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
