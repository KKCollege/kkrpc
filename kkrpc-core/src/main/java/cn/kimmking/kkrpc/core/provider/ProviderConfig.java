package cn.kimmking.kkrpc.core.provider;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.consumer.ConsumerBootstrap;
import cn.kimmking.kkrpc.core.registry.ZkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 21:33
 */

@Configuration
public class ProviderConfig {

    @Value("${kkrpc.zkServer}")
    String zkServer;

    @Value("${kkrpc.namespace}")
    String namespace;


    @Bean
    ProviderBootstrap providerBootstrap() {
        return new ProviderBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrap_runner(@Autowired ProviderBootstrap providerBootstrap) {
        return x -> {
            System.out.println("providerBootstrap starting ...");
            providerBootstrap.start();
            System.out.println("providerBootstrap started ...");
        };
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter rc() {
        return new ZkRegistryCenter(this.zkServer, this.namespace);
    }

}
