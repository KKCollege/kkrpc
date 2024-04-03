package cn.kimmking.kkrpc.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * config consumer properties.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/3 08:16
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "kkrpc.consumer")
public class ConsumerConfigProperties {


    // for ha and governance
    private int retries;

    private int timeout;

    private int faultLimit;

    private int halfOpenInitialDelay;

    private int halfOpenDelay;

    private int grayRatio;

}
