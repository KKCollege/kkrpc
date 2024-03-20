package cn.kimmking.kkrpc.core.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 描述一个服务提供者实例。
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/19 22:06
 */

@Data
@NoArgsConstructor
public class InstanceMeta {

    private String scheme;
    private String host;
    private Integer port;
    private String context;

    private boolean status; // online / offline
    private Map<String, String> parameters;

    public InstanceMeta(String scheme, String host, int port, String context) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.context = context;
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d/%s", scheme, host, port, context);
    }

    public String toPath() {
        return String.format("%s_%d", host, port);
    }

    public static InstanceMeta http(String host, Integer port) {
        return new InstanceMeta("http", host,port, "");
    }
}
