package cn.kimmking.kkrpc.core.meta;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 描述Provider的映射关系.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/13 20:17
 */

@Data
@Builder
public class ProviderMeta {

    Method method;
    String methodSign;
    Object serviceImpl;

}
