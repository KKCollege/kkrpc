package cn.kimmking.kkrpc.core.meta;

import lombok.Builder;
import lombok.Data;

/**
 * 描述服务元数据.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/20 21:30
 */

@Data
@Builder
public class ServiceMeta {

    private String app;
    private String namespace;
    private String env;
    private String name;

    public String toPath() {
        return String.format("%s_%s_%s_%s", app, namespace, env, name);
    }
}
