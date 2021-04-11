package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.dao.entity.FavoritesEntity;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.FavoritesRepository;
import com.bear.animal.enums.ResultCode;
import com.bear.animal.service.IFavoritesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图片收藏服务层实现
 * @author bear
 * @date 2021年04月10日 12:42
 */
@Slf4j
@Service
public class FavoritesServiceImpl implements IFavoritesService {

    @Autowired
    private FavoritesRepository favoritesRepository;

    /**
     * 更新图片收藏状态
     *
     * @param imageId
     * @param userId
     * @param state
     * @return
     */
    @Transactional
    @Override
    public Result updateImageFavoritesState(Long imageId, Long userId, boolean state) {
        if (state == true) {
            // 收藏图片
            FavoritesEntity favoritesEntity = new FavoritesEntity();
            UserEntity userEntity = new UserEntity();
            userEntity.setUser_id(userId);
            favoritesEntity.setImage_id(imageId);
            favoritesEntity.setUser(userEntity);
            FavoritesEntity saveRes = favoritesRepository.save(favoritesEntity);
            if (null == saveRes) {
                // 收藏失败
                return Result.failure(ResultCode.IMAGE_FAVORITES_FAIL);
            }
            return Result.success();
        }
        // 取消收藏
        int effectRow = favoritesRepository.deleteFavoritesByImageIdAndUserId(imageId, userId);
        if (effectRow == 0) {
            // 操作失败
            return Result.failure(ResultCode.FAIL);
        }
        return Result.success();
    }
}
