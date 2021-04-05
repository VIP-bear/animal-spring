package com.bear.animal.controller.res;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 注册请求结果数据
 */
@Data
public class RegisterResult implements Serializable {

    private Long user_id;

    private String username;

    private String password;

    private String email;

    private Integer attention_count;

    private Integer follow_count;

    private String user_introduction;

    private String tags;

    private Timestamp create_time;

    private String session_id;
}
