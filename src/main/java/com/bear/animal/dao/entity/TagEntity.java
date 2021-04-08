package com.bear.animal.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 标签表
 * @author bear
 * @date 2021年04月07日 20:43
 */
@Entity
@Data
@Table(name = "al_tag")
public class TagEntity {

    // 主键，标签id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tag_id;

    // 外键，图片id
    @ManyToOne(targetEntity = ImageEntity.class)
    @JoinColumn(name = "image_id", referencedColumnName = "image_id", nullable = false)
    private ImageEntity image;

    @Column(nullable = false, columnDefinition = "varchar(30) comment '标签名'")
    private String tag_name;
}
