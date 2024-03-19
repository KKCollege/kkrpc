package cn.kimmking.kkrpc.core.provider;

import cn.kimmking.kkrpc.core.annotation.KKProvider;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.meta.ProviderMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.SneakyThrows;
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
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();
    private String instance;
    private RegistryCenter rc;

    @Value("${server.port}")
    private String port;

    @SneakyThrows
    @PostConstruct  // init-method
    public void init() {
        System.out.println("ProviderBootstrap init...");
        rc = applicationContext.getBean(RegistryCenter.class);
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(KKProvider.class);
        providers.forEach((x,y) -> System.out.println(x));
        providers.values().forEach(this::genInterface);
        System.out.println("ProviderBootstrap initialized.");
    }

    @SneakyThrows
    public void start() {
        System.out.println("ProviderBootstrap start...");
        rc.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        instance = ip + "_" + port;
        skeleton.keySet().forEach(this::registerService);
        System.out.println("ProviderBootstrap started.");
    }

    @PreDestroy
    public void stop() {
        System.out.println("ProviderBootstrap stop...");
        skeleton.keySet().forEach(this::unregisterService);
        rc.stop();
        System.out.println("ProviderBootstrap stopped.");
    }

    private void registerService(String service) {
        rc.register(service, instance);
    }

    private void unregisterService(String service) {
        rc.unregister(service, instance);
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
        ProviderMeta meta = new ProviderMeta();
        meta.setMethod(method);
        meta.setServiceImpl(impl);
        meta.setMethodSign(MethodUtils.methodSign(method));
        System.out.println(" create a provider: " + meta);
        skeleton.add(service.getCanonicalName(), meta);
    }
}