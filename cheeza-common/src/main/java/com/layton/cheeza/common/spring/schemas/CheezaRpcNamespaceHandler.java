package com.layton.cheeza.common.spring.schemas;

import com.layton.cheeza.common.model.ProviderBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class CheezaRpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("provider", new CheezaRpcBeanDefinitionParser(ProviderBean.class, true));
    }
}
