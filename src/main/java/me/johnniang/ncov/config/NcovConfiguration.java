package me.johnniang.ncov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.johnniang.ncov.config.properties.NotificationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 2019-nCoV configuration.
 * @author johnniang
 */
@Configuration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(NotificationProperties.class)
public class NcovConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
