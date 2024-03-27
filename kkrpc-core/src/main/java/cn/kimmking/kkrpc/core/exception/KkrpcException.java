package cn.kimmking.kkrpc.core.exception;

import lombok.Data;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/27 19:56
 */

@Data
public class KkrpcException extends RuntimeException {

    private String Errcode;

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
}
