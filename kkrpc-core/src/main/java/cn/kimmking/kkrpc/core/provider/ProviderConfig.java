package cn.kimmking.kkrpc.core.provider;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.registry.zk.ZkRegistryCenter;
import cn.kimmking.kkrpc.core.transport.SpringBootTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * provider config class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 21:33
 */

@Slf4j
@Configuration
@Import({SpringBootTransport.class})
public class ProviderConfig {

    @Value("${server.port:8080}")
    private String port;

    @Value("${app.id:app1}")
    private String app;

    @Value("${app.namespace:public}")
    private String namespace;

    @Value("${app.env:dev}")
    private String env;

    @Value("#{${app.metas:{dc:'bj',gray:'false',unit:'B001'}}}")  //Spel
    Map<String, String> metas;

    @Bean
    ProviderBootstrap providerBootstrap() {
        return new ProviderBootstrap(port, app, namespace, env, metas);
    }

    @Bean
    ProviderInvoker providerInvoker(@Autowired ProviderBootstrap providerBootstrap) {
        return new ProviderInvoker(providerBootstrap);
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrap_runner(@Autowired ProviderBootstrap providerBootstrap) {
        return x -> {
            log.info("providerBootstrap starting ...");
            providerBootstrap.start();
            log.info("providerBootstrap started ...");
        };
    }

    @Bean //(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public RegistryCenter provider_rc() {
        return new ZkRegistryCenter();
    }

}
