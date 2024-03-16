package cn.kimmking.kkrpc.demo.consumer;

import cn.kimmking.kkrpc.core.annotation.KKConsumer;
import cn.kimmking.kkrpc.core.api.RpcRequest;
import cn.kimmking.kkrpc.core.api.RpcResponse;
import cn.kimmking.kkrpc.core.consumer.ConsumerConfig;
import cn.kimmking.kkrpc.demo.api.Order;
import cn.kimmking.kkrpc.demo.api.OrderService;
import cn.kimmking.kkrpc.demo.api.User;
import cn.kimmking.kkrpc.demo.api.UserService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@RestController
@Import({ConsumerConfig.class})
public class KkrpcDemoConsumerApplication {

    @Autowired
    ApplicationContext context;

    @KKConsumer
    UserService userService;

    @KKConsumer
    OrderService orderService;

    @Autowired
    Demo2 demo2;

    public static void main(String[] args) {
        SpringApplication.run(KkrpcDemoConsumerApplication.class, args);
    }

    @RequestMapping("/hello")
    public User hello(int id) {
        return userService.findById(id);
    }

    @Bean
    public ApplicationRunner consumer_runner() {
        return x -> {

             System.out.println(" userService.getId(10) = " + userService.getId(10));

            System.out.println(" userService.getId(10f) = " +
                    userService.getId(10f));

            System.out.println(" userService.getId(new User(100,\"KK\")) = " +
                    userService.getId(new User(100,"KK")));

            User user = userService.findById(1);
            System.out.println("RPC result userService.findById(1) = " + user);

            User user1 = userService.findById(1, "hubao");
            System.out.println("RPC result userService.findById(1, \"hubao\") = " + user1);

            System.out.println(userService.getName());

            System.out.println(userService.getName(123));

            System.out.println(userService.toString());

            System.out.println(userService.getId(11));

            System.out.println(userService.getName());

            System.out.println(" ===> userService.getLongIds()");
            for (long id : userService.getLongIds()) {
                System.out.println(id);
            }

            System.out.println(" ===> userService.getLongIds()");
            for (long id : userService.getIds(new int[]{4,5,6})) {
                System.out.println(id);
            }

            //Order order = orderService.findById(2);
            //System.out.println("RPC result orderService.findById(2) = " + order);

            //demo2.test();

//            Order order404 = orderService.findById(404);
//            System.out.println("RPC result orderService.findById(2) = " + order404);

        };
    }

}
