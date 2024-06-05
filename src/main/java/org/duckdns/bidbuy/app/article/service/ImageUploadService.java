package org.duckdns.bidbuy.app.article.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ImageUploadService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${ncp.objectstorage.bucket}")
    private String bucket;

    public List<Map<String, String>> uploadImages(MultipartFile[] multipartFiles) throws IOException {
        List<Map<String, String>> imageUrlMaps = new ArrayList<>();
        for (int i = 0; i < multipartFiles.length; i++) {
            MultipartFile multipartFile = multipartFiles[i];
            File originalFile = convertMultiPartToFile(multipartFile);
            String originalFileName = "article/" + generateFileName(multipartFile.getOriginalFilename());

            Map<String, String> imageUrlMap = new HashMap<>();
            imageUrlMap.put("original", originalFileName.substring(originalFileName.lastIndexOf("/") + 1));

            // 첫 번째 이미지인 경우에만 썸네일 생성
            if (i == 0) {
                File thumbnailFile = new File("s_" + originalFile.getName());
                Thumbnails.of(originalFile).size(600, 600).toFile(thumbnailFile);
                String thumbnailFileName = "article/s_" + originalFileName.substring(originalFileName.lastIndexOf("/") + 1);
                amazonS3.putObject(new PutObjectRequest(bucket, thumbnailFileName, thumbnailFile).withCannedAcl(CannedAccessControlList.PublicRead));
                imageUrlMap.put("thumbnail", thumbnailFileName.substring(thumbnailFileName.lastIndexOf("/") + 1));
                thumbnailFile.delete();
            }

            amazonS3.putObject(new PutObjectRequest(bucket, originalFileName, originalFile).withCannedAcl(CannedAccessControlList.PublicRead));
            originalFile.delete();

            imageUrlMaps.add(imageUrlMap);
        }
        return imageUrlMaps;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public void deleteImage(String imageUrl) {
        // imageUrl에서 파일명 추출
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        String objectKey = "article/" + fileName;  // 'article' 폴더 내의 파일 삭제

        // Object Storage에서 파일 삭제
        amazonS3.deleteObject(bucket, objectKey);
    }


    private String generateFileName(String originalName) {
        return "uuid_" + UUID.randomUUID().toString();
    }
}