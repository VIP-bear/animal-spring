package com.bear.animal.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * 上传图片到阿里云oss
 */

@Component
public class AliyunProvider {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.folder}")
    private String folder;

    @Value("${aliyun.oss.public-key}")
    private String accessKeyId;

    @Value("${aliyun.oss.private-key}")
    private String accessKeySecret;

    @Value("${aliyun.oss.policy.expires}")
    private Integer expires;

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
            url = ossClient.generatePresignedUrl(bucketName, folder+subDirectory+fileName,
                    new Date(new Date().getTime() + expires));
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

    /**
     * base64转MultipartFile
     * @param base64
     * @return
     */
    public MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new BASE64DecodeMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
