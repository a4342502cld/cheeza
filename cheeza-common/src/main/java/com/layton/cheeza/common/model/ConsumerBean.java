package com.layton.cheeza.common.model;

import com.layton.cheeza.common.config.ConsumerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ConsumerBean<T> extends ConsumerConfig<T> implements FactoryBean, InitializingBean, DisposableBean, ApplicationContextAware, BeanNameAware {

    private transient ApplicationContext applicationContext;
    private transient String beanName;

    /**
     * 服务引用
     */
    private transient T ref;

    /**
     * 引用类型
     */
    private transient Class<?> interfaceClass;


    @Override
    public Object getObject() throws Exception {
        ref = refer();
        return ref;
    }

    @Override
    public Class<?> getObjectType() {
        try {
            interfaceClass = getProxyClass();
        } catch (Exception e) {
            interfaceClass = null;
        }
        return interfaceClass;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
