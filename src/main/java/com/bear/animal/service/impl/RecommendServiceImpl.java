package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.dao.entity.BehaviorEntity;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.BehaviorRepository;
import com.bear.animal.dao.repository.ImageRepository;
import com.bear.animal.dao.repository.UserRepository;
import com.bear.animal.service.IRecommendService;
import com.bear.animal.util.RecommendImageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐服务实现
 * @author bear
 * @date 2021年04月11日 12:19
 */
@Service
@Slf4j
public class RecommendServiceImpl implements IRecommendService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BehaviorRepository behaviorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    // 用户数量
    private static final int n = 10;

    // 用户行为数
    private static final int m = 10;

    // 推荐图片数量
    private static final int recommendNum = 18;

    // 设置redis缓存中推荐图片的key
    private final String key = "recommend";

    // 设置redis缓存中推荐图片过期时间为10分钟
    private final long validTime = 10;

    /**
     * 推荐图片给用户
     *
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public Result getRecommendImage(Long userId) {
        List<ImageEntity> recommendImageList;
        // 从redis缓存中根据userId获取推荐图片
//        recommendImageList = (List<ImageEntity>) redisTemplate.boundHashOps(key).get(userId);

        // 随机获取n个用户
        List<UserEntity> userList = userRepository.getUserByRandom(n);
        log.info("获取的n个用户: {}", userList);
        // 存储n个用户感兴趣的图片(历史行为)
        Map<Long, List<Long>> userAndImageMap = new HashMap<>(userList.size());
        // 获取用户感兴趣的图片(历史行为)
        List<Long> imageIdList;
        boolean isTargetUser = false;   // 是否是目标用户
        for (UserEntity user : userList) {
            if (user.getUser_id().equals(userId)) isTargetUser = true;
            // 根据用户id获取用户前m个行为
            imageIdList = behaviorRepository.findByUserId(user.getUser_id(), m);
            log.info("userId: {}, imageIdList: {}", user.getUser_id(), imageIdList);
            userAndImageMap.put(user.getUser_id(), imageIdList);
        }
        // 加入目标用户
        if (!isTargetUser) {
            imageIdList = behaviorRepository.findByUserId(userId, m);
            userAndImageMap.put(userId, imageIdList);
        }
        // 推荐图片
        RecommendImageProvider recommendImageProvider = new RecommendImageProvider();
        List<Long> recommendImageIdList = recommendImageProvider.recommendImage(userId, recommendNum, userAndImageMap);
        log.info("推荐图片id集: {}", recommendImageIdList);
        // 查询推荐的图片
        recommendImageList = imageRepository.findAllById(recommendImageIdList);
        // 将推荐图片存入缓存中
//        redisTemplate.boundHashOps(key).put(userId, recommendImageList);
        return Result.success(recommendImageList);
    }

}
