package cn.kimmking.kkrpc.demo.consumer;

import cn.kimmking.kkrpc.demo.provider.KkrpcDemoProviderApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class KkrpcDemoConsumerApplicationTests {

    static ConfigurableApplicationContext context1;
    @BeforeAll
    static void init() {
        context1 = SpringApplication.run(KkrpcDemoProviderApplication.class,
                "--server.port=8084", "--logging.level.cn.kimmking=debug");
    }

    @Test
    void contextLoads() {
    }

    @AfterAll
    static void destory() {
        SpringApplication.exit(context1, () -> 1);
    }

}
