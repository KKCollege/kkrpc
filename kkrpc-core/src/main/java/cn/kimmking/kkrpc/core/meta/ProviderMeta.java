package cn.kimmking.kkrpc.core.meta;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/13 20:17
 */

@Data
public class ProviderMeta {

    Method method;
    String methodSign;
    Object serviceImpl;

}
