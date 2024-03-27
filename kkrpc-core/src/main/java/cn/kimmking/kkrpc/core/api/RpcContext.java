package cn.kimmking.kkrpc.core.api;

import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/16 20:26
 */

@Data
public class RpcContext {

    List<Filter> filters;

    Router<InstanceMeta> router;

    LoadBalancer<InstanceMeta> loadBalancer;

    private Map<String, String> parameters = new HashMap<>();
    // kkrpc.color = gray
    // kkrpc.gtrace_id
    // gw -> service1 ->  service2(跨线程传递) ...
    // http headers

}
