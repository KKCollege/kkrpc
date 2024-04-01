package cn.kimmking.kkrpc.core.filter;

import cn.kimmking.kkrpc.core.api.Filter;
import cn.kimmking.kkrpc.core.api.RpcContext;
import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;

import java.util.Map;

/**
 * 处理上下文参数.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/1 17:59
 */
public class ParameterFilter implements Filter {
    @Override
    public Object prefilter(RpcRequest request) {
        Map<String, String> params = RpcContext.ContextParameters.get();
        if(!params.isEmpty()) {
            request.getParams().putAll(params);
        }
        return null;
    }

    @Override
    public Object postfilter(RpcRequest request, RpcResponse response, Object result) {
        // RpcContext.ContextParameters.get().clear();
        return null;
    }
}
