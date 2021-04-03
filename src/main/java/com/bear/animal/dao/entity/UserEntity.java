package com.bear.animal.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 用户表
 */

@Entity
@Data
@Table(name = "al_user")
public class UserEntity {

    // 主键，用户id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    @Column(nullable = false, columnDefinition = "varchar(16) comment '用户名'")
    private String username;

    @Column(nullable = false, columnDefinition = "varchar(64) comment '密码'")
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(32) comment '邮箱'")
    private String email;

    @Column(columnDefinition = "int(11) default 0 comment '关注数'")
    private Integer attention_count;

    @Column(columnDefinition = "int(11) default 0 comment '粉丝数'")
    private Integer follow_count;

    @Column(columnDefinition = "varchar(255) default '' comment '用户介绍'")
    private String user_introduction;

    @Column(columnDefinition = "varchar(255) default '' comment '用户使用过的标签集合，以#隔开'")
    private String tags;

    @Column(nullable = false, columnDefinition = "datetime(6) comment '账号创建时间'")
    private Timestamp create_time;
}
