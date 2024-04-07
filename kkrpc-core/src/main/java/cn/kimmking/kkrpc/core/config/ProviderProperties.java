package cn.kimmking.kkrpc.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * config provider properties.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/3 08:16
 */

@Data
@ConfigurationProperties(prefix = "kkrpc.provider")
public class ProviderProperties {

    // for provider

    Map<String, String> metas = new HashMap<>();

    String test;

    public void setTest(String test) {
        this.test = test;
    }


}
