package me.hashemalayan.fileservice.properties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@RequiredArgsConstructor
@Data
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
}
