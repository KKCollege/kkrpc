package cn.kimmking.kkrpc.core.registry.kk;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.consumer.HttpInvoker;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.kimmking.kkrpc.core.registry.ChangedListener;
import cn.kimmking.kkrpc.core.registry.Event;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * implementation for kk registry center.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/24 下午8:21
 */
@Slf4j
public class KkRegistryCenter implements RegistryCenter {

    private static final String REG_PATH = "/reg";
    private static final String UNREG_PATH = "/unreg";
    private static final String FINDALL_PATH = "/findAll";
    private static final String VERSION_PATH = "/version";
    private static final String RENEWS_PATH = "/renews";

    @Value("${kkregistry.servers}")
    private String servers;

    Map<String, Long> VERSIONS = new HashMap<>();
    MultiValueMap<InstanceMeta, ServiceMeta> RENEWS = new LinkedMultiValueMap<>();
    KkHealthChecker healthChecker = new KkHealthChecker();

    @Override
    public void start() {
        log.info(" ====>>>> [KKRegistry] : start with server : {}", servers);
        healthChecker.start();
        providerCheck();
    }

    @Override
    public void stop() {
        log.info(" ====>>>> [KKRegistry] : stop with server : {}", servers);
        healthChecker.stop();
    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        log.info(" ====>>>> [KKRegistry] : register instance {} for {}", instance, service);
        HttpInvoker.httpPost(JSON.toJSONString(instance), regPath(service), InstanceMeta.class);
        log.info(" ====>>>> [KKRegistry] : registered {}", instance);
        RENEWS.add(instance, service);
    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {
        log.info(" ====>>>> [KKRegistry] : unregister instance {} for {}", instance, service);
        HttpInvoker.httpPost(JSON.toJSONString(instance), unregPath(service), InstanceMeta.class);
        log.info(" ====>>>> [KKRegistry] : unregistered {}", instance);
        RENEWS.remove(instance, service);
    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        log.info(" ====>>>> [KKRegistry] : find all instances for {}", service);
        List<InstanceMeta> instances = HttpInvoker.httpGet(findAllPath(service), new TypeReference<List<InstanceMeta>>() {
        });
        log.info(" ====>>>> [KKRegistry] : findAll = {}", instances);
        return instances;
    }

    public void providerCheck() {
        healthChecker.providerCheck(() -> {
            RENEWS.keySet().stream().forEach(
                    instance -> {
                        Long timestamp = HttpInvoker.httpPost(JSON.toJSONString(instance),
                                renewsPath(RENEWS.get(instance)), Long.class);
                        log.info(" ====>>>> [KKRegistry] : renew instance {} at {}", instance, timestamp);
                    }
            );
        });
    }

    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {
        healthChecker.consumerCheck( () -> {
            Long version = VERSIONS.getOrDefault(service.toPath(), -1L);
            Long newVersion = HttpInvoker.httpGet(versionPath(service), Long.class);
            log.info(" ====>>>> [KKRegistry] : version = {}, newVersion = {}", version, newVersion);
            if(newVersion > version) {
                List<InstanceMeta> instances = fetchAll(service);
                listener.fire(new Event(instances));
                VERSIONS.put(service.toPath(), newVersion);
            }
        });
    }

    private String regPath(ServiceMeta service) {
        return path(REG_PATH, service);
    }
    private String unregPath(ServiceMeta service) {
        return path(UNREG_PATH, service);
    }
    private String findAllPath(ServiceMeta service) {
        return path(FINDALL_PATH, service);
    }
    private String versionPath(ServiceMeta service) {
        return path(VERSION_PATH, service);
    }
    private String path(String context, ServiceMeta service) {
        return servers + context + "?service=" + service.toPath();
    }

    private String renewsPath(List<ServiceMeta> serviceList) {
        return path(RENEWS_PATH, serviceList);
    }

    private String path(String context, List<ServiceMeta> serviceList) {
        StringBuffer sb = new StringBuffer();
        for (ServiceMeta service : serviceList) {
            sb.append(service.toPath()).append(",");
        }
        String services = sb.toString();
        if(services.endsWith(",")) services = services.substring(0, services.length() - 1);
        log.info(" ====>>>> [KKRegistry] : renew instance for {}", services);
        return servers + context + "?services=" + services;
    }

}
