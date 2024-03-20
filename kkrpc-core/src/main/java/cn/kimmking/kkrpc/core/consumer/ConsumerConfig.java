package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.LoadBalancer;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.api.Router;
import cn.kimmking.kkrpc.core.cluster.RoundRibonLoadBalancer;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.registry.zk.ZkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 19:49
 */

@Configuration
public class ConsumerConfig {

    @Value("${kkrpc.providers}")
    String servers;

    @Bean
    ConsumerBootstrap createConsumerBootstrap() {
        return new ConsumerBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner consumerBootstrap_runner(@Autowired ConsumerBootstrap consumerBootstrap) {
        return x -> {
            System.out.println("consumerBootstrap starting ...");
            consumerBootstrap.start();
            System.out.println("consumerBootstrap started ...");
        };
    }

    @Bean
    public LoadBalancer<InstanceMeta> loadBalancer() {
        //return LoadBalancer.Default;
        return new RoundRibonLoadBalancer<>();
    }

    @Bean
    public Router<InstanceMeta> router() {
        return Router.Default;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter consumer_rc() {
        return new ZkRegistryCenter();
    }

}
