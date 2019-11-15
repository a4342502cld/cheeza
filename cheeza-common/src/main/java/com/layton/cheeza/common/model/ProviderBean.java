package com.layton.cheeza.common.model;


import com.layton.cheeza.common.config.ProviderConfig;
import com.layton.cheeza.common.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 服务提供者
 */
@Slf4j
public class ProviderBean extends ProviderConfig implements InitializingBean, ApplicationContextAware, BeanNameAware, DisposableBean {

    private transient ApplicationContext applicationContext;
    private transient String beanName;


    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        export();
    }

    //初始化服务器信息
    private void init() {
        if (applicationContext != null) {
            if (getServerConfig() == null) {
                ServerConfig serverConfig = applicationContext.getBean(ServerConfig.class);
                if (serverConfig != null) {
                    setServerConfig(serverConfig);
                }
            }
        }
    }



    @Override
    public void destroy() throws Exception {
        log.info("===>destroy");
    }
}
