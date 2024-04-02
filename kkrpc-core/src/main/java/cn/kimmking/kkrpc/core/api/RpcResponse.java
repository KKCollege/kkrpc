package cn.kimmking.kkrpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response data for RPC call.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/6 20:49
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> {

    boolean status;  // 状态: true
    T data;   // new User
    RpcException ex;

}
