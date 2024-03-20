package cn.kimmking.kkrpc.core.consumer;

import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/20 20:39
 */
public interface HttpInvoker {

    RpcResponse<?> post(RpcRequest rpcRequest, String url);

}
