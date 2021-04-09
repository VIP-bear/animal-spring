package com.bear.animal.service.impl;

import com.bear.animal.controller.Result;
import com.bear.animal.controller.req.UploadImageReq;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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

    // 设置redis缓存中排行榜过期时间为1小时
    private final long validTime = 1;

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
        if (imageEntity.getImage_title() == null) {
            imageEntity.setImage_title("no title");
        }
        if (imageEntity.getImage_description() == null) {
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
        for (String tag : tags) {
            if (tag == null || tag.equals("")) continue;
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
        // 保存成功
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
            log.info("imageList: {}", imageList);
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
        // 将sessionId存入redis数据库中(key为ranking, value排行榜信息)
        redisTemplate.opsForValue().set(key, imageList, validTime, TimeUnit.HOURS);
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
        List<String> urls = imageRepository.findImageUrlByUserId(imageEntity.getUser().getUser_id());
        if (null == urls) {
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
        imageResult.setUrls(urls);
        imageResult.setTags(list);
        return Result.success(imageResult);
    }
}
