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
 * @create 2024/4/28 下午6:18
 */
class RoundRibonLoadBalancerTest {

    int times = 10000;
    LoadBalancer<Integer> loadBalancer = new RoundRibonLoadBalancer<>();

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
            assertEquals(i%3, index, "must be RR");
        }
        for (int idx : servers) {
            System.out.println( idx + " -> " + result[idx]);
            assertEquals(times, result[idx], "chosen ratio must = " + times);
        }
    }
}