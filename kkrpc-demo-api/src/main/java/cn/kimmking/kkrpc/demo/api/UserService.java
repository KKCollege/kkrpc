package cn.kimmking.kkrpc.demo.api;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 20:36
 */
public interface UserService {

    User findById(int id);

    User findById(int id, String name);

    int getId(int id);

    String getName();

    String getName(int id);

//    User findById(long id);

}
