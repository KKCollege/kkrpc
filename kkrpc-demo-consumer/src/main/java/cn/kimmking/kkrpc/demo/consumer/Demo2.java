package cn.kimmking.kkrpc.demo.consumer;

import cn.kimmking.kkrpc.core.annotation.KKConsumer;
import cn.kimmking.kkrpc.demo.api.User;
import cn.kimmking.kkrpc.demo.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 21:23
 */

@Component
@Slf4j
public class Demo2 {

    @KKConsumer
    UserService userService2;

    public void test() {
        User user = userService2.findById(100);
        log.info(user.toString());
    }

}
