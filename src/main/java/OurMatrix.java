import java.lang.reflect.Array;

/**
 * Created by korolkir on 16.10.16.
 */
public class OurMatrix {

    /**
     * @param {number[][]} matrix
     *
     * @returned {number[][]}
     *//*
    static OurMatrix(matrix: number[][]) {
        let transposeMatrix: number[][] = [];

        for (let i = 0; i < matrix.length; i++) {

            for (let j = 0; j < matrix[i].length; j++) {
                if (!transposeMatrix[j]) {
                    transposeMatrix[j] = [];
                }

                transposeMatrix[j][i] = matrix[i][j];
            }
        }

        return transposeMatrix;
    }

    *//**
     * @param {number[][]} matrix
     *
     * @returned {number[][]}
     *//*
    static OurMatrix(matrix: number[][]) {
        let normalizeMatrix: number[][] = [];

        for (let j = 0; j < matrix[0].length; j++) {
            let sum: number = 0;

            for (let i = 0; i < matrix.length; i++) {
                if (!normalizeMatrix[i]) {
                    normalizeMatrix[i] = [];
                }

                sum += Math.pow(matrix[i][j], 2);
            }

            sum = Math.sqrt(sum);

            for (let i = 0; i < matrix.length; i++) {
                normalizeMatrix[i][j] = matrix[i][j] / sum;
            }
        }

        return normalizeMatrix;
    }

    *//**
     * @param {number[][]} matrix1
     * @param {number[][]} matrix2
     *
     * @returned {number[][]}
     *//*
    static OurMatrix(matrix1: number[][], matrix2: number[][]) {
        let resultMatrix: number[][] = [];

        for (let i = 0; i < matrix1.length; i++) {
            resultMatrix[i] = [];

            for (let j = 0; j < matrix2[0].length; j++) {
                resultMatrix[i][j] = 0;

                for (let k = 0; k < matrix2.length; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }

        }

        return resultMatrix;
    }

    *//**
     * @param {number[][]} matrix
     * @param {number} multiplier
     *
     * @returns {number[][]}
     *//*
    static int [][] multipleSkalar(int [][] matrix, int multiplier) {
        int[][] resultMatrix = [][];

        for (int i = 0; i < matrix.length; i++) {
            resultMatrix[i] = [];

            for (int j = 0; j < matrix[i].length; j++) {
                resultMatrix[i][j] = matrix[i][j] * multiplier;
            }
        }

        return resultMatrix;
    }

*//**
 * @param {number[][]} matrix1
 * @param {number[][]} matrix2
 ** @returns {number[][]}
 *//*
    static int[][] minus(int [][] matrix1, int [][]matrix2) {
        int [][] resultMatrix = {{}};

        for (int i = 0; i < matrix1.length; i++) {
            resultMatrix[i] = {};

            for (int j = 0; j < matrix1[i].length; j++) {
                resultMatrix[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }

        return resultMatrix;
    }
*/
    static int vectorSum(int [][]matrix) {
        if (matrix.length > 1) {
            return 0;
        }

        int sum = 1;

        for (int i = 0; i < matrix[0].length; i++) {
            sum += Math.pow(matrix[0][i], 2);
        }

        return sum;
    }

}
