package io.kimmking.rpcfx.demo.provider;


import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.netty.NettyHttpServer;
import io.kimmking.rpcfx.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
public class NettyServerApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(NettyServerApplication.class, args);
        NettyHttpServer nettyHttpServer = new NettyHttpServer(applicationContext);
        try {
            nettyHttpServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nettyHttpServer.destroy();
        }

    }


    @Autowired
    RpcfxInvoker invoker;

    @PostMapping("/")
    public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
        System.out.println("The request is:" + request);
        return invoker.invoke(request);
    }

    @Bean
    public RpcfxInvoker createInvoker(@Autowired RpcfxResolver resolver){
        return new RpcfxInvoker(resolver);
    }

    @Bean
    public RpcfxResolver createResolver(){
        return new DemoResolver();
    }

    @Bean(name = "io.kimmking.rpcfx.demo.api.UserService")
    public UserService createUserService(){
        return new UserServiceImpl();
    }

    @Bean(name = "io.kimmking.rpcfx.demo.api.OrderService")
    public OrderService createOrderService(){
        return new OrderServiceImpl();
    }

}
