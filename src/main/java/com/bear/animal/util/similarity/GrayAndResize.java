package com.bear.animal.util.similarity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 简化色彩，转灰度图；改变图片尺寸
 * @author bear
 * @date 2021年04月24日 19:49
 */
public class GrayAndResize {

    /**
     * 简化色彩，转为灰度图(为了统一输入标准)
     * @param rgb
     * @return
     */
    public int gray(int rgb) {
        // 获取24-31位的信息(alpha通道)
        int a = rgb & 0xff000000;
        // 获取16-23位的信息(红色)
        int r = (rgb >> 16) & 0xff;
        // 获取8-15位的信息(绿色)
        int g = (rgb >> 8) & 0xff;
        // 获取0-7位的信息(蓝色)
        int b = rgb & 0xff;
        // 计算灰度值
        /**
         * rgb = (int)(r * 0.3 + g * 0.59 + b * 0.11); // 浮点方法
         * rgb = (r * 30 + g * 59 + b * 11) / 100; // 整数方法
         * rgb = (r + g + b) / 3; // 平均值方法
         * rgb = g; // 仅取绿色
         */
        rgb = (r * 76 + g * 151 + b * 28) >> 8; // 移位方法
        // 将灰度值重新送入各个颜色中
        return a | (rgb << 16) | (rgb << 8) | rgb;
    }

    /**
     * 改变图片尺寸
     * @param src
     * @param height
     * @param width
     * @return
     */
    public BufferedImage resize(BufferedImage src, int height, int width) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(src, 0, 0, width, height, null);
        return image;
    }

}
