package com.bear.animal.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户行为表
 * @author bear
 * @date 2021年04月08日 9:22
 */
@Entity
@Data
@Table(name = "al_behavior")
public class BehaviorEntity {

    // 主键，行为id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long behavior_id;

    // 外键，用户id
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    // 外键，图片id
    @ManyToOne(targetEntity = ImageEntity.class)
    @JoinColumn(name = "image_id", referencedColumnName = "image_id", nullable = false)
    private ImageEntity image;

    @Column(columnDefinition = "int(2) default 0 comment '用户行为分数'")
    private Integer user_behavior;
}
