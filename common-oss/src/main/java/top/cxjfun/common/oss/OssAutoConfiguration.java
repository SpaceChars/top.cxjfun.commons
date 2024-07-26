package top.cxjfun.common.oss;

import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

@AutoConfiguration
@EnableConfigurationProperties(OssConfigurationProperties.class)
public class OssAutoConfiguration {

    @Bean
    public MinioClient minioClient(OssConfigurationProperties ossConfigProperties){
        Assert.notNull(ossConfigProperties,"The don't find of OssConfigurationProperties Type Bean");

        Assert.notNull(ossConfigProperties.getEndpoint(),"OSS server endpoint is undefined");
        Assert.notNull(ossConfigProperties.getAccessKey(),"OSS Server Access Key don't null");
        Assert.notNull(ossConfigProperties.getSecretKey(),"OSS Secret Key don't null");

        MinioClient minioClient = MinioClient.builder().endpoint(ossConfigProperties.getEndpoint()).credentials(ossConfigProperties.getAccessKey(), ossConfigProperties.getSecretKey()).build();
        OssHelper.minioClient=minioClient;
        OssHelper.properties=ossConfigProperties;
        return minioClient;
    }
}
