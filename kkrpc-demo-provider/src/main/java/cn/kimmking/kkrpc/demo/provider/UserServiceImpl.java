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

    @Override
    public User findById(int id, String name) {
        return new User(id, "KK-" + name + "_" + System.currentTimeMillis());
    }

    @Override
    public long getId(long id) {
        return id;
    }

    @Override
    public int getId(User user) {
        return user.getId();
    }

    @Override
    public String getName() {
        return "KK123";
    }

}
