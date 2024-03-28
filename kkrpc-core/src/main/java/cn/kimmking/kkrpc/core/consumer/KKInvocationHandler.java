package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.*;
import cn.kimmking.kkrpc.core.consumer.http.OkHttpInvoker;
import cn.kimmking.kkrpc.core.governance.SlidingTimeWindow;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static cn.kimmking.kkrpc.core.util.TypeUtils.castMethodResult;

/**
 * 消费端的动态代理处理类.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 20:02
 */
@Slf4j
public class KKInvocationHandler implements InvocationHandler {

    Class<?> service;
    RpcContext context;
    final List<InstanceMeta> providers;

    final List<InstanceMeta> isolatedProviders = new ArrayList<>();

    final List<InstanceMeta> halfopenProviders = new ArrayList<>();

    Map<String, SlidingTimeWindow> windows = new HashMap<>();

    HttpInvoker httpInvoker;

    ScheduledExecutorService executor;

    public KKInvocationHandler(Class<?> clazz, RpcContext context, List<InstanceMeta> providers) {
        this.service = clazz;
        this.context = context;
        this.providers = providers;
        int timeout = Integer.parseInt(context.getParameters()
                .getOrDefault("app.timeout", "1000"));
        this.httpInvoker = new OkHttpInvoker(timeout);
        this.executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(this::halfopen,10, 60, TimeUnit.SECONDS);
    }

    private void halfopen() {
        log.debug(" ===> halfopen add all isolated providers：{}", isolatedProviders);
        halfopenProviders.clear();
        halfopenProviders.addAll(isolatedProviders);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (MethodUtils.checkLocalMethod(method.getName())) {
            return null;
        }

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setService(service.getCanonicalName());
        rpcRequest.setMethodSign(MethodUtils.methodSign(method));
        rpcRequest.setArgs(args);

        int retries = Integer.parseInt(context.getParameters()
                .getOrDefault("app.retries", "1"));

        while (retries -- > 0) {
            log.debug(" ===> reties: " + retries);
            try {
                for (Filter filter : this.context.getFilters()) {
                    Object preResult = filter.prefilter(rpcRequest);
                    if (preResult != null) {
                        log.debug(filter.getClass().getName() + " ==> prefilter: " + preResult);
                        return preResult;
                    }
                }

                InstanceMeta instance;
                synchronized (halfopenProviders) {
                    if (halfopenProviders.isEmpty()) {
                        List<InstanceMeta> instances = context.getRouter().route(providers);
                        instance = context.getLoadBalancer().choose(instances);
                        log.debug("loadBalancer.choose(instances) ==> " + instance);
                    } else {
                        instance = halfopenProviders.remove(0);
                        log.debug(" ===> halfopenProviders.remove(0)  ==> " + instance);
                    }
                }

                RpcResponse<?> rpcResponse;
                Object result;
                String url = instance.toUrl();
                try {
                    rpcResponse = httpInvoker.post(rpcRequest, url);
                    result = castReturnResult(method, rpcResponse);
                } catch (Exception e) {
                    // 记录 instance 一次调用故障。
                    // 如果一定时间内多次故障，比如30s内10次，则标记节点为不可用。

                    if(providers.contains(instance)) {
                        SlidingTimeWindow window = windows.get(url);
                        if (window == null) {
                            window = new SlidingTimeWindow();
                            windows.put(url, window);
                        }

                        window.record(System.currentTimeMillis());
                        log.debug("instance {} in window with {}", instance, window.getSum());
                        if (window.getSum() >= 10) {
                            isolate(instance);
                        }
                    }

                    throw e;
                }

                for (Filter filter : this.context.getFilters()) {
                    Object filterResult = filter.postfilter(rpcRequest, rpcResponse, result);
                    if (filterResult != null) {
                        return filterResult;
                    }
                }

                synchronized (providers) {
                    if (!providers.contains(instance)) {
                        isolatedProviders.remove(instance);
                        providers.add(instance);
                        log.debug("instance[{}] is recovered, isolatedProviders=[{}],providers=[{}]", instance,isolatedProviders,providers);
                    }
                }

                return result;
            } catch (Exception ex) {
                if (!(ex.getCause() instanceof SocketTimeoutException)) {
                    throw ex;
                }
            }
        }
        return null;
    }

    private void isolate(InstanceMeta instance) {
        log.debug("isolate " + instance);
        providers.remove(instance);
        log.debug("providers = " + providers);
        isolatedProviders.add(instance);
        log.debug("isolatedProviders =  " + isolatedProviders);
    }

    private static Object castReturnResult(Method method, RpcResponse<?> rpcResponse) {
        if (rpcResponse.isStatus()) {
            return castMethodResult(method, rpcResponse.getData());
        } else {
            Exception exception = rpcResponse.getEx();
            if(exception instanceof RpcException ex) {
                throw ex;
            } else {
                throw new RpcException(exception, RpcException.UnknownEx);
            }
        }
    }
}
