package cn.kimmking.kkrpc.core.api;

/**
 * 过滤器.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/16 19:12
 */
public interface Filter {

    Object prefilter(RpcRequest request);

    Object postfilter(RpcRequest request, RpcResponse response, Object result);

    // Filter next();

    Filter Default = new Filter() {
        @Override
        public RpcResponse prefilter(RpcRequest request) {
            return null;
        }

        @Override
        public Object postfilter(RpcRequest request, RpcResponse response, Object result) {
            return null;
        }
    };

}
