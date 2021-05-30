package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.res.RecommendUserResult;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.*;
import com.bear.animal.service.IRecommendService;
import com.bear.animal.util.PredictImageProvider;
import com.bear.animal.util.filter.RecommendImageProvider;
import com.bear.animal.util.similarity.PHashCalculate;
import lombok.extern.slf4j.Slf4j;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 推荐服务实现
 *
 * 当用户为新用户（刚注册没多久：根据用户注册时间长短、用户访问图片量等信息来衡量）时，使用图片相似度比较算法；
 * 当用户不为新用户时，使用基于用户的协同过滤推荐算法。
 * 新用户的历史行为数据量较小，使用图片相似度比较算法来推荐能够快速的推荐图片给用户，而不为新用户使用基于用户
 * 的协同过滤推荐算法能够发掘目标用户的隐藏兴趣。
 *
 *
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
    private TagRepository tagRepository;

    @Autowired
    private AttentionRepository attentionRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PredictImageProvider predictImageProvider;

    // 用户数量
    private static final int n = 10;

    // 用户行为数
    private static final int m = 10;

    // 推荐图片数量
    private static final int recommendNum = 18;

    // 设置redis缓存中推荐图片过期时间
    private final long validTime = 5;

    // 用户行为数量阈值
    private final int behaviorThreshold = 2;

    /**
     * 推荐图片给用户
     *
     * @param userId
     * @return
     */
    @Override
    public Result getRecommendImage(Long userId) {
        // 结果集
        List<ImageEntity> recommendImageList;
        // 从redis缓存中根据userId获取推荐图片
        recommendImageList = (List<ImageEntity>) redisTemplate.opsForValue().get(String.valueOf(userId));
        if (recommendImageList != null) {
            return Result.success(recommendImageList);
        }
        // 获取用户行为记录数量，根据用户行为数量选择不同的算法
        int recordCount = behaviorRepository.findRecordCountByUserId(userId);
        if (recordCount == 0) {
            // 随机推荐图片
            recommendImageList = imageRepository.findImageByViewCount(0,18);
            log.info("随机推荐图片: {}", recommendImageList);
        } else if (recordCount < behaviorThreshold) {
            // 使用图片识别
            recommendImageList = predictImage(userId);
            log.info("使用图片识别推荐图片: {}", recommendImageList);
        } else if (recordCount < 2 * behaviorThreshold) {
            // 使用图片相似度比较算法
            recommendImageList = imageSimilarity(userId);
            log.info("使用图片相似度比较算法: {}", recommendImageList);
        } else {
            // 使用基于用户的协同过滤推荐
            recommendImageList = CollaborativeFiltering(userId);
            log.info("使用基于用户的协同过滤推荐: {}", recommendImageList);
        }
        // 将推荐图片存入缓存中
        redisTemplate.opsForValue().set(String.valueOf(userId), recommendImageList, validTime, TimeUnit.MINUTES);
        return Result.success(recommendImageList);
    }

    /**
     * 图片相似度比较推荐
     * @param userId 用户id
     * @return
     */
    private List<ImageEntity> imageSimilarity(Long userId) {
        // 结果集
        List<ImageEntity> recommendImageList = new ArrayList<>();
        Set<Long> recommendImageIdList = new HashSet<>();
        // 获取用户感兴趣的图片
        List<Long> userImageIdList = behaviorRepository.findByUserId(userId, 50);
        // 感知哈希算法
        PHashCalculate pHashCalculate = new PHashCalculate();
        // 从redis缓冲中获取图片指纹（哈希）
        // key: 图片id，value: 哈希
        HashMap<String, String> imageHashMap = (HashMap<String, String>) redisTemplate.opsForValue().get("image_hash");
        boolean completed = false;
        for (Long imageId : userImageIdList) {
            String imageHash = imageHashMap.get(String.valueOf(imageId));
            // 标签
            Set<String> tags = new HashSet<>();
            String[] tag = imageRepository.findTagByImageId(imageId).split("#");
            for (String t : tag) {
                if (null != t && !t.equals("")) tags.add(t);
            }
            // 根据标签查找图片
            List<Long> temp = tagRepository.findByTagList(new ArrayList<>(tags), 0, 1000);
            // 其他图片
            Set<Long> otherImageIdList = new HashSet<>(temp);
            otherImageIdList.removeAll(userImageIdList);
            for (Long otherImageId : otherImageIdList) {
                // 去重
                if (recommendImageIdList.contains(otherImageId)) continue;
                // 获取图片哈希
                String otherImageHash = imageHashMap.get(String.valueOf(otherImageId));
                // 比较图片相似度
                int distance = pHashCalculate.distance(imageHash.toCharArray(), otherImageHash.toCharArray());
                if (distance > 36) {
                    recommendImageIdList.add(otherImageId);
                }
                if (recommendImageIdList.size() == 18) {
                    completed = true;
                    break;
                }
            }
            if (completed) {
                break;
            }
        }
        recommendImageList = imageRepository.findAllById(recommendImageIdList);
        return recommendImageList;
    }

    /**
     * 基于用户的协同过滤推荐
     * @param userId 用户id
     * @return
     */
    private List<ImageEntity> CollaborativeFiltering(Long userId) {
        // 结果集
        List<ImageEntity> recommendImageList;
        // 随机获取n个用户
        List<UserEntity> userList = userRepository.getUserByRandom(n);
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
        return recommendImageList;
    }

    /**
     * 图片识别
     * 识别出图片中的对象，然后根据对象的标签找到图片进行推荐
     * @param userId
     * @return
     */
    private List<ImageEntity> predictImage(Long userId) {
        // 结果集
        List<ImageEntity> recommendImageList = new ArrayList<>();
        // 获取用户历史行为图片
        List<Long> imageIdList = behaviorRepository.findByUserIdOrderTime(userId, 1);
        // 图片地址
        List<String> imageUrlList = new ArrayList<>();
        for (Long imageId : imageIdList) {
            imageUrlList.add(imageRepository.findImageUrlByImageId(imageId));
        }
        // 图片中的对象
        List<String> imageTags = new ArrayList<>();
        for (String imageUrl : imageUrlList) {
            // 图片识别
            List<String> list = predictImageProvider.predictImage(imageUrl);
            if (list.size() > 0) {
                imageTags.addAll(list);
            }
        }
        // 根据标签查找图片
        List<Long> recommendImageIdList = tagRepository.findByTagList(imageTags, 0, 18);
        recommendImageList = imageRepository.findAllById(recommendImageIdList);
        return recommendImageList;
    }


    /**
     * 推荐与图片相关联的图片
     *
     * @param imageId
     * @param offset
     * @param size
     * @return
     */
    @Override
    public Result getRecommendRelatedImage(Long imageId, int offset, int size) {
        // 获取图片信息
        ImageEntity image = imageRepository.findById(imageId).get();
        // 获取图片标签
        String[] tags = image.getImage_tags().split("#");
        List<String> imageTag = new ArrayList<>();
        for (String tag : tags) {
            if (null == tag || tag.equals("")) continue;
            imageTag.add(tag);
        }
        // 根据标签获取图片
        List<Long> imageIdList = tagRepository.findByTagList(imageTag, offset, size);
        List<ImageEntity> imageList = new ArrayList<>();
        imageList = imageRepository.findAllById(imageIdList);
        return Result.success(imageList);
    }

    /**
     * 推荐用户
     *
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public Result getRecommendUser(Long userId, int size) {
        // 去重
        Set<Long> set = new HashSet<>();
        set.add(userId);
        // 获取用户关注列表
        List<Long> attentionUserIdList = attentionRepository.findByUserId(userId);
        set.addAll(attentionUserIdList);
        // 获取用户操作过的图片
        List<Long> imageIdList = behaviorRepository.findByUserId(userId, size * 10);
        // 获取图片信息
        List<ImageEntity> imageList = imageRepository.findAllById(imageIdList);
        List<UserEntity> recommendUserList = new ArrayList<>();
        for (ImageEntity imageEntity : imageList) {
            // 已经关注了
            if (set.contains(imageEntity.getUser().getUser_id())) continue;
            // 未关注，推荐
            recommendUserList.add(imageEntity.getUser());
            set.add(imageEntity.getUser().getUser_id());
            if (recommendUserList.size() >= size) break;
        }
        if (recommendUserList.size() != size) {
            // 推荐的用户数量不足
            recommendUserList.addAll(userRepository.getUserByRandom(size - recommendUserList.size()));
        }
        // 推荐结果
        List<RecommendUserResult> recommendRes = new ArrayList<>();
        // 获取推荐用户发布的图片
        for (UserEntity userEntity : recommendUserList) {
            List<ImageEntity> images = new ArrayList<>();
            images = imageRepository.findByUserId(userEntity.getUser_id(), 0, 3);
            RecommendUserResult message = new RecommendUserResult();
            message.setUser(userEntity);
            message.setImageList(images);
            message.setAttention(false);
            recommendRes.add(message);
        }
        return Result.success(recommendRes);
    }

    /**
     * 搜索图片
     *
     * @param searchName
     * @param offset
     * @param size
     * @return
     */
    @Override
    public Result searchImage(String searchName, int offset, int size) {
        // 根据标签模糊搜索图片
        String tag = "%" + searchName.trim() + "%";
        List<Long> imageIdList = tagRepository.findByTag(tag, offset, size);
        List<ImageEntity> imageList = imageRepository.findAllById(imageIdList);
        return Result.success(imageList);
    }
}
