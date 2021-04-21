package com.bear.animal.controller.res;

import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import lombok.Data;

import java.util.List;

/**
 * 推荐用户信息
 * @author bear
 * @date 2021年04月21日 19:54
 */
@Data
public class RecommendUserResult {

    private UserEntity user;

    private List<ImageEntity> imageList;

    private boolean attention;

}
