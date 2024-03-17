package cn.kimmking.kkrpc.core.registry;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/17 21:47
 */
public interface ChangedListener {
    void fire(Event event);
}
