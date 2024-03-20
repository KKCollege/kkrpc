package cn.kimmking.kkrpc.core.registry.zk;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.kimmking.kkrpc.core.registry.ChangedListener;
import cn.kimmking.kkrpc.core.registry.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/16 20:59
 */
@Slf4j
public class ZkRegistryCenter implements RegistryCenter {

    private CuratorFramework client = null;
    String zkServer;
    String namespace;

    public ZkRegistryCenter(String zkServer, String namespace) {
        this.zkServer = zkServer;
        this.namespace = namespace;
    }

    @Override
    public void start() {
        log.info("zkServer: " + zkServer);
        log.info("namespace: " + namespace);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServer)
                .namespace(namespace)
                .retryPolicy(retryPolicy)
                .build();
        log.info(" ===> zk client starting.");
        client.start();
    }

    @Override
    public void stop() {
        log.info(" ===> zk client stopped.");
        client.close();
    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        String servicePath = "/" + service.toString();
        try {
            // 创建服务的持久化节点
            if (client.checkExists().forPath(servicePath) == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath, "service".getBytes());
            }
            // 创建实例的临时性节点
            String instancePath = servicePath + "/" + instance.toPath();
            log.info(" ===> register to zk: " + instancePath);
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "provider".getBytes());
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {
        String servicePath = "/" + service.toString();
        try {
            // 判断服务是否存在
            if (client.checkExists().forPath(servicePath) == null) {
                return;
            }
            // 删除实例节点
            String instancePath = servicePath + "/" + instance.toPath();
            log.info(" ===> unregister to zk: " + instancePath);
            client.delete().quietly().forPath(instancePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        String servicePath = "/" + service.toString();
        try {
            // 获取所有子节点
            List<String> nodes = client.getChildren().forPath(servicePath);
            List<InstanceMeta> instances = mapInstance(nodes);
            log.info(" ===> fetchAll from zk: " + servicePath);
            instances.forEach(System.out::println);
            return instances;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<InstanceMeta> mapInstance(List<String> nodes) {
        return nodes.stream()
                .map(x -> {
                    String[] strs = x.split("_");
                    return InstanceMeta.http(strs[0], Integer.valueOf(strs[1]));
                }).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {
        final TreeCache cache = TreeCache.newBuilder(client, "/"+service.toString())
                .setCacheData(true).setMaxDepth(2).build();
        cache.getListenable().addListener(
                (curator, event) -> {
                    // 有任何节点变动这里会执行
                    log.info("zk subscribe event: " + event);
                    List<InstanceMeta> nodes = fetchAll(service);
                    listener.fire(new Event(nodes));
                }
        );
        cache.start();
    }
}
