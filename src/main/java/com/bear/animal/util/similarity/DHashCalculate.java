package com.bear.animal.util.similarity;

import java.awt.image.BufferedImage;

/**
 * 计算图片之间的相似度:
 * 差异哈希计算:
 * 使用当前像素与后一个像素点做对比得到布尔值
 * 1. 缩放图片尺寸为9*8
 * 2. 简化色彩，转为256阶灰度图
 * 3. 计算灰度差值，: 像素大于后一个像素均值的记为1，反之为0，得到8*8=64位
 * 4. 生成哈希，得到图片的指纹（64位哈希）
 * 5. 计算汉明距离
 * @author bear
 * @date 2021年04月24日 19:44
 */
public class DHashCalculate {

    private GrayAndResize grayAndResize;

    public DHashCalculate() {
        grayAndResize = new GrayAndResize();
    }

    /**
     * 计算得到图片指纹（哈希）
     * @param src
     * @return 由0和1构成的指纹字符串
     */
    public String dHash(BufferedImage src) {
        int width = 9;
        int height = 8;
        // 缩放图片尺寸
        BufferedImage image = grayAndResize.resize(src, height, width);
        // 灰度值
        int[] grays = new int[width * height];
        int index = 0;
        // 计算每个位置的灰度值
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // 获取图片（j, i）位置像素
                int pixel = image.getRGB(j, i);
                // 获取灰度值
                int gray = grayAndResize.gray(pixel);
                grays[index++] = gray;
            }
        }
        // 图片指纹（哈希）
        StringBuilder hashValue = new StringBuilder();
        // 计算哈希
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                if (grays[9 * j + i] >= grays[9 * j + i + 1]) {
                    // 大于等于后一个灰度值，记为1
                    hashValue.append("1");
                } else {
                    // 小于后一个灰度值，记为0
                    hashValue.append("0");
                }
            }
        }
        return hashValue.toString();
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
            if (c1[i] != c2[i]) diffCount++;
        }
        return diffCount;
    }

}
