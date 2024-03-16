package cn.kimmking.kkrpc.core.registry;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/15 11:27
 */
public class ZkRegistryCenter implements RegistryCenter {

    private CuratorFramework client = null;
    private String zkServer;
    private String namespace;

    public ZkRegistryCenter(String zkServer, String namespace) {
        this.zkServer = zkServer;
        this.namespace = namespace;
    }

    @Override
    public void start() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(this.zkServer)
                .namespace(this.namespace)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @Override
    public void stop() {
        client.close();
    }

    @Override
    public void register(String service, String instance) {
        try {
            String servicePath = "/" + service;
            // 创建服务节点
            if (client.checkExists().forPath(servicePath) == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath, "service".getBytes());
            }
            // 创建临时节点
            String instancePath = servicePath + "/" + instance;
            System.out.println(" ===> register to zk: " + instancePath);
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "provider".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unregister(String service, String instance) {

        try {
            String servicePath = "/" + service;
            // 服务节点不存在就返回
            if (client.checkExists().forPath(servicePath) == null) {
                return;
            }
            // 创建临时节点
            String instancePath = servicePath + "/" + instance;
            System.out.println(" ===> unregister to zk: " + instancePath);
            client.delete().quietly().forPath(instancePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> fetchAll(String service) {
        String servicePath = "/" + service;
        try {
            return client.getChildren().forPath(servicePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
