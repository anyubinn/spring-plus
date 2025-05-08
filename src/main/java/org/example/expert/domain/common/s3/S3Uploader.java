package org.example.expert.domain.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImageFromUrl(String imageUrl) {
        try {

            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(connection.getContentLengthLong());
            metadata.setContentType(connection.getContentType());

            String fileName = UUID.randomUUID() + extractExtension(imageUrl);

            amazonS3.putObject(bucket, fileName, inputStream, metadata);

            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패");
        }
    }

    private String extractExtension(String imageUrl) {
        try {
            String path = new URL(imageUrl).getPath();
            return path.substring(path.lastIndexOf("."));
        } catch (Exception e) {
            return ".jpg";
        }
    }
}
