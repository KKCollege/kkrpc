package cn.kimmking.kkrpc.core.cluster;

import cn.kimmking.kkrpc.core.api.LoadBalancer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/28 下午6:06
 */
class RandomLoadBalancerTest {

    int times = 10000;
    float ratio = 0.98f;
    int limit = (int) (times * ratio);
    LoadBalancer<Integer> loadBalancer = new RandomLoadBalancer<>();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void choose() {
        int[] result = new int[3];
        List<Integer> servers = List.of(0, 1, 2);
        for (int i = 0; i < 3*times; i++) {
            int index = loadBalancer.choose(servers);
            result[index] = result[index] + 1;
        }
        for (int idx : servers) {
            System.out.println( idx + " -> " + result[idx]);
            assertTrue(result[idx] > limit, "chosen ratio must > " + ratio);
        }
    }
}