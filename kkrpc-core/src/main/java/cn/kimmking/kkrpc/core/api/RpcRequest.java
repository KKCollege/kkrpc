package cn.kimmking.kkrpc.core.api;

import lombok.Data;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 20:48
 */

@Data
public class RpcRequest {

    private String service; // 接口：cn.kimmking.kkrpc.demo.api.UserService
    private String method;  // 方法：findById
    private Object[] args;  // 参数： 100

}
