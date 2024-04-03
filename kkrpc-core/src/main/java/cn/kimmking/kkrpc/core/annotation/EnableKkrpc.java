package cn.kimmking.kkrpc.core.annotation;

import cn.kimmking.kkrpc.core.config.ConsumerConfig;
import cn.kimmking.kkrpc.core.config.ProviderConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 组合一个入口.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/2 22:49
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({ProviderConfig.class, ConsumerConfig.class})
public @interface EnableKkrpc {

}
