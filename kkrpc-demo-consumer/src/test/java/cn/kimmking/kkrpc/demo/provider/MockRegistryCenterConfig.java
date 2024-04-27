package cn.kimmking.kkrpc.demo.provider;

import cn.kimmking.kkrpc.core.transport.MockKKRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/28 上午2:35
 */
@Configuration
public class MockRegistryCenterConfig {
    @Bean
    @ConditionalOnMissingBean
    MockKKRegistry mockKKRegistry() {
        return new MockKKRegistry();
    }

}
