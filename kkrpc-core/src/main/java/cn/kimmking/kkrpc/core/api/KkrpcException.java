package cn.kimmking.kkrpc.core.api;

import lombok.Data;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/27 20:18
 */

@Data
public class KkrpcException extends RuntimeException {

    private String errcode;

    public KkrpcException() {
    }

    public KkrpcException(String message) {
        super(message);
    }

    public KkrpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public KkrpcException(Throwable cause) {
        super(cause);
    }

    public KkrpcException(Throwable cause, String errcode) {
        super(cause);
        this.errcode = errcode;
    }

    // X => 技术类异常：
    // Y => 业务类异常：
    // Z => unknown, 搞不清楚，再归类到X或Y
    public static final String SocketTimeoutEx = "X001" + "-" + "http_invoke_timeout";
    public static final String NoSuchMethodEx  = "X002" + "-" + "method_not_exists";
    public static final String UnknownEx  = "Z001" + "-" + "unknown";
}
