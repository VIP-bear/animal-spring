package com.bear.animal.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 关注表
 * @author bear
 * @date 2021年04月07日 20:37
 */
@Entity
@Data
@Table(name = "al_attention")
public class AttentionEntity {

    // 主键，表id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attention_id;

    // 外键，用户id
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "int(20) comment '关注用户id'")
    private Long attention_user_id;
}
