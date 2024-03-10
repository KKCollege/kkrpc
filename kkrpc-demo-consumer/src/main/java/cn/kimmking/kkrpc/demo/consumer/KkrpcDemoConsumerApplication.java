package cn.kimmking.kkrpc.demo.consumer;

import cn.kimmking.kkrpc.core.annotation.KKConsumer;
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

    @Bean
    public ApplicationRunner consumer_runner() {
        return x -> {
//            User user = userService.findById(1);
//            System.out.println("RPC result userService.findById(1) = " + user);

//            System.out.println(userService.toString());
//
//            System.out.println(userService.getId(11));

            System.out.println(userService.getName());

            //Order order = orderService.findById(2);
            //System.out.println("RPC result orderService.findById(2) = " + order);

            //demo2.test();

//            Order order404 = orderService.findById(404);
//            System.out.println("RPC result orderService.findById(2) = " + order404);

        };
    }

}
