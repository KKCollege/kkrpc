package cn.kimmking.kkrpc.core.api;

import java.util.List;

/**
 * 路由器.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/16 19:12
 */
public interface Router<T> {

    List<T> route(List<T> providers);

    Router Default = p -> p;

}
