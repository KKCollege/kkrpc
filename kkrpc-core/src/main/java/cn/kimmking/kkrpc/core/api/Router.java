package cn.kimmking.kkrpc.core.api;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/14 09:43
 */
public interface Router {

    List<String> route(List<String> urls);

    Router DefaultRouter = new Router() {
        @Override
        public List<String> route(List<String> urls) {
            return urls;
        }
    };

}
