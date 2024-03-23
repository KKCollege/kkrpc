package cn.kimmking.kkrpc.core.api;

import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import lombok.Data;

import java.util.List;

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

}
