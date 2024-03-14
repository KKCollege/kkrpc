package cn.kimmking.kkrpc.core.cluster;

import cn.kimmking.kkrpc.core.api.LoadBalancer;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/14 14:57
 */
public class RoundRibonLoadBalancer<T> implements LoadBalancer<T> {
    @Override
    public T choose(List<T> urls) {
        return null;
    }
}
