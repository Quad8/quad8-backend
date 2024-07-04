package site.keydeuk.store.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public String uploadBase64ToImage(byte[] byteImage) {

        String objectKey = "keydeuk/product/custom/" + UUID.randomUUID() + ".png";
        String objectUrl = null;

        try (InputStream inputStream = new ByteArrayInputStream(byteImage)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(byteImage.length);
            metadata.setContentType("image/png");

            amazonS3Client.putObject(bucketName, objectKey, inputStream, metadata);

            objectUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error uploading image: {}", e.getMessage());
            throw new RuntimeException("Error uploading image", e);
        }

        if (objectUrl.isEmpty()) {
            throw new NullPointerException();
        }

        return objectUrl;
    }

    public String uploadUserImage(MultipartFile multipartFile) {
        String objectKey = "keydeuk/user/profile/" + UUID.randomUUID() + ".png";
        String objectUrl = null;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3Client.putObject(bucketName, objectKey, multipartFile.getInputStream(), metadata);
            objectUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error uploading image: {}", e.getMessage());
            throw new RuntimeException("Error uploading image", e);
        }
        return objectUrl;
    }
    public List<String> uploadReviewImages(List<MultipartFile> multipartFiles) {
//        String objectKey = "keydeuk/product/review/" + UUID.randomUUID() + ".png";

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = getFileExtension(originalFilename);

            // 확장자를 포함하여 객체 키 생성
            String objectKey = "keydeuk/product/review/" + UUID.randomUUID() + "." + extension;
            String imageUrl = uploadImage(objectKey, multipartFile);
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
    // 파일 확장자 추출 메서드
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid file name: " + filename);
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public String uploadCommunityImage(MultipartFile multipartFile) {
        String objectKey = "keydeuk/product/community/" + UUID.randomUUID() + ".png";
        return uploadImage(objectKey, multipartFile);
    }

    private String uploadImage(String objectKey, MultipartFile multipartFile) {
        String objectUrl = null;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3Client.putObject(bucketName, objectKey, multipartFile.getInputStream(), metadata);
            objectUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error uploading image: {}", e.getMessage());
            throw new RuntimeException("Error uploading image", e);
        }
        return objectUrl;
    }
}
