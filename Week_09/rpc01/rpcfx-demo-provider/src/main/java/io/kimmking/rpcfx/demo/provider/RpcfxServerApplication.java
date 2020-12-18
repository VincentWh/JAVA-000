package io.kimmking.rpcfx.demo.provider;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

//Comment these two annotation for testing NettyServer
//@SpringBootApplication
//@RestController
public class RpcfxServerApplication {

	public static void main(String[] args) throws Exception {

		// xxx "io.kimmking.rpcfx.demo.api.UserService"

		ServiceProviderDesc desc = new ServiceProviderDesc();
		desc.setHost(InetAddress.getLocalHost().getHostAddress());
		desc.setPort(8080);
		desc.setServiceClass("io.kimmking.rpcfx.demo.api.UserService");

		// Curator.

		SpringApplication.run(RpcfxServerApplication.class, args);
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

	// 能否去掉name
	//

	// annotation


	@Bean(name = "io.kimmking.rpcfx.demo.api.UserService")
	public UserService createUserService(){
		return new UserServiceImpl();
	}

	@Bean(name = "io.kimmking.rpcfx.demo.api.OrderService")
	public OrderService createOrderService(){
		return new OrderServiceImpl();
	}

	/**
	 * 替换默认的全局Jackson 转换，避免手动转换
	 * @return HttpMessage序列化全局转换器列表
	 */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		SerializerFeature[] serializerFeatures = new SerializerFeature[]{
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteClassName
		};

		fastJsonConfig.setSerializerFeatures(serializerFeatures);
		fastJsonConfig.setCharset(StandardCharsets.UTF_8);

		fastConverter.setFastJsonConfig(fastJsonConfig);

		return new HttpMessageConverters(fastConverter);
	}

}
