package com.bear.animal.controller.res;

import com.bear.animal.dao.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求获取榜单数据
 * @author bear
 * @date 2021年04月09日 12:00
 */
@Data
public class RankingImageResult implements Serializable {

    private Long image_id;

    private UserEntity user;

    private String image_url;

    private String image_title;

}
