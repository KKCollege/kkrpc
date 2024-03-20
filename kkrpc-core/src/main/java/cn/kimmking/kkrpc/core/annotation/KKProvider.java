package cn.kimmking.kkrpc.core.annotation;

import java.lang.annotation.*;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 20:56
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface KKProvider {
    // String version();
}
