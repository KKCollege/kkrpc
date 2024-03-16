package cn.kimmking.kkrpc.core.api;

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

    List<Filter> filters; // todo

    Router router;

    LoadBalancer loadBalancer;

}
