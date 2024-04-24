package cn.kimmking.kkrpc.core.registry.kk;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.kimmking.kkrpc.core.registry.ChangedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * implementation for kk registry center.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/24 下午8:21
 */
@Slf4j
public class KkRegistryCenter implements RegistryCenter {

    @Value("${kkregistry.servers}")
    private String servers;

    @Override
    public void start() {
        log.info(" ====>>>> [KKRegistry] : start with server : {}", servers);
    }

    @Override
    public void stop() {
        log.info(" ====>>>> [KKRegistry] : stop with server : {}", servers);
    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        log.info(" ====>>>> [KKRegistry] : registry instance {} for {}", instance, service);

    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {

    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        return List.of();
    }

    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {

    }
}
