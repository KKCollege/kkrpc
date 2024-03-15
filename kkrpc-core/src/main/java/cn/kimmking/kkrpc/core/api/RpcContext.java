package cn.kimmking.kkrpc.core.api;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/15 10:32
 */

@Data
public class RpcContext {

    List<Filter> filters = new ArrayList<>();
    Router<String> router;
    LoadBalancer<String> loadBalancer;

}
