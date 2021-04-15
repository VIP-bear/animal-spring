package com.bear.animal.controller.res;

import com.bear.animal.dao.entity.UserEntity;
import lombok.Data;

/**
 * 请求关注用户图片
 * @author bear
 * @date 2021年04月13日 12:35
 */
@Data
public class AttentionUserImageResult {

    private Long image_id;

    private UserEntity user;

    private String image_url;

    private String image_title;

}
