package com.bear.animal.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * 上传图片到阿里云oss
 */

@Component
public class AliyunProvider {

    @Value("${aliyun.file.endpoint}")
    private static String endpoint;

    @Value("${aliyun.file.bucket-name}")
    private static String bucketName;

    @Value("${aliyun.file.folder}")
    private static String folder;

    @Value("${aliyun.file.public-key}")
    private static String accessKeyId;

    @Value("${aliyun.file.private-key}")
    private static String accessKeySecret;

    @Value("${aliyun.file.expires}")
    private static Integer expires;

    /**
     * 上传图片到服务器，返回图片url
     * @param uploadFile
     * @return
     */
    public String upload(MultipartFile uploadFile, String subDirectory) {
        URL url;
        String imageUrl = null;
        // 图片名
        String fileName = UUID.randomUUID().toString() + uploadFile.getOriginalFilename();
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 上传
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(uploadFile.getInputStream().available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(uploadFile.getContentType());
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            ossClient.putObject(bucketName, folder+subDirectory+fileName, uploadFile.getInputStream(), objectMetadata);
            // 获取图片url,第二个参数图片地址，第三个参数图片地址有效期
            url = ossClient.generatePresignedUrl(bucketName, folder+subDirectory+fileName, new Date(new Date().getTime() + expires));
            if (url != null){
                imageUrl = url.toString();
            }else {
                return null;
            }
            // 关闭client
            ossClient.shutdown();

        }catch (IOException e){
            return null;
        }
        return imageUrl;
    }
}
