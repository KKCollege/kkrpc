package cn.kimmking.kkrpc.core.api;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/14 09:43
 */
public interface Router<T> {

    List<T> route(List<T> urls);

    Router DefaultRouter = urls -> urls;

}
