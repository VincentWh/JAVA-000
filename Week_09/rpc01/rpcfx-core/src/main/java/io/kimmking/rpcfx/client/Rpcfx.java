package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.proxy.RpcfxInvocationHandler;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T, filters> T createFromRegistry(final Class<T> serviceClass, final String zkUrl, Router router, LoadBalancer loadBalance, Filter filter) {

        // 加filte之一

        // curator Provider list from zk
        List<String> invokers = new ArrayList<>(); // 监听zk的临时节点，根据事件更新这个list

        List<String> urls = router.route(invokers);

        String url = loadBalance.select(urls); // router, loadbalance

        return (T) create(serviceClass, url, filter);

    }

    public static <T> T create(final Class<T> serviceClass, final String url, Filter filter) {

        // 0. 替换动态代理 -> AOP
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url, filter));

    }

    public static <T> T createByUsingByteEnhanceCGLib(final Class<T> serviceClass, final String url, Filter filter) {

        // 0. 更换JDK动态代理为CGLib字节码增强
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(serviceClass);
        enhancer.setCallback(new RpcfxInvocationHandler(serviceClass, url, filter));
        return (T) enhancer.create();

    }

}
