package com.bear.animal.controller.res;

import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import lombok.Data;

import java.util.List;

/**
 * 关注用户信息
 * @author bear
 * @date 2021年04月15日 18:36
 */
@Data
public class AttentionMessage {

    // 用户信息
    private UserEntity user;

    // 用户图片
    private List<ImageEntity> imageList;

}
