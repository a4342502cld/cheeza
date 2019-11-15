package com.layton.cheeza.common.spring.schemas;

import com.layton.cheeza.common.model.ConsumerBean;
import com.layton.cheeza.common.model.ProviderBean;
import com.layton.cheeza.common.model.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class CheezaRpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("provider", new CheezaRpcBeanDefinitionParser(ProviderBean.class, true));
        registerBeanDefinitionParser("consumer", new CheezaRpcBeanDefinitionParser(ConsumerBean.class, true));
        registerBeanDefinitionParser("server", new CheezaRpcBeanDefinitionParser(ServerBean.class, true));
    }
}
