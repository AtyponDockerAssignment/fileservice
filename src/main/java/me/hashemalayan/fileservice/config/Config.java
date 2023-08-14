package me.hashemalayan.fileservice.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import me.hashemalayan.fileservice.properties.MinioProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Config {
    private MinioProperties minioProperties;

    @Autowired
    public Config(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public MinioClient minioClient() {

        return MinioClient
                .builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
