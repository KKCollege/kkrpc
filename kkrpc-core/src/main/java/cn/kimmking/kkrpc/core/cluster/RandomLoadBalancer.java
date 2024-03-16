package cn.kimmking.kkrpc.core.cluster;

import cn.kimmking.kkrpc.core.api.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/14 14:57
 */
public class RandomLoadBalancer<T> implements LoadBalancer<T> {

    Random random = new Random();
    @Override
    public T choose(List<T> urls) {
        return urls.get(random.nextInt(urls.size()));
    }
}
