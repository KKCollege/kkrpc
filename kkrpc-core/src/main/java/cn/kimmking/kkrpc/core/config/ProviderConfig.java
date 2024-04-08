package cn.kimmking.kkrpc.core.config;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.provider.ProviderBootstrap;
import cn.kimmking.kkrpc.core.provider.ProviderInvoker;
import cn.kimmking.kkrpc.core.registry.zk.ZkRegistryCenter;
import cn.kimmking.kkrpc.core.transport.SpringBootTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * provider config class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 21:33
 */

@Slf4j
@Configuration
@Import({ProviderProperties.class, AppProperties.class,SpringBootTransport.class})
public class ProviderConfig {

    @Value("${server.port:8080}")
    private String port;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "apollo.bootstrap", value = "enabled")
    ApolloChangedListener provider_apolloChangedListener() {
        return new ApolloChangedListener();
    }

    @Bean
    ProviderBootstrap providerBootstrap(@Autowired AppProperties ap,
                                        @Autowired ProviderProperties pp) {
        return new ProviderBootstrap(port, ap, pp);
    }

    @Bean
    ProviderInvoker providerInvoker(@Autowired ProviderBootstrap provider) {
        return new ProviderInvoker(provider);
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrap_runner(@Autowired ProviderBootstrap provider) {
        return x -> {
            log.info("providerBootstrap starting ...");
            provider.start();
            log.info("providerBootstrap started ...");
        };
    }

    @Bean //(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public RegistryCenter provider_rc() {
        return new ZkRegistryCenter();
    }

}
