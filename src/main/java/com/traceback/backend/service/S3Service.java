package com.traceback.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.time.Duration;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Presigner s3Presigner;

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
            S3Client s3Client,
            S3Presigner s3Presigner,
            @Value("${aws.s3.bucket-name}") String bucketName) {

        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    public String generatePresignedUrl(String key) {

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(15))
                        .getObjectRequest(getObjectRequest)
                        .build();

        return s3Presigner
                .presignGetObject(presignRequest)
                .url()
                .toString();
    }

    public String uploadFile(MultipartFile file) throws IOException {

        String key = "items/"
                + UUID.randomUUID()
                + "-"
                + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(
                        file.getInputStream(),
                        file.getSize()
                )
        );

        return key;
    }
}