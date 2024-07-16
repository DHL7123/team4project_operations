package com.evo.evoproject.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaverCloudConfig {

    @Value("${naver.cloud.access-key}")
    private String accessKey;

    @Value("${naver.cloud.secret-key}")
    private String secretKey;

    @Value("${naver.cloud.region}")
    private String region;

    @Value("${naver.cloud.endpoint}")
    private String endpoint;

    @Bean
    public AmazonS3 naverS3() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withPathStyleAccessEnabled(true) // 네이버 클라우드의 경우 path-style 액세스 필요
                .build();
    }
}

