package com.bear.animal.util.filter;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 推荐图片
 * 基于用户的协同过滤推荐算法
 */
@Slf4j
public class RecommendImageProvider {

    // 用户-图片表
    private Map<Long, List<Long>> userAndImageMap;
    // 图片-用户表
    private Map<Long, List<Long>> imageAndUserMap;
    // 目标用户与其它用户之间喜欢图片得分表
    private Map<Long, Integer> userImageScore;
    // 目标用户与其它用户之间相似度表
    private Map<Long, Double> userSimilar;
    // 相似用户集
    private List<Long> similarUserList;
    // 推荐图片集合
    private List<Long> recommendImageList;


    // 推荐图片
    public List<Long> recommendImage(Long userId, int size, Map<Long, List<Long>> userAndImageMap) {
        log.info("executed recommendImage");
        this.userAndImageMap = userAndImageMap;
        transferForm();
        getUserImageScore(userId);
        calUserSimilar(userId);
        getSimilarUserList();
        getRecommendImageList(userId, size);
        return recommendImageList;
    }

    // 获取推荐图片集合
    private void getRecommendImageList(Long targetUserId, int size) {
        log.info("executed getRecommendImageList");
        recommendImageList = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        // 相似用户图片去重
        for (Long userId : similarUserList) {
            set.addAll(userAndImageMap.get(userId));
        }
        // 目标用户和相似用户图片去重
        List<Long> imageList = userAndImageMap.get(targetUserId);
        if (imageList == null) return;
        for (Long imageId : imageList) {
            if (set.contains(imageId)) {
                set.remove(imageId);
            }
        }
        // 计算目标用户对候选图片感兴趣程度
        Map<Long, Double> targetUserXp = new HashMap<>();
        for (Long imageId : set) {
            List<Long> userIdList = imageAndUserMap.get(imageId);
            Double xp = 0.0;
            System.out.println("size: " + userIdList.size());
            for (Long userId : userIdList) {
                xp += userSimilar.get(userId) == null ? 0 : userSimilar.get(userId);
            }
            targetUserXp.put(imageId, xp);
        }
        // 对感兴趣图片排序
        List<Map.Entry<Long, Double>> list = new ArrayList<>(targetUserXp.entrySet()); //转换为list
        list.sort((o1, o2) -> {
            return o2.getValue().compareTo(o1.getValue());
        });
        // 得到前size个推荐图片
        int k = 0;
        for (Map.Entry<Long, Double> map : list) {
            if (k >= size) break;
            recommendImageList.add(map.getKey());
            k++;
        }
    }

    // 寻找前5个与目标用户相似度较高的用户
    private void getSimilarUserList() {
        log.info("executed getSimilarUserList");
        similarUserList = new ArrayList<>();
        List<Map.Entry<Long, Double>> list = new ArrayList<>(userSimilar.entrySet()); //转换为list
        list.sort((o1, o2) -> {
            return o2.getValue().compareTo(o1.getValue());
        });
        int k = 0;
        for (Map.Entry<Long, Double> map : list) {
            if (k == 5) break;
            similarUserList.add(map.getKey());
            k++;
        }
    }

    // 计算目标用户与其他用户相似度
    private void calUserSimilar(Long targetUserId) {
        log.info("executed calUserSimilar");
        userSimilar = new HashMap<>();
        for (Long userId : userImageScore.keySet()) {
            Set<Long> set = new HashSet<>();    // 存储图片id，用于去重
            if (userId != targetUserId) {
                set.addAll(userAndImageMap.get(targetUserId));
                set.addAll(userAndImageMap.get(userId));
                userSimilar.put(userId, 1.0 * userImageScore.get(userId) / set.size());
            }
        }
    }

    // 获取用户图片得分表
    private void getUserImageScore(Long targetUserId) {
        log.info("executed getUserImageScore");
        userImageScore = new HashMap<>();
        for (Long imageId : imageAndUserMap.keySet()) {
            List<Long> userIdList = imageAndUserMap.get(imageId);
            boolean isExist = false;
            for (Long userId : userIdList) {
                if (userId.equals(targetUserId)) {
                    isExist = true;
                    break;
                }
            }
            // 存在目标用户
            if (isExist) {
                for (Long userId : userIdList) {
                    if (!userId.equals(targetUserId)) {
                        userImageScore.put(userId, userImageScore.getOrDefault(userId, 0) + 1);
                    }
                }
            }
        }
    }

    // 用户-图片表转图片-用户表
    private void transferForm() {
        log.info("executed transferForm");
        imageAndUserMap = new HashMap<>();
        for (Long userId : userAndImageMap.keySet()) {
            List<Long> imageIdList = userAndImageMap.get(userId);
            for (Long imageId : imageIdList) {
                List<Long> userIdList;
                if (imageAndUserMap.containsKey(imageId)) {
                    userIdList = imageAndUserMap.get(imageId);
                } else {
                    userIdList = new ArrayList<>();
                }
                userIdList.add(userId);
                imageAndUserMap.put(imageId, userIdList);
            }
        }
    }

}
