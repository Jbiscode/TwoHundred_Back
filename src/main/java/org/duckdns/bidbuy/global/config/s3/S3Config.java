package org.duckdns.bidbuy.global.config.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${ncp.objectstorage.endpoint}")
    private String endpoint;

    @Value("${ncp.objectstorage.region}")
    private String region;

    @Value("${ncp.objectstorage.accessKey}")
    private String accessKey;

    @Value("${ncp.objectstorage.secretKey}")
    private String secretKey;

    @Value("${ncp.objectstorage.bucket}")
    private String bucket;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

}
