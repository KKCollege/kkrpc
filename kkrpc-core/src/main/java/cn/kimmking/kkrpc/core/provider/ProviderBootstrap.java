package cn.kimmking.kkrpc.core.provider;

import cn.kimmking.kkrpc.core.annotation.KKProvider;
import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;
import cn.kimmking.kkrpc.core.meta.ProviderMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 21:30
 */

@Data
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    @PostConstruct  // init-method
    public void start() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(KKProvider.class);
        providers.forEach((x,y) -> System.out.println(x));
//        skeleton.putAll(providers);

        providers.values().forEach(
                x -> genInterface(x)
        );

    }

    private void genInterface(Object x) {
        Class<?> itfer = x.getClass().getInterfaces()[0];
        Method[] methods = itfer.getMethods();
        for (Method method : methods) {
            if (MethodUtils.checkLocalMethod(method)) {
                continue;
            }
            createProvider(itfer, x, method);
        }
    }

    private void createProvider(Class<?> itfer, Object x, Method method) {
        ProviderMeta meta = new ProviderMeta();
        meta.setMethod(method);
        meta.setServiceImpl(x);
        meta.setMethodSign(MethodUtils.methodSign(method));
        System.out.println(" create a provider: " + meta);
        skeleton.add(itfer.getCanonicalName(), meta);
    }

    public RpcResponse invoke(RpcRequest request) {
        RpcResponse rpcResponse = new RpcResponse();
        List<ProviderMeta> providerMetas = skeleton.get(request.getService());
        try {
            ProviderMeta meta = findProviderMeta(providerMetas, request.getMethodSign());
            Method method = meta.getMethod();
            Object result = method.invoke(meta.getServiceImpl(), request.getArgs());
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
            return rpcResponse;
        } catch (InvocationTargetException e) {
            rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
        } catch (IllegalAccessException e) {
            rpcResponse.setEx(new RuntimeException(e.getMessage()));
        }
        return rpcResponse;
    }

    private ProviderMeta findProviderMeta(List<ProviderMeta> providerMetas, String methodSign) {
        Optional<ProviderMeta> optional = providerMetas.stream()
                .filter(x -> x.getMethodSign().equals(methodSign)).findFirst();
        return optional.orElse(null);
    }

}
