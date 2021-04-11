package com.bear.animal;

import com.bear.animal.controller.Result;
import com.bear.animal.service.IRecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class AnimalApplicationTests {
    @Autowired
    private IRecommendService recommendService;

    @Test
    void contextLoads() {
    }

    @Test
    void recommend() {
        Result res = recommendService.getRecommendImage(64l);
    }

}
