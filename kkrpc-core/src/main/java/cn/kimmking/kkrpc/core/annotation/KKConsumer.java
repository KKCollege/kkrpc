package cn.kimmking.kkrpc.core.annotation;

import java.lang.annotation.*;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 21:40
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface KKConsumer {
}
