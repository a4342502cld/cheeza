package com.layton.cheeza.common.model;

import com.layton.cheeza.common.config.ServerConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Data
@Slf4j
public class ServerBean extends ServerConfig implements InitializingBean, DisposableBean, BeanNameAware {

    private transient String beanName;

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("server started with beanName {}", beanName);
    }

    @Override
    public void destroy() throws Exception {
        log.info("Stop server with beanName {}", beanName);
    }
}
