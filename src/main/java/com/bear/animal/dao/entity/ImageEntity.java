package com.bear.animal.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 图片表
 * @author bear
 * @date 2021年04月07日 18:52
 */
@Entity
@Data
@Table(name = "al_image")
public class ImageEntity {

    // 主键，图片id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long image_id;

    // 外键，用户id
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "varchar(255) comment '图片链接'")
    private String image_url;

    @Column(nullable = false, columnDefinition = "varchar(30) comment '图片标题'")
    private String image_title;

    @Column(nullable = false, columnDefinition = "varchar(255) comment '图片描述'")
    private String image_description;

    @Column(nullable = false, columnDefinition = "varchar(255) comment '图片标签，使用#隔开'")
    private String image_tags;

    @Column(nullable = false, columnDefinition = "datetime(6) comment '图片发布时间'")
    private Timestamp image_upload_time;

    @Column(columnDefinition = "int(11) default 0 comment '浏览数'")
    private Integer image_view_count;

    @Column(columnDefinition = "int(11) default 0 comment '点赞数'")
    private Integer image_like_count;

    @Column(columnDefinition = "int(11) default 0 comment '收藏数'")
    private Integer image_favorites_count;

}
