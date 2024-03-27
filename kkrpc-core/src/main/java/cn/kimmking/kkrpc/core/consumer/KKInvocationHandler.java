package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.Filter;
import cn.kimmking.kkrpc.core.api.RpcContext;
import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;
import cn.kimmking.kkrpc.core.consumer.http.OkHttpInvoker;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.List;

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
    List<InstanceMeta> providers;

    HttpInvoker httpInvoker = new OkHttpInvoker();

    public KKInvocationHandler(Class<?> clazz, RpcContext context, List<InstanceMeta> providers) {
        this.service = clazz;
        this.context = context;
        this.providers = providers;
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

        int retry = Integer.parseInt(context.getParameters().getOrDefault("retry", "1"));
        while ( retry-- > 0) {
            System.out.println(" ===> retry:" + retry);
            try {
                for (Filter filter : this.context.getFilters()) {
                    Object preResult = filter.prefilter(rpcRequest);
                    if (preResult != null) {
                        log.debug(filter.getClass().getName() + " ==> prefilter: " + preResult);
                        return preResult;
                    }
                }

                List<InstanceMeta> instances = context.getRouter().route(providers);
                InstanceMeta instance = context.getLoadBalancer().choose(instances);
                log.debug("loadBalancer.choose(instances) ==> " + instance);

                RpcResponse<?> rpcResponse = httpInvoker.post(rpcRequest, instance.toUrl());
                Object result = castReturnResult(method, rpcResponse);

                for (Filter filter : this.context.getFilters()) {
                    Object filterResult = filter.postfilter(rpcRequest, rpcResponse, result);
                    if (filterResult != null) {
                        return filterResult;
                    }
                }
                return result;
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                if (! (ex.getCause() instanceof SocketTimeoutException)) {
                    break;
                }
            }
        }
        return null;
    }

    private static Object castReturnResult(Method method, RpcResponse<?> rpcResponse) {
        if (rpcResponse.isStatus()) {
            Object data = rpcResponse.getData();
            return castMethodResult(method, data);
        } else {
            Exception ex = rpcResponse.getEx();
            throw new RuntimeException(ex);
        }
    }
}
