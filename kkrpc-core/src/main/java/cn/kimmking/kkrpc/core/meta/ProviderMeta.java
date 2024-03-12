package cn.kimmking.kkrpc.core.meta;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/12 16:07
 */

@Data
public class ProviderMeta {

    private Object serviceImpl;
    private Method method;
    private String methodSign;

}
