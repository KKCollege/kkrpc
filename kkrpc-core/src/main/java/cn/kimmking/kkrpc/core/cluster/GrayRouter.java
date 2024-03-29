package cn.kimmking.kkrpc.core.cluster;

import cn.kimmking.kkrpc.core.api.Router;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 灰度路由.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/29 10:48
 */

@Slf4j
public class GrayRouter implements Router<InstanceMeta> {

    @Setter
    @Getter
    int grayRatio = -1;
    Random random = new Random();

    public GrayRouter(int grayRatio) {
        this.grayRatio = grayRatio;
    }

    @Override
    public List<InstanceMeta> route(List<InstanceMeta> providers) {

        if(grayRatio == -1) return providers;

        List<InstanceMeta> normalNodes = new ArrayList<>(); // 3
        List<InstanceMeta> grayNodes = new ArrayList<>();   // 1
        providers.forEach(p -> {
            String gray = p.getParameters().get("gray");
            if("true".equals(gray)) {
                grayNodes.add(p);
            } else {
                normalNodes.add(p);
            }
        });

        log.debug(" grayRouter grayNodes/normalNodes,grayRatio ===> {}/{},{}",
                grayNodes.size(), normalNodes.size(), grayRatio);

        if(grayNodes.isEmpty() || normalNodes.isEmpty()) return providers;

        if(grayRatio <= 0) return normalNodes;
        if(grayRatio >= 100) return grayNodes;

        if(random.nextInt(100) < grayRatio) {
            log.debug(" grayRouter grayNodes ===> {}", grayNodes);
            return grayNodes;
        } else {
            log.debug(" grayRouter normalNodes ===> {}", normalNodes);
            return normalNodes;
        }
    }
}
