package com.bear.animal.util;

import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片识别
 * @author bear
 * @date 2021年05月09日 15:36
 */
@Component
public class PredictImageProvider {

    public static Map<String, String> map;

    static {
        map = new HashMap<>();
        map.put("cat", "猫");
        map.put("dog", "狗");
        map.put("horse", "马");
        map.put("cow", "牛");
        map.put("sheep", "羊");
        map.put("elephant", "大象");
        map.put("spider", "蜘蛛");
        map.put("butterfly", "蝴蝶");
        map.put("chicken", "鸡");
        map.put("squirrel", "松鼠");
    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 图片识别
     * 识别图片中的对象
     * @param imageUrl 图片地址
     * @return
     */
    public List<String> predictImage(String imageUrl) {
        List<String> tags = new ArrayList<>();
        String pythonServerUrl = "http://127.0.0.1:2021/predict?image_url=" + imageUrl;
        String result = restTemplate.getForObject(pythonServerUrl, String.class);
        if (result.equals("")) {
            // 没有识别出对象
            return tags;
        }
        String[] objects = result.split("#");
        for (String object : objects) {
            if (null != object && !("").equals(object)) {
                String name = object.split(" ")[0].substring(2);
                System.out.println(name);
                tags.add(map.get(name));
            }
        }
        return tags;
    }

}
