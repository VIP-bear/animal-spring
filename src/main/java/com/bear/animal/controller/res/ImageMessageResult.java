package com.bear.animal.controller.res;

import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import lombok.Data;

import java.util.List;

/**
 * 图片详细信息
 * @author bear
 * @date 2021年04月09日 17:57
 */
@Data
public class ImageMessageResult {

    // 图片信息
    private ImageEntity image;

    // 图片标签
    private List<String> tags;

    // 用户信息
    private UserEntity user;

    // 是否关注
    private boolean isAttention;

    // 是否收藏
    private boolean isFavorites;

    // 用户图片集
    private List<String> urls;

}
