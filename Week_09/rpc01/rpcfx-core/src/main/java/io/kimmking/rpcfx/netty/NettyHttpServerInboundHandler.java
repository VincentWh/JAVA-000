package io.kimmking.rpcfx.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NettyHttpServerInboundHandler extends ChannelInboundHandlerAdapter {
    private ApplicationContext applicationContext;

    NettyHttpServerInboundHandler(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("Msg we receive:" + msg);
        RpcfxRequest request = JSON.parseObject((String) msg, RpcfxRequest.class);
        RpcfxResponse response = invoke(request);
        String requestJson = JSON.toJSONString(response, SerializerFeature.WriteClassName);
        ctx.writeAndFlush(requestJson).sync();
    }

    public <T> RpcfxResponse invoke(RpcfxRequest<T> request) {
        RpcfxResponse response = new RpcfxResponse();
        Class<T> serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射 --done
        T service = applicationContext.getBean(serviceClass);//this.applicationContext.getBean(serviceClass);

        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个 -- done by using FastJsonHttpMessageConverter in RpcfxServerApplication.java
            response.setResult(result);
            response.setStatus(true);
            return response;
        } catch ( IllegalAccessException | InvocationTargetException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException -- done : RpcfxException.java
            // 客户端也需要判断异常
            e.printStackTrace();
            RpcfxException rpcfxException = new RpcfxException(500, e.getMessage());
            response.setRpcfxException(rpcfxException);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
