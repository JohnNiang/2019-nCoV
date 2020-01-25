package me.johnniang.ncov.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ncov")
@Data
public class NotificationProperties {

    private String[] to;

    private String cron;

}
