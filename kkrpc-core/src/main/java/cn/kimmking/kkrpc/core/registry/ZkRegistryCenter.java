package cn.kimmking.kkrpc.core.registry;

import cn.kimmking.kkrpc.core.api.RegistryCenter;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/16 20:59
 */
public class ZkRegistryCenter implements RegistryCenter {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void register(String service, String instance) {

    }

    @Override
    public void unregister(String service, String instance) {

    }

    @Override
    public List<String> fetchAll(String service) {
        return null;
    }
}
