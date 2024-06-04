package org.duckdns.bidbuy.app.article.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${ncp.objectstorage.bucket}")
    private String bucket;

    public List<String> uploadImages(MultipartFile[] multipartFiles) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = "article/" + generateFileName(multipartFile.getOriginalFilename());
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
            file.delete();
            imageUrls.add(fileName.substring(fileName.lastIndexOf("/") + 1));
        }
        return imageUrls;
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