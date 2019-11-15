package com.layton.cheeza.common.config;

import org.apache.commons.lang3.StringUtils;

public class ConsumerConfig<T> {

    /**
     * 服务接口名
     */
    private String interfaceName;

    private volatile transient T proxyInstance;

    protected volatile transient Class<?> proxyClass;

    protected T refer() {
        if (proxyInstance != null) {
            return proxyInstance;
        }
        try {
            proxyInstance = (T)getProxyClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Build consumer proxy error!", e);
        }

        return proxyInstance;
    }

    /**
     * 获取代理类
     * @return
     */
    protected Class<?> getProxyClass() {
        if (proxyClass != null) {
            return proxyClass;
        }
        try {
            if (StringUtils.isNotBlank(interfaceName)) {
                this.proxyClass = Class.forName(interfaceName);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return proxyClass;
    }

    public String getInterface() {
        return interfaceName;
    }

    public void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
