package cn.kimmking.kkrpc.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * config provider properties.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/3 08:16
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "kkrpc.provider")
public class ProviderConfigProperties {

    // for provider

    Map<String, String> metas = new HashMap<>();


}
