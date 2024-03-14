package cn.kimmking.kkrpc.core.api;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/14 09:43
 */
public interface LoadBalancer {

    String choose(List<String> urls);

    LoadBalancer DefaultLoadBalancer = new LoadBalancer() {
        @Override
        public String choose(List<String> urls) {
            if(urls == null || urls.size() == 0) return null;
            return urls.get(0);
        }
    };

}
