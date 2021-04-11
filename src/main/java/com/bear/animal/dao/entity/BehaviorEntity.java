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

    @Column(nullable = false, columnDefinition = "int(20) comment '用户id'")
    private Long user_id;

    @Column(nullable = false, columnDefinition = "int(20) comment '图片id'")
    private Long image_id;

    @Column(columnDefinition = "int(2) default 0 comment '用户行为分数'")
    private Integer user_behavior_score;
}
