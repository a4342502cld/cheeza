package com.layton.cheeza.demo.provider.test;

import com.layton.cheeza.common.model.ProviderBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class HelloServiceTest implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ProviderBean providerBean = (ProviderBean)applicationContext.getBean("helloProvider");
        System.out.println("===>" + providerBean.getInterface() + ", ===>" + providerBean.getRef().getClass().getName());
    }
}
