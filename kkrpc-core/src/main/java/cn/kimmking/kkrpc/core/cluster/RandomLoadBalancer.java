package cn.kimmking.kkrpc.core.cluster;

import cn.kimmking.kkrpc.core.api.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/16 19:53
 */
public class RandomLoadBalancer<T> implements LoadBalancer<T> {

    Random random = new Random();
    @Override
    public T choose(List<T> providers) {
        if(providers == null || providers.isEmpty()) return null;
        if(providers.size() == 1) return providers.get(0);
        return providers.get(random.nextInt(providers.size()));
    }
}
