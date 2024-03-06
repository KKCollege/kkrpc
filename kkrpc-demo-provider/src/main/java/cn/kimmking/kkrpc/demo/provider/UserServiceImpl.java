package cn.kimmking.kkrpc.demo.provider;

import cn.kimmking.kkrpc.core.annotation.KKProvider;
import cn.kimmking.kkrpc.demo.api.User;
import cn.kimmking.kkrpc.demo.api.UserService;
import org.springframework.stereotype.Component;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 20:41
 */

@Component
@KKProvider
public class UserServiceImpl implements UserService {
    @Override
    public User findById(int id) {
        return new User(id, "KK-" + System.currentTimeMillis());
    }
}
