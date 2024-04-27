package cn.kimmking.kkrpc.demo.provider;

import cn.kimmking.kkrpc.core.config.ProviderProperties;
import cn.kimmking.kkrpc.core.transport.MockKKRegistry;
import com.ctrip.framework.apollo.core.ApolloClientSystemConsts;
import com.ctrip.framework.apollo.mockserver.ApolloTestingServer;
import com.ctrip.framework.apollo.mockserver.MockApolloExtension;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MockApolloExtension.class)
class KkrpcDemoProviderApplicationTests {

    //static TestZKServer zkServer = new TestZKServer();
    static ApolloTestingServer apollo = new ApolloTestingServer();

    @Autowired
    ProviderProperties providerProperties;

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
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> KkrpcDemoProviderApplicationTests  .... ");
        System.out.println("....  ApolloClientSystemConsts.APOLLO_CONFIG_SERVICE  .....");
        System.out.println(System.getProperty(ApolloClientSystemConsts.APOLLO_CONFIG_SERVICE));
        System.out.println("....  ApolloClientSystemConsts.APOLLO_CONFIG_SERVICE  .....");
    }

    @Test
    void printProviderProperties() {
        System.out.println(" ===> KkrpcDemoProviderApplicationTests  .... ");
        System.out.println("....  providerProperties  .....");
        System.out.println(providerProperties);
        System.out.println("....  providerProperties  .....");
    }

    @AfterAll
    static void destory() {
//        System.out.println(" ===========     stop zookeeper server    ======= ");
//        zkServer.stop();
        System.out.println(" ===========     stop apollo mockserver   ======= ");
        apollo.close();
        System.out.println(" ===========     destroy in after all     ======= ");
    }

}
