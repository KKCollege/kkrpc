package cn.kimmking.kkrpc.demo.consumer;

import cn.kimmking.kkrpc.demo.provider.KkrpcDemoProviderApplication;
import com.ctrip.framework.apollo.mockserver.ApolloTestingServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
properties = {"kkregistry.servers=http://localhost:8094/registry", "kkrpc.app.env=test"})
class KkrpcDemoConsumerApplicationTests {

    static ApplicationContext context1;
    static ApplicationContext context2;

//    static TestZKServer zkServer = new TestZKServer();

    //    @ClassRule // junit4
    static ApolloTestingServer apollo = new ApolloTestingServer();


    @SneakyThrows
    @BeforeAll
    static void init() {
//        System.out.println(" ====================================== ");
//        System.out.println(" ====================================== ");
//        System.out.println(" =============     ZK2182    ========== ");
//        System.out.println(" ====================================== ");
//        System.out.println(" ====================================== ");
//        zkServer.start();
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        System.out.println(" ===========     mock apollo    ======= ");
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        apollo.start();
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        System.out.println(" =============      P8094    ========== ");
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        context1 = SpringApplication.run(KkrpcDemoProviderApplication.class,
                "--server.port=8094",
//                "--kkrpc.zk.server=localhost:2182",
                "--kkregistry.servers=http://localhost:8094/registry",
                "--kkrpc.zk.server=localhost:2182",
                "--kkrpc.app.env=test",
                "--logging.level.cn.kimmking.kkrpc=info",
                "--kkrpc.provider.metas.dc=bj",
                "--kkrpc.provider.metas.gray=false",
                "--kkrpc.provider.metas.unit=B001",
                "--kkrpc.provider.metas.tc=300"
        );
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        System.out.println(" =============      P8095    ========== ");
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        context2 = SpringApplication.run(KkrpcDemoProviderApplication.class,
                "--server.port=8095",
                //"--kkrpc.zk.server=localhost:2182",
                "--kkregistry.servers=http://localhost:8094/registry",
                "--kkrpc.app.env=test",
                "--logging.level.cn.kimmking.kkrpc=info",
                "--kkrpc.provider.metas.dc=bj",
                "--kkrpc.provider.metas.gray=false",
                "--kkrpc.provider.metas.unit=B002",
                "--kkrpc.provider.metas.tc=300"
        );
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> aaaa  .... ");
    }

    @AfterAll
    static void destroy() {
        System.out.println(" ===========     close spring conetext    ======= ");
        SpringApplication.exit(context1, () -> 1);
        SpringApplication.exit(context2, () -> 1);
//        System.out.println(" ===========     stop zookeeper server    ======= ");
//        zkServer.stop();
        System.out.println(" ===========     stop apollo mockserver   ======= ");
        apollo.close();
        System.out.println(" ===========     destroy in after all     ======= ");
    }

}
