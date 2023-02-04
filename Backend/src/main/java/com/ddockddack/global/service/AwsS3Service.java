package com.ddockddack.global.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@NoArgsConstructor
public class AwsS3Service {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accesskey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accesskey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String multipartFileUpload(MultipartFile uploadFile) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        try {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile.getInputStream(), null).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String InputStreamUpload(byte[] byteImages) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        s3Client.putObject(new PutObjectRequest(bucket, fileName, new ByteArrayInputStream(byteImages), null).withCannedAcl(CannedAccessControlList.PublicRead));

        return fileName;
    }


}
