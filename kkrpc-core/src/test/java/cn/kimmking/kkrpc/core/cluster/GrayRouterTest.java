package cn.kimmking.kkrpc.core.cluster;

import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
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
class GrayRouterTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void route_ratio_01() {
        grayTest(01);
    }

    @Test
    void route_ratio_10() {
        grayTest(10);
    }

    @Test
    void route_ratio_33() {
        grayTest(33);
    }

    @Test
    void route_ratio_75() {
        grayTest(75);
    }

    @Test
    void route_ratio_90() {
        grayTest(90);
    }

    private void grayTest(int ratio) {
        GrayRouter grayRouter = new GrayRouter(ratio);
        List<InstanceMeta> providers = mock();
        providers.get(0).getParameters().put("gray", "true");
        int[] result = new int[3];
        for (int i = 0; i < 10000; i++) {
            List<InstanceMeta> grays = grayRouter.route(providers);
            result[grays.size()] = result[grays.size()] + 1;
        }
        System.out.println(" ===>>> gray = " + result[1] + ", normal = " + result[2]);
        assertTrue(result[1] > (ratio -1) * 100);
        assertTrue(result[1] < (ratio +1) * 100);
        assertTrue(result[2] > (100- ratio -1) * 100);
        assertTrue(result[2] < (100- ratio +1) * 100);
    }

    @Test
    void route_ratio_nogray() {
        allGrayTest(false);
    }

    @Test
    void route_ratio_allgray() {
        allGrayTest(true);
    }

    @Test
    void route_ratio_00() {
        route_ratio_00_or_100(0);
    }

    @Test
    void route_ratio_100() {
        route_ratio_00_or_100(100);
    }

    @Test
    void route_ratio_negative() {
        route_ratio_00_or_100(-10);
    }

    @Test
    void route_ratio_over100() {
        route_ratio_00_or_100(200);
    }

    void route_ratio_00_or_100(int ratio) {
        if(ratio > 0 && ratio < 100) return ;
        GrayRouter grayRouter = new GrayRouter(ratio);
        List<InstanceMeta> providers = mock();
        providers.get(0).getParameters().put("gray", "true");
        int[] result = new int[3];
        for (int i = 0; i < 10000; i++) {
            List<InstanceMeta> grays = grayRouter.route(providers);
            result[grays.size()] = result[grays.size()] + 1;
        }
        System.out.println(" ===>>> gray = " + result[1] + ", normal = " + result[2]);
        assertEquals(10000, ratio <= 0 ? result[2] : result[1]);
    }

    void allGrayTest(boolean gray) {
        int ratio = 33;
        GrayRouter grayRouter = new GrayRouter(ratio);
        List<InstanceMeta> providers = mock();
        if(gray) providers.forEach(p -> p.getParameters().put("gray", "true"));
        int[] result = new int[4];
        for (int i = 0; i < 10000; i++) {
            List<InstanceMeta> grays = grayRouter.route(providers);
            result[grays.size()] = result[grays.size()] + 1;
        }
        System.out.println(" ===>>> gray = " + result[2] + ", normal = " + result[3]);
        assertEquals(10000, result[3]);
    }

    List<InstanceMeta> mock() {
        return List.of(
                InstanceMeta.http("localhost", 8081),
                InstanceMeta.http("localhost", 8082),
                InstanceMeta.http("localhost", 8083)
                );
    }
}