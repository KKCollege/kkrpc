package cn.kimmking.kkrpc.core.provider;

import cn.kimmking.kkrpc.core.annotation.KKProvider;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ProviderMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.*;

/**
 * 服务提供者的启动类.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 21:30
 */

@Data
@Slf4j
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();
    private InstanceMeta instance;
    private RegistryCenter rc;

    @Value("${server.port}")
    private String port;
    @Value("${app.id}")
    public String app;
    @Value("${app.namespace}")
    public String namespace;
    @Value("${app.env}")
    public String env;

    @SneakyThrows
    @PostConstruct  // init-method
    public void init() {
        log.info("ProviderBootstrap init...");
        rc = applicationContext.getBean(RegistryCenter.class);
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(KKProvider.class);
        providers.forEach((x,y) -> log.info(x));
        providers.values().forEach(this::genInterface);
        log.info("ProviderBootstrap initialized.");
    }

    @SneakyThrows
    public void start() {
        log.info("ProviderBootstrap start...");
        rc.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        instance = InstanceMeta.http(ip, Integer.valueOf(port));
        skeleton.keySet().forEach(this::registerService);
        log.info("ProviderBootstrap started.");
    }

    @PreDestroy
    public void stop() {
        log.info("ProviderBootstrap stop...");
        skeleton.keySet().forEach(this::unregisterService);
        rc.stop();
        log.info("ProviderBootstrap stopped.");
    }

    private void registerService(String service) {
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .app(app).namespace(namespace).env(env).name(service).build();
        rc.register(serviceMeta, instance);
    }

    private void unregisterService(String service) {
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .app(app).namespace(namespace).env(env).name(service).build();
        rc.unregister(serviceMeta, instance);
    }

    private void genInterface(Object impl) {
        Arrays.stream(impl.getClass().getInterfaces()).forEach(
                service -> {
                    Method[] methods = service.getMethods();
                    for (Method method : methods) {
                        if (MethodUtils.checkLocalMethod(method)) {
                            continue;
                        }
                        createProvider(service, impl, method);
                    }
                });
    }

    private void createProvider(Class<?> service, Object impl, Method method) {
        ProviderMeta meta = ProviderMeta.builder().method(method)
                .serviceImpl(impl).methodSign(MethodUtils.methodSign(method)).build();
        log.info(" create a provider: " + meta);
        skeleton.add(service.getCanonicalName(), meta);
    }
}