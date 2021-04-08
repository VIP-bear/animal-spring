package com.bear.animal.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 评论表
 * @author bear
 * @date 2021年04月07日 20:29
 */
@Entity
@Data
@Table(name = "al_comment")
public class CommentEntity {

    // 主键，评论id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long comment_id;

    // 外键，图片id
    @ManyToOne(targetEntity = ImageEntity.class)
    @JoinColumn(name = "image_id", referencedColumnName = "image_id", nullable = false)
    private ImageEntity image;

    // 外键，用户id
    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "varchar(255) comment '评论内容'")
    private String comment_content;

    @Column(nullable = false, columnDefinition = "datetime(6) comment '评论时间'")
    private Timestamp comment_time;
}
