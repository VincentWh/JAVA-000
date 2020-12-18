package io.kimmking.rpcfx.server;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker<T> {

    private final RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver){
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest<T> request) {
        RpcfxResponse response = new RpcfxResponse();
        Class<T> serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射 --done
        T service = resolver.resolve(serviceClass);//this.applicationContext.getBean(serviceClass);

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
