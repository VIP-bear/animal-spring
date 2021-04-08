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

    // 外键，图片id
    @ManyToOne(targetEntity = ImageEntity.class)
    @JoinColumn(name = "image_id", referencedColumnName = "image_id", nullable = false)
    private ImageEntity image;

}
