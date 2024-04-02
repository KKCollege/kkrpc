package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.*;
import cn.kimmking.kkrpc.core.cluster.GrayRouter;
import cn.kimmking.kkrpc.core.cluster.RoundRibonLoadBalancer;
import cn.kimmking.kkrpc.core.filter.ParameterFilter;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.registry.zk.ZkRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * Config for consumer.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 19:49
 */

@Slf4j
@Configuration
public class ConsumerConfig {

    @Value("${kkrpc.providers:}")
    String[] servers;

    @Value("${app.grayRatio:0}")
    private int grayRatio;

    @Value("${app.id:app1}")
    private String app;

    @Value("${app.namespace:public}")
    private String namespace;

    @Value("${app.env:dev}")
    private String env;

    @Value("${app.retries:1}")
    private int retries;

    @Value("${app.timeout:1000}")
    private int timeout;

    @Value("${app.faultLimit:10}")
    private int faultLimit;

    @Value("${app.halfOpenInitialDelay:10000}")
    private int halfOpenInitialDelay;

    @Value("${app.halfOpenDelay:60000}")
    private int halfOpenDelay;

    @Bean
    ConsumerBootstrap createConsumerBootstrap() {
        return new ConsumerBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE + 1)
    public ApplicationRunner consumerBootstrap_runner(@Autowired ConsumerBootstrap consumerBootstrap) {
        return x -> {
            log.info("consumerBootstrap starting ...");
            System.out.println("kkrpc.providers => " + String.join(",", servers));
            consumerBootstrap.start();
            log.info("consumerBootstrap started ...");
        };
    }

    @Bean
    public LoadBalancer<InstanceMeta> loadBalancer() {
        return new RoundRibonLoadBalancer<>();
    }

    @Bean
    public Router<InstanceMeta> router() {
        return new GrayRouter(grayRatio);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public RegistryCenter consumer_rc() {
        return new ZkRegistryCenter();
    }

    @Bean
    public Filter defaultFilter() {
        return new ParameterFilter();
    }

    @Bean
    public RpcContext createContext(@Autowired Router router,
                                    @Autowired LoadBalancer loadBalancer,
                                    @Autowired List<Filter> filters) {
        RpcContext context = new RpcContext();
        context.setRouter(router);
        context.setLoadBalancer(loadBalancer);
        context.setFilters(filters);
        context.getParameters().put("app.id", app);
        context.getParameters().put("app.namespace", namespace);
        context.getParameters().put("app.env", env);
        context.getParameters().put("app.retries", String.valueOf(retries));
        context.getParameters().put("app.timeout", String.valueOf(timeout));
        context.getParameters().put("app.halfOpenInitialDelay", String.valueOf(halfOpenInitialDelay));
        context.getParameters().put("app.faultLimit", String.valueOf(faultLimit));
        context.getParameters().put("app.halfOpenDelay", String.valueOf(halfOpenDelay));
        return context;
    }

}
