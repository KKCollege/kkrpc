package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.RpcContext;
import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;
import cn.kimmking.kkrpc.core.consumer.http.OkHttpInvoker;
import cn.kimmking.kkrpc.core.util.MethodUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static cn.kimmking.kkrpc.core.util.TypeUtils.castMethodResult;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 20:02
 */
@Slf4j
public class KKInvocationHandler implements InvocationHandler {

    Class<?> service;
    RpcContext context;
    List<String> providers;

    HttpInvoker httpInvoker = new OkHttpInvoker();

    public KKInvocationHandler(Class<?> clazz, RpcContext context, List<String> providers) {
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

        List<String> urls = context.getRouter().route(providers);
        String url = (String) context.getLoadBalancer().choose(urls);
        log.debug("loadBalancer.choose(urls) ==> " + url);
        RpcResponse<?> rpcResponse = httpInvoker.post(rpcRequest, url);


        if (rpcResponse.isStatus()) {
            return castMethodResult(method, rpcResponse.getData());
        } else {
            Exception ex = rpcResponse.getEx();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }


}
