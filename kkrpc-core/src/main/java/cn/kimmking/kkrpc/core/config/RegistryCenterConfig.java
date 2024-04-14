package cn.kimmking.kkrpc.core.config;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.provider.ProviderBootstrap;
import cn.kimmking.kkrpc.core.provider.ProviderInvoker;
import cn.kimmking.kkrpc.core.registry.kk.KKRegistryCenter;
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
public class RegistryCenterConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
//    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "kkregistry", value = "enabled", havingValue = "true")
    public RegistryCenter registryCenter() {
        return new KKRegistryCenter();
    }

}
