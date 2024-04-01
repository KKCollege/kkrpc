package cn.kimmking.kkrpc.core.api;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 20:48
 */

@Data
@ToString
public class RpcRequest {

    private String service; // 接口：cn.kimmking.kkrpc.demo.api.UserService
    private String methodSign;  // 方法：findById
    private Object[] args;  // 参数： 100

    // 跨调用方需要传递的参数
    private Map<String,String> params = new HashMap<>();

}
