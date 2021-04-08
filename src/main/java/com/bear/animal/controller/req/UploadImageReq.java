package com.bear.animal.controller.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

/**
 * 上传图片信息
 * @author bear
 * @date 2021年04月07日 18:49
 */
@Data
public class UploadImageReq {

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 图片
     */
    private String image;

    /**
     * 图片标题
     */
    private String image_title;

    /**
     * 图片描述
     */
    private String image_description;

    /**
     * 图片标签
     */
    private String image_tags;

}
