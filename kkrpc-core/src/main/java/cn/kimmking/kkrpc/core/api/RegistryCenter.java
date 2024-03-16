package cn.kimmking.kkrpc.core.api;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/15 11:19
 */
public interface RegistryCenter {

    void register(String service, String instance);
    void unregister(String service, String instance);
    List<String> fetchAll(String service);

    void start();
    void stop();

    class StaticRegistryCenter implements RegistryCenter {

        List<String> providers;

        public StaticRegistryCenter(List<String> providers) {
            this.providers = providers;
        }

        @Override
        public void register(String service, String instance) {

        }

        @Override
        public void unregister(String service, String instance) {

        }

        @Override
        public List<String> fetchAll(String service) {
            providers.forEach(System.out::println);
            return providers;
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }
    }

}
