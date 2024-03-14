package cn.kimmking.kkrpc.core.api;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/14 09:43
 */
public interface LoadBalancer<T> {

    T choose(List<T> urls);

    LoadBalancer DefaultLoadBalancer = urls -> {
            if(urls == null || urls.size() == 0) return null;
            return urls.get(0);
    };

}
