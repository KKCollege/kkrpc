package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.RpcContext;
import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;
import cn.kimmking.kkrpc.core.consumer.http.OkHttpInvoker;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.util.MethodUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static cn.kimmking.kkrpc.core.util.TypeUtils.castMethodResult;

/**
 * 消费端的动态代理处理类.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 20:02
 */
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

        List<InstanceMeta> instances = context.getRouter().route(providers);
        InstanceMeta instance = context.getLoadBalancer().choose(instances);
        System.out.println("loadBalancer.choose(instances) ==> " + instance);

        RpcResponse<?> rpcResponse = httpInvoker.post(rpcRequest, instance.toString());

        if (rpcResponse.isStatus()) {
            Object data = rpcResponse.getData();
            return castMethodResult(method, data);
        } else {
            Exception ex = rpcResponse.getEx();
            throw new RuntimeException(ex);
        }
    }
}
