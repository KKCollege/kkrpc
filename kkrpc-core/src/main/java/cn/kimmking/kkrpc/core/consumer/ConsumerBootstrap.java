package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.annotation.KKConsumer;
import cn.kimmking.kkrpc.core.api.*;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费者启动类.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 19:46
 */

@Data
@Slf4j
public class ConsumerBootstrap implements ApplicationContextAware, EnvironmentAware {

    ApplicationContext applicationContext;
    Environment environment;

    private Map<String, Object> stub = new HashMap<>();

    public void start() {
        RegistryCenter rc = applicationContext.getBean(RegistryCenter.class);
        RpcContext context = applicationContext.getBean(RpcContext.class);

        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            List<Field> fields = MethodUtils.findAnnotatedField(bean.getClass(), KKConsumer.class);
            fields.stream().forEach( f -> {
                Class<?> service = f.getType();
                String serviceName = service.getCanonicalName();
                log.info(" ===> " + f.getName());
                try {
                    Object consumer = stub.get(serviceName);
                    if (consumer == null) {
                        consumer = createFromRegisry(service, context, rc);
                        stub.put(serviceName, consumer);
                    }
                    f.setAccessible(true);
                    f.set(bean, consumer);
                } catch (Exception ex) {
                    // ignore and print it
                    log.warn(" ==> Field[{}.{}] create consumer failed.", serviceName, f.getName());
                    log.error("Ignore and print it as: ", ex);
                }
            });
        }
    }

    private Object createFromRegisry(Class<?> service, RpcContext context, RegistryCenter rc) {
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .app(context.param("app.id")).namespace(context.param("app.namespace"))
                .env(context.param("app.env")).name(service.getCanonicalName()).build();
        List<InstanceMeta> providers = rc.fetchAll(serviceMeta);
        log.info(" ===> map to providers: ");
        providers.forEach(System.out::println);

        rc.subscribe(serviceMeta, event -> {
            providers.clear();
            providers.addAll(event.getData());
        });

        return createConsumer(service, context, providers);
    }

    private Object createConsumer(Class<?> service, RpcContext context, List<InstanceMeta> providers) {
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new KKInvocationHandler(service, context, providers));
    }

}
