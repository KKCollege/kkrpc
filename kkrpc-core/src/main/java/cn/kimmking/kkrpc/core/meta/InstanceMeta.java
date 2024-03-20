package cn.kimmking.kkrpc.core.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/20 21:03
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstanceMeta {

    private String scheme;
    private String host;
    private Integer port;
    private String context;

    private boolean status; // online or offline
    private Map<String, String> parameters;

    public InstanceMeta(String scheme, String host, Integer port, String context) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.context = context;
    }

    public String toPath() {
        return String.format("%s_%d", host, port);
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d/%s", scheme, host, port, context);
    }

    public static InstanceMeta http(String host, Integer port) {
        return new InstanceMeta("http", host, port, "");
    }
}
