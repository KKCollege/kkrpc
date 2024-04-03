package cn.kimmking.kkrpc.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * config app properties.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/3 08:16
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "kkrpc.app")
public class AppConfigProperties {

    // for app instance
    private String id;

    private String namespace;

    private String env;

}
