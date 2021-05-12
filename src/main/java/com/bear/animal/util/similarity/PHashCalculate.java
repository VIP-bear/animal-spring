package com.bear.animal.util.similarity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

/**
 * 计算图片之间的相似度:
 * 感知哈希计算:
 * 1. 缩放图片尺寸为8*8
 * 2. 简化色彩，转为阶灰度图
 * 3. 计算DCT，得到32*32的DCT系数矩阵
 * 4. 缩小DCT，只保留左上角的8*8的矩阵
 * 5. 计算DCT的平均值
 * 6. 计算哈希值
 * @author bear
 * @date 2021年04月24日 23:59
 */
public class PHashCalculate {

    private GrayAndResize grayAndResize;

    public PHashCalculate() {
        grayAndResize = new GrayAndResize();
    }

    /**
     * 感知哈希算法计算得到图片指纹（哈希）
     * @param src
     * @return
     */
    public String pHash(BufferedImage src) {
        int width = 8;
        int height = 8;
        // 缩放图片尺寸
        BufferedImage image = grayAndResize.resize(src, height, width);
        // 灰度值
        int[] dctData = new int[width * height];
        int index = 0;
        // 计算每个位置的灰度值
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // 获取图片（j, i）位置像素
                int pixel = image.getRGB(j, i);
                // 获取灰度值
                int gray = grayAndResize.gray(pixel);
                dctData[index++] = gray;
            }
        }

        // 离散余弦变换
        dctData = DCT(dctData, width);
        // 求灰度像素的均值
        int avg = averageGray(dctData, width, height);
        // 图片指纹（哈希）
        StringBuilder hashValue = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (dctData[i * height + j] >= avg) {
                    // 大于均值，记为1
                    hashValue.append("1");
                } else {
                    // 小于均值，记为0
                    hashValue.append("0");
                }
            }
        }
        return hashValue.toString();
    }

    /**
     * 离散余弦变换
     * @param pix
     * @param n
     * @return
     */
    private int[] DCT(int[] pix, int n) {
        double[][] matrix = new double[n][n];
        // 一维灰度值转二维灰度值
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (double)(pix[i * n + j]);
            }
        }
        // 求系数矩阵
        double[][] quotient = coefficient(n);
        // 求转置系数矩阵
        double[][] quotientT = transposeMatrix(quotient);
        // 矩阵相乘
        double[][] temp = matrixMultiply(quotient, matrix, n);
        matrix = matrixMultiply(temp, quotientT, n);

        // 得到系数矩阵
        int[] res = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i * n + j] = (int)matrix[i][j];
            }
        }
        return res;
    }

    private double[][] matrixMultiply(double[][] A, double[][] B, int n) {
        double[][] res = new double[n][n];
        double t = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                t = 0;
                for (int k = 0; k < n; k++) {
                    t += A[i][k] * B[k][j];
                }
                res[i][j] = t;
            }
        }
        return res;
    }

    /**
     * 转置矩阵
     * @param quotient
     * @return
     */
    private double[][] transposeMatrix(double[][] quotient) {
        int n = quotient.length;
        double[][] tMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tMatrix[i][j] = quotient[j][i];
            }
        }
        return tMatrix;
    }

    /**
     * 求系数矩阵
     * @param n
     * @return
     */
    private double[][] coefficient(int n) {
        double[][] coe = new double[n][n];
        double sqrt = 1.0 / Math.sqrt(n);
        // 第一行系数
        for (int i = 0; i < n; i++) {
            coe[0][i] = sqrt;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                coe[i][j] = Math.sqrt(2.0 / n) * Math.cos(i * Math.PI * (j + 0.5) / (double)n);
            }
        }
        return coe;
    }

    /**
     * 求灰度像素的平均值
     * @param pix
     * @param width
     * @param height
     * @return
     */
    private int averageGray(int[] pix, int width, int height) {
        int sum = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sum += pix[i * width + j];
            }
        }
        return sum / (width * height);
    }

    /**
     * 计算字符串的汉明距离（同一位置上的字符，相同为0，不同为1）
     * @param c1
     * @param c2
     * @return
     */
    public int distance(char[] c1, char[] c2) {
        int diffCount = 0;
        for (int i = 0; i < c1.length; i++) {
            if (c1[i] == c2[i]) {
                diffCount++;
            }
        }
        return diffCount;
    }

    /**
     * 将图片url转换成BufferedImage
     * @param imageUrl
     * @return
     */
    public BufferedImage getBufferedImage(String imageUrl) {
        BufferedImage image = null;
        try {
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            image = ImageIO.read(inputStream);
        } catch (Exception e) {
            return null;
        } finally {
            return image;
        }
    }
}
