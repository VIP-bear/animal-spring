package com.bear.animal;

import com.bear.animal.util.similarity.PHashCalculate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

/**
 * @author bear
 * @date 2021年04月25日 15:15
 */
public class TestSimilarity {

    public static void main(String[] args) {
        String targetTag = "https://bearanimal.oss-cn-beijing.aliyuncs.com/image/c0ed4890-d35a-4b01-a85b-83c677e47db41617943618040.jpeg";
        String url1 = "https://bearanimal.oss-cn-beijing.aliyuncs.com/image/c817e51c-31af-4800-9f69-4c7a6e3607d41617943583739.jpeg";
        String url2 = "https://bearanimal.oss-cn-beijing.aliyuncs.com/image/ce184656-a54d-4114-a4e6-d425c5de410d1618109116124.png";
        PHashCalculate pHashCalculate = new PHashCalculate();
        String hash = pHashCalculate.pHash(getBufferedImage(targetTag));
        String hash1 = pHashCalculate.pHash(getBufferedImage(url1));
        String hash2 = pHashCalculate.pHash(getBufferedImage(url2));
        System.out.println("hash: " + hash);
        System.out.println("hash1: " + hash1);
        System.out.println("hash2: " + hash2);
        System.out.println("1:" + pHashCalculate.distance(hash.toCharArray(), hash1.toCharArray()));
        System.out.println("2:" + pHashCalculate.distance(hash.toCharArray(), hash2.toCharArray()));
    }

    public static BufferedImage getBufferedImage(String imageUrl) {
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
