package cn.kimmking.kkrpc.demo.consumer;

import cn.kimmking.kkrpc.core.test.EmbeddedZookeeper;
import cn.kimmking.kkrpc.demo.provider.KkrpcDemoProviderApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class KkrpcDemoConsumerApplicationTests {

    static ApplicationContext context;
    static EmbeddedZookeeper embeddedZookeeper;

    @BeforeAll
    static void init() {

        embeddedZookeeper =  new EmbeddedZookeeper("localhost:2182");
        context = SpringApplication.run(KkrpcDemoProviderApplication.class,
                "--server.port=8084", "--kkrpc.zkServer=localhost:2182",
                "--logging.level.cn.kimmking.kkrpc=debug");
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> aaaa  .... ");
    }

    @AfterAll
    static void destory() {
        SpringApplication.exit(context, () -> 1);
        embeddedZookeeper.shutdown();
    }

}
