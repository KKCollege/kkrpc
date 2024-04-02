package cn.kimmking.kkrpc.core.transport;

import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;
import cn.kimmking.kkrpc.core.provider.ProviderInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transport for spring boot endpoint.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/2 01:27
 */


@RestController
public class SpringBootTransport {

    @Autowired
    ProviderInvoker providerInvoker;

    @RequestMapping("/kkrpc")
    public RpcResponse<Object> invoke(@RequestBody RpcRequest request) {
        return providerInvoker.invoke(request);
    }

}
