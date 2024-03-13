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

    long getId(long id);

    long getId(User user);

    long getId(float id);

    String getName();

    String getName(int id);

    int[] getIds();
    long[] getLongIds();
    int[] getIds(int[] ids);


//    User findById(long id);

}
