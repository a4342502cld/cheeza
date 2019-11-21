package com.layton.cheeza.common.remote;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NettyClient {

    //存放代理类
    private final Map<Class, Object> proxyInstances = new ConcurrentHashMap();

    private String host;
    private int port;

    public void initClient(String host, int port) {
        this.host = host;
        this.port = port;
        connect();
    }

    public void connect() {

    }

    public <T> T create(Class<T> interfaceClass) {
        if (proxyInstances.containsKey(interfaceClass)) {
            return (T)proxyInstances.get(interfaceClass);
        } else {
            Object proxy = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                    new Class<?>[] {interfaceClass},
                    null);
            proxyInstances.put(interfaceClass, proxy);
            return (T)proxy;
        }
    }


}
