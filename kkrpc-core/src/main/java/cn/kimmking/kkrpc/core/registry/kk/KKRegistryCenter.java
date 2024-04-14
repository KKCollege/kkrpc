package cn.kimmking.kkrpc.core.registry.kk;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.consumer.HttpInvoker;
import cn.kimmking.kkrpc.core.consumer.http.OkHttpInvoker;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.kimmking.kkrpc.core.registry.ChangedListener;
import cn.kimmking.kkrpc.core.registry.Event;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implementation of KKRegistryCenter.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/15 00:26
 */

@Slf4j
public class KKRegistryCenter implements RegistryCenter {

    @Value("${kkregistry.servers}")
    String server;

    private final Map<String, Long> VERSIONS = new HashMap<>();

    @Override
    public void start() {
        log.info(" ====>>>> [KKRegistry] : start with server: {}", server);
    }

    @Override
    public void stop() {
        log.info(" ====>>>> [KKRegistry] : stop with server: {}", server);
    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        log.info(" ====>>>> [KKRegistry] : register instance {} to {}", instance.toUrl(), service.toPath());
        InstanceMeta inst = HttpInvoker.httpPost(JSON.toJSONString(instance), regPath(service), InstanceMeta.class);
        log.info(" ====>>>> [KKRegistry] : registered {}", inst);
    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {
        log.info(" ====>>>> [KKRegistry] : unregister instance {} to {}", instance.toUrl(), service.toPath());
        InstanceMeta inst = HttpInvoker.httpPost(JSON.toJSONString(instance), unregPath(service), InstanceMeta.class);
        log.info(" ====>>>> [KKRegistry] : unregistered {}", inst);
    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        log.info(" ====>>>> [KKRegistry] : find all instances for {}", service.toPath());
        List<InstanceMeta> instances = HttpInvoker.httpGet(findAllPath(service), new TypeReference<List<InstanceMeta>>() {});
        log.info(" ====>>>> [KKRegistry] : findAll = {}", instances);
        return instances;
    }

    KKHeathChecker heathChecker = new KKHeathChecker();
    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {
        heathChecker.check(() -> {
            String versionPath = versionPath(service);
            Long newVersion = HttpInvoker.httpGet(versionPath, Long.class);
            Long version = VERSIONS.getOrDefault(service.toPath(), -1L);
            log.debug(" ====>>>> [{}] newVersion:{} oldVersion:{}", service.toPath(), newVersion, version);
            if (newVersion > version) {
                log.info(" ====>>>> version changed [{}] newVersion:{} oldVersion:{}", service.toPath(), newVersion, version);
                List<InstanceMeta> instances = fetchAll(service);
                log.info(" ====>>>> version {} fetch all and fire: {}", newVersion, instances);
                listener.fire(new Event(instances));
                VERSIONS.put(service.toPath(), newVersion);
            }
        });
    }

    private String regPath(ServiceMeta service) {
        return server + "/reg?service=" + service.toPath();
    }

    private String unregPath(ServiceMeta service) {
        return server + "/unreg?service=" + service.toPath();
    }

    private String findAllPath(ServiceMeta service) {
        return server + "/findAll?service=" + service.toPath();
    }

    private String versionPath(ServiceMeta service) {
        return server + "/version?service=" + service.toPath();
    }
}
