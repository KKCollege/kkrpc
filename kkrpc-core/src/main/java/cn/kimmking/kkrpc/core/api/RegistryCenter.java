package cn.kimmking.kkrpc.core.api;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/16 20:39
 */
public interface RegistryCenter {

    void start(); // p/c
    void stop(); // p/c

    // provider侧
    void register(String service, String instance); // p
    void unregister(String service, String instance); // p

    // consumer侧
    List<String> fetchAll(String service); // c
    // void subscribe(); // c
    // void heartbeat();

    class StaticRegistryCenter implements RegistryCenter {

        List<String> providers;
        public StaticRegistryCenter(List<String> providers) {
            this.providers = providers;
        }

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
            return providers;
        }
    }

}
