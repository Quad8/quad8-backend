package site.keydeuk.store.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public String uploadBase64ToImage(byte[] byteImage){

        String objectKey = "keydeuk/product/custom"+ UUID.randomUUID()+".png";
        String objectUrl = null;

        try (InputStream inputStream = new ByteArrayInputStream(byteImage)){
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(byteImage.length);
            metadata.setContentType("image/png");

            amazonS3Client.putObject(bucketName,objectKey,inputStream,metadata);

            objectUrl = amazonS3Client.getUrl(bucketName,objectKey).toString();

        }catch (Exception e){
            e.printStackTrace();;
            log.error("error: {}",e.getMessage());
        }

        if (objectUrl.isEmpty()){
            throw new NullPointerException();
        }

        return objectUrl;
    }

}
