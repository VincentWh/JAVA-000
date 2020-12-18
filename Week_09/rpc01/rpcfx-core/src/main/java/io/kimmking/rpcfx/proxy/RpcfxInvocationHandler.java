package io.kimmking.rpcfx.proxy;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.Filter;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.netty.NettyHttpClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcfxInvocationHandler<T> implements InvocationHandler, MethodInterceptor {

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private final Class<T> serviceClass;
    private final String url;
    private final Filter[] filters;

    public RpcfxInvocationHandler(Class<T> serviceClass, String url, Filter... filters) {
        this.serviceClass = serviceClass;
        this.url = url;
        this.filters = filters;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        return sendRequest(method, params);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        return sendRequest(method, params);
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class
    private Object sendRequest(Method method, Object[] params) throws Exception {
        // 加filter地方之二
        // mock == true, new Student("hubao");

        RpcfxRequest<T> request = new RpcfxRequest<>();
        request.setServiceClass(this.serviceClass);
        request.setMethod(method.getName());
        request.setParams(params);

        for (Filter filter : filters) {
            if (!filter.filter(request)) {
                return null;
            }
        }

        RpcfxResponse response = postByNettyClient(request, url);
//        RpcfxResponse response = post(request, url);

        // 加filter地方之三
        // Student.setTeacher("cuijing");

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException
        if (response.isStatus()) {
            return response.getResult();
        } else {
            System.out.println("Failed to create instance for " + serviceClass +
                    ", ExceptionCode:" + response.getRpcfxException().getCode() +
                    ", ExceptionDetails:" + response.getRpcfxException().getMessage());
            return null;
        }
    }

    private RpcfxResponse post(RpcfxRequest<T> req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println("resp json: " + respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }

    private RpcfxResponse postByNettyClient(RpcfxRequest<T> req, String url) throws Exception {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        NettyHttpClient client = new NettyHttpClient();

        String respJson = client.post(reqJson, url);
        System.out.println("resp json: " + respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
