package cn.kimmking.kkrpc.core.meta;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Method;

/**
 * 封装服务提供者端的方法对应实现类的对应关系。
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/13 20:17
 */

@Data
@Builder
@ToString
public class ProviderMeta {

    Method method;
    String methodSign;
    Object serviceImpl;

}
