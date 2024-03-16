package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.LoadBalancer;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.api.Router;
import cn.kimmking.kkrpc.core.cluster.RandomLoadBalancer;
import cn.kimmking.kkrpc.core.cluster.RoundRibonLoadBalancer;
import cn.kimmking.kkrpc.core.registry.ZkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 19:49
 */

@Configuration
public class ConsumerConfig {

    @Value("${kkrpc.providers}")
    String providers;

    @Value("${kkrpc.zkServer}")
    String zkServer;

    @Value("${kkrpc.namespace}")
    String namespace;

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
    public LoadBalancer loadBalancer() {
        //return LoadBalancer.DefaultLoadBalancer;
        return new RoundRibonLoadBalancer();
    }

    @Bean
    public Router router() {
        return Router.DefaultRouter;
    }

//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public RegistryCenter rc() {
//        System.out.println(this.providers);
//        return new RegistryCenter.StaticRegistryCenter(List.of(providers.split(",")));
//    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter rc() {
        return new ZkRegistryCenter(this.zkServer, this.namespace);
    }

}
