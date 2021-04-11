package com.bear.animal.service;

import com.bear.animal.controller.Result;

/**
 * 图片收藏服务层接口
 * @author bear
 * @date 2021年04月10日 12:39
 */
public interface IFavoritesService {

    /**
     * 更新图片收藏状态
     * @param imageId
     * @param userId
     * @param state
     * @return
     */
    Result updateImageFavoritesState(Long imageId, Long userId, boolean state);

}
