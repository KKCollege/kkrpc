package cn.kimmking.kkrpc.core.provider;

import cn.kimmking.kkrpc.core.api.RpcContext;
import cn.kimmking.kkrpc.core.api.RpcException;
import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;
import cn.kimmking.kkrpc.core.governance.SlidingTimeWindow;
import cn.kimmking.kkrpc.core.meta.ProviderMeta;
import cn.kimmking.kkrpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.kimmking.kkrpc.core.api.RpcException.ExceedLimitEx;

/**
 * invoke the service methods in provider.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/20 20:16
 */

@Slf4j
public class ProviderInvoker {

    private MultiValueMap<String, ProviderMeta> skeleton;

    private int tpsLimit = 20;

    final Map<String, SlidingTimeWindow> windows = new HashMap<>();

    public ProviderInvoker(ProviderBootstrap providerBootstrap) {
        this.skeleton = providerBootstrap.getSkeleton();
    }

    public RpcResponse<Object> invoke(RpcRequest request) {
        log.debug(" ===> ProviderInvoker.invoke(request:{})", request);
        if(!request.getParams().isEmpty()) {
            request.getParams().forEach(RpcContext::setContextParameter);
        }
        RpcResponse<Object> rpcResponse = new RpcResponse<>();
        String service = request.getService();
        synchronized (windows) {
            SlidingTimeWindow window = windows.computeIfAbsent(service, k -> new SlidingTimeWindow());
            if (window.calcSum() >= tpsLimit) {
                System.out.println(window.toString());
                throw new RpcException("service " + service + " invoked in 30s/[" +
                        window.getSum() + "] larger than tpsLimit = " + tpsLimit, ExceedLimitEx);
            } else {
                window.record(System.currentTimeMillis());
                log.debug("service {} in window with {}", service, window.getSum());
            }
        }

        List<ProviderMeta> providerMetas = skeleton.get(service);
        try {
            ProviderMeta meta = findProviderMeta(providerMetas, request.getMethodSign());
            Method method = meta.getMethod();
            Object[] args = processArgs(request.getArgs(), method.getParameterTypes(), method.getGenericParameterTypes());
            Object result = method.invoke(meta.getServiceImpl(), args);
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
            rpcResponse.setEx(new RpcException(e.getTargetException().getMessage()));
        } catch (IllegalAccessException | IllegalArgumentException e) {
            //e.printStackTrace();
            rpcResponse.setEx(new RpcException(e.getMessage()));
        } finally {
            RpcContext.ContextParameters.get().clear(); // 防止内存泄露和上下文污染
        }
        log.debug(" ===> ProviderInvoker.invoke() = {}", rpcResponse);
        return rpcResponse;
    }

    private Object[] processArgs(Object[] args, Class<?>[] parameterTypes, Type[] genericParameterTypes) {
        if(args == null || args.length == 0) return args;
        Object[] actual = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            actual[i] = TypeUtils.castGeneric(args[i], parameterTypes[i], genericParameterTypes[i]);
        }
        return actual;
    }

    private ProviderMeta findProviderMeta(List<ProviderMeta> providerMetas, String methodSign) {
        Optional<ProviderMeta> optional = providerMetas.stream()
                .filter(x -> x.getMethodSign().equals(methodSign)).findFirst();
        return optional.orElse(null);
    }

}
