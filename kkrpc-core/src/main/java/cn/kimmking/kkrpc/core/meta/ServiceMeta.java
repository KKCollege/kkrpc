package cn.kimmking.kkrpc.core.meta;

import lombok.Data;

/**
 * 描述一个服务。
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/19 22:05
 */

@Data
public class ServiceMeta {

    private String app;
    private String namespace;
    private String env;
    private String service;

}
