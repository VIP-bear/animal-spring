package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.UploadImageReq;
import com.bear.animal.controller.res.AttentionUserImageResult;
import com.bear.animal.controller.res.ImageMessageResult;
import com.bear.animal.dao.entity.ImageEntity;
import com.bear.animal.dao.entity.TagEntity;
import com.bear.animal.dao.entity.UserEntity;
import com.bear.animal.dao.repository.*;
import com.bear.animal.enums.ResultCode;
import com.bear.animal.except.BusinessException;
import com.bear.animal.service.IImageService;
import com.bear.animal.util.AliyunProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 图片服务实现
 * @author bear
 * @date 2021年04月08日 9:45
 */
@Slf4j
@Service
public class ImageServiceImpl implements IImageService {

    @Autowired
    private AliyunProvider aliyunProvider;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttentionRepository attentionRepository;

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    RedisTemplate redisTemplate;

    // 设置redis缓存中排行榜的key
    private final String key = "ranking";

    // 设置redis缓存中排行榜过期时间为10分钟
    private final long validTime = 10;

    /**
     * 上传图片
     *
     * @param uploadImageMessage
     * @return
     */
    @Transactional
    @Override
    public Result uploadImage(UploadImageReq uploadImageMessage) {
        // base64转MultipartFile
        MultipartFile file = aliyunProvider.base64ToMultipart(uploadImageMessage.getImage());
        if (file == null) {
            // 图片转码失败
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 上传图片到阿里云oss
        String image_url = aliyunProvider.upload(file, "");
        if (image_url == null) {
            // 图片上传失败
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 保存图片信息到数据库中
        ImageEntity imageEntity = new ImageEntity();
        BeanUtils.copyProperties(uploadImageMessage, imageEntity);
        if (imageEntity.getImage_title().equals("")) {
            imageEntity.setImage_title("no title");
        }
        if (imageEntity.getImage_description().equals("")) {
            imageEntity.setImage_description("");
        }
        imageEntity.setImage_url(image_url);
        imageEntity.setImage_upload_time(new Timestamp(System.currentTimeMillis()));
        imageEntity.setImage_favorites_count(0);
        imageEntity.setImage_like_count(0);
        imageEntity.setImage_view_count(0);
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_id(uploadImageMessage.getUser_id());
        imageEntity.setUser(userEntity);
        ImageEntity res = imageRepository.save(imageEntity);
        if (null == res) {
            // 保存失败，系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 保存标签到标签库中
        String[] tags = uploadImageMessage.getImage_tags().split("#");
        List<TagEntity> list = new ArrayList<>();
        Set<String> set = new HashSet<>();  // 标签去重
        for (String tag : tags) {
            if (tag == null || tag.equals("") || set.contains(tag)) continue;
            set.add(tag);
            TagEntity tagEntity = new TagEntity();
            tagEntity.setImage(res);
            tagEntity.setTag_name(tag);
            list.add(tagEntity);
        }
        List<TagEntity> tagRes = tagRepository.saveAll(list);
        if (tagRes == null) {
            // 保存失败，系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 保存标签到用户信息中
        UserEntity user = userRepository.findById(uploadImageMessage.getUser_id()).get();
        if (user.getTags() != null) {
            String[] userTags = user.getTags().split("#");
            for (String tag : userTags) {
                if (tag == null || tag.equals("")) continue;
                set.add(tag);
            }
        }
        StringBuilder tagList = new StringBuilder();
        for (String tag : set) {
            tagList.append(tag + "#");
        }
        tagList.deleteCharAt(tagList.length()-1);
        // 更新用户标签信息
        int effectRow = userRepository.updateTagsByUserId(uploadImageMessage.getUser_id(), tagList.toString());
        if (effectRow == 0) {
            // 系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        return Result.success();
    }

    /**
     * 获取每日排行榜图片
     *
     * @return
     */
    @Transactional
    @Override
    public Result getRanking() {

        //从redis缓存中获取排行榜
        List<ImageEntity> imageList = (List<ImageEntity>) redisTemplate.opsForValue().get(key);
        if (null != imageList && imageList.size() != 0) {
            // 缓存命中
            return Result.success(imageList);
        }

        // 获取今天0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Timestamp today = new Timestamp(calendar.getTime().getTime());
        // 获取排行榜图片
        imageList = imageRepository.getRankingByUploadTime(today);
        if (null == imageList) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 将排行榜存入缓存中
        // 将排行榜存入redis数据库中(key为ranking, value排行榜信息)
        redisTemplate.opsForValue().set(key, imageList, validTime, TimeUnit.MINUTES);
        return Result.success(imageList);
    }

    /**
     * 根据图片id获取图片详细信息
     *
     * @param imageId
     * @return
     */
    @Transactional
    @Override
    public Result getImageMessage(Long imageId, Long userId) {
        // 根据图片id获取图片信息
        ImageEntity imageEntity = imageRepository.findImageByImageId(imageId);
        if (null == imageEntity) {
            // 系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 根据用户id获取用户信息
        Optional<UserEntity> userRes = userRepository.findById(imageEntity.getUser().getUser_id());
        if (null == userRes) {
            // 系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        UserEntity userEntity = userRes.get();
        // 判断用户是否关注了用户
        boolean isAttention = false;
        int attCount = attentionRepository.findByAttentionUserId(userId, userEntity.getUser_id());
        if (attCount == 1) {
            isAttention = true;
        }
        // 判断用户是否收藏了图片
        boolean isFavorites = false;
        int favCount = favoritesRepository.findByUserIdAndImageId(userId, imageId);
        if (favCount == 1) {
            isFavorites = true;
        }
        // 根据用户id获取用户图片集
        List<ImageEntity> imageList = imageRepository.findImageUrlByUserId(imageEntity.getUser().getUser_id(), 6);
        if (null == imageList) {
            // 系统异常
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        // 结果
        String[] tags = imageEntity.getImage_tags().split("#");
        List<String> list = new ArrayList<>();
        for (String tag : tags) {
            if (tag == null || tag.equals("")) continue;
            list.add(tag);
        }
        ImageMessageResult imageResult = new ImageMessageResult();
        imageResult.setImage(imageEntity);
        imageResult.setUser(userEntity);
        imageResult.setAttention(isAttention);
        imageResult.setFavorites(isFavorites);
        imageResult.setImageList(imageList);
        imageResult.setTags(list);
        return Result.success(imageResult);
    }

    /**
     * 根据图片id更新图片浏览数
     *
     * @param imageId
     */
    @Transactional
    @Override
    public void updateImageViewCount(Long imageId) {
        imageRepository.updateImageViewCountByImageId(imageId);
    }

    /**
     * 根据图片id更新图片收藏数
     *
     * @param imageId
     * @param count
     */
    @Transactional
    @Override
    public void updateImageFavoritesCount(Long imageId, int count) {
        imageRepository.updateImageFavoritesCountByImageId(imageId, count);
    }

    /**
     * 根据用户id获取关注用户的图片
     *
     * @param userId
     * @return
     */
    @Override
    public Result getAttentionUserImage(Long userId) {
        // 获取关注用户id集合
        List<Long> attentionUserIdList = attentionRepository.findByUserId(userId);
        if (attentionUserIdList == null) {
            return Result.success();
        }
        List<AttentionUserImageResult> res = new ArrayList<>();
        for (Long id : attentionUserIdList) {
            // 根据用户id获取图片
            List<ImageEntity> imageList = imageRepository.findByUserId(id, 0, 10);
            if (imageList != null) {
                for (ImageEntity image : imageList) {
                    AttentionUserImageResult attentionUserImageResult = new AttentionUserImageResult();
                    BeanUtils.copyProperties(image, attentionUserImageResult);
                    res.add(attentionUserImageResult);
                }
            }
        }
        return Result.success(res);
    }

    /**
     * 获取用户收藏图片列表
     *
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Override
    public Result getFavoritesImageList(Long userId, Integer offset, int size) {
        // 获取图片id集合
        List<Long> imageIdList = favoritesRepository.findByUserId(userId, offset, size);
        List<ImageEntity> imageList = imageRepository.findAllById(imageIdList);
        return Result.success(imageList);
    }
}
