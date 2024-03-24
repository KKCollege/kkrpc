package cn.kimmking.kkrpc.core.test;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/24 20:04
 */
public class EmbeddedZookeeper {

    final String connectString;

    File snapshotDir;
    File logDir;
    ZooKeeperServer zookeeper;
    org.apache.zookeeper.server.NIOServerCnxnFactory factory;
    public ZkClient client;

    public EmbeddedZookeeper(String connectString) {
        try {
            this.connectString = connectString;
            snapshotDir = new File("~/sslog");
            logDir = new File("~/log");
            zookeeper = new ZooKeeperServer(snapshotDir, logDir, 3000);
            int port = Integer.valueOf(connectString.split(":")[1]);
            factory = new NIOServerCnxnFactory();
            factory.reconfigure(new InetSocketAddress("127.0.0.1", port));
            factory.startup(zookeeper);
            //client = new ZkClient(connectString);
            //client.setZkSerializer(ZKStringSerializer.getInstance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        factory.shutdown();
        //client.close();
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

