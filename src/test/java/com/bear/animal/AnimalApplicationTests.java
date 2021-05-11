package com.bear.animal;

import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.repository.ImageRepository;
import com.bear.animal.util.PredictImageProvider;
import com.bear.animal.util.similarity.PHashCalculate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.jupiter.api.Test;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;

@Slf4j
@SpringBootTest
class AnimalApplicationTests {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PredictImageProvider predictImageProvider;

    @Test
    void contextLoads() {
    }

    @Test
    void testSimilarity() {
        // 查找全部图片
        List<ImageEntity> allImage = imageRepository.findAll();
        PHashCalculate pHashCalculate = new PHashCalculate();
        Map<String, String> map = new HashMap<>();
        for (ImageEntity imageEntity : allImage) {
            String hash = pHashCalculate.pHash(getBufferedImage(imageEntity.getImage_url()));
            map.put(String.valueOf(imageEntity.getImage_id()), hash);
            System.out.println("image_id: " + imageEntity.getImage_id() + ", hash: " + hash);
        }
        redisTemplate.opsForValue().set("image_hash", map);

        Map<String, String> result = (Map<String, String>) redisTemplate.opsForValue().get("image_hash");
        for (String id : result.keySet()) {
            System.out.println("image_id: " + id + ", hash: " + result.get(id));
        }
    }

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

    @Test
    public void testPython() {
        String PATH = "E:\\python\\imagerecommend\\yolo3-pytorch\\predict_test.py";
        String line = "python " + PATH;
        CommandLine cmdLine = CommandLine.parse(line);
        ByteArrayOutputStream outputStream;
        try {
            outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(streamHandler);
            int exitCode = executor.execute(cmdLine);
            log.info("调用python脚本执行结果: {}", exitCode == 0 ? "成功" : "失败");
            log.info(outputStream.toString().trim());
        } catch (IOException e) {
            log.error("调用python脚本出错", e);
        }
    }

    @Test
    public void testJython() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("E:\\python\\imagerecommend\\yolo3-pytorch\\predict_test.py");

    }

    @Test
    public void testRuntime() {
        Process proc;
        try {
            proc = Runtime.getRuntime().exec("python E:\\python\\imagerecommend\\yolo3-pytorch\\predict_test.py");
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error1!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("error2!");
        }
    }

    @Test
    public void testPredict() {
        String imageUrl = "https://onlinejpgtools.com/images/examples-onlinejpgtools/sunflower.jpg";
        List<String> objects = predictImageProvider.predictImage(imageUrl);
        for (String object : objects) {
            System.out.println(object);
        }
    }

}
