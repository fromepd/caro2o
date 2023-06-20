package com.ruoyi.common.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Eastern unbeaten
 * @Email chenshiyun2011@163.com
 * @Date 2022/9/3 6:44 下午
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MiniIoConfig {
    private static String endpoint;
    private String accessKey;
    private String secretKey;
    private static String bucket;

    /**
     * @return
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

    public void setEndpoint(String endpoint) {
        MiniIoConfig.endpoint = endpoint;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        MiniIoConfig.bucket = bucket;
    }

    public static String getEndpoint() {
        return endpoint;
    }

    public static String getBucket() {
        return bucket;
    }
}
