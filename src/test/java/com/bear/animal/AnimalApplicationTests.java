package com.bear.animal;

import com.bear.animal.controller.Result;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.repository.ImageRepository;
import com.bear.animal.service.IImageService;
import com.bear.animal.service.IRecommendService;
import com.bear.animal.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class AnimalApplicationTests {
    @Autowired
    private ImageServiceImpl imageService;

    @Test
    void contextLoads() {
    }


}
