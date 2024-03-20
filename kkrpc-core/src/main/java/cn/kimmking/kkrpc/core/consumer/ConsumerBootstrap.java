package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.annotation.KKConsumer;
import cn.kimmking.kkrpc.core.api.LoadBalancer;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.api.Router;
import cn.kimmking.kkrpc.core.api.RpcContext;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 19:46
 */

@Data
@Slf4j
public class ConsumerBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Value("${app.id}")
    public String app;
    @Value("${app.namespace}")
    public String namespace;
    @Value("${app.env}")
    public String env;

    private Map<String, Object> stub = new HashMap<>();

    public void start() {

        Router<?> router = applicationContext.getBean(Router.class);
        LoadBalancer<?> loadBalancer = applicationContext.getBean(LoadBalancer.class);
        RegistryCenter rc = applicationContext.getBean(RegistryCenter.class);

        RpcContext context = new RpcContext();
        context.setRouter(router);
        context.setLoadBalancer(loadBalancer);

        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);

            List<Field> fields = MethodUtils.findAnnotatedField(bean.getClass(), KKConsumer.class);
            fields.forEach(f -> {
                log.info(" ===> " + f.getName());
                try {
                    Class<?> service = f.getType();
                    String serviceName = service.getCanonicalName();
                    Object consumer = stub.get(serviceName);
                    if (consumer == null) {
                        consumer = createFromRegisry(service, context, rc);
                    }
                    f.setAccessible(true);
                    f.set(bean, consumer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        }
    }

    private Object createFromRegisry(Class<?> service, RpcContext context, RegistryCenter rc) {
        String serviceName = service.getCanonicalName();
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .app(app).namespace(namespace).env(env).name(serviceName).build();
        List<InstanceMeta> instances = rc.fetchAll(serviceMeta);
        log.info(" ===> map to providers: ");
        instances.forEach(System.out::println);

        rc.subscribe(serviceMeta, event -> {
            instances.clear();
            instances.addAll(event.getData());
        });

        return createConsumer(service, context, instances);
    }

    private Object createConsumer(Class<?> service, RpcContext context, List<InstanceMeta> providers) {
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new KKInvocationHandler(service, context, providers));
    }

}
