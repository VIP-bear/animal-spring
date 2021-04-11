package com.bear.animal.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 收藏表
 * @author bear
 * @date 2021年04月08日 9:19
 */
@Entity
@Data
@Table(name = "al_favorites")
public class FavoritesEntity {

    // 主键，收藏id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long favorites_id;

    // 外键，用户id
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "int(11) comment '图片id'")
    private Long image_id;

}
