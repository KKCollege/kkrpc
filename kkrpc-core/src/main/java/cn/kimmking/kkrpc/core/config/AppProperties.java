package cn.kimmking.kkrpc.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * config app properties.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/3 08:16
 */

@Data
//@Configuration
@ConfigurationProperties(prefix = "kkrpc.app")
public class AppProperties {

    // for app instance
    private String id = "app1";

    private String namespace = "public";

    private String env = "dev";

}
