package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;

/**
 * Interface for http invoke.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/20 20:39
 */
public interface HttpInvoker {

    RpcResponse<?> post(RpcRequest rpcRequest, String url);

}
