package com.layton.cheeza.common.spring.schemas;

import com.layton.cheeza.common.model.ProviderBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CheezaRpcBeanDefinitionParser implements BeanDefinitionParser {

    private final Class<?> beanClass;

    private final boolean required;

    public CheezaRpcBeanDefinitionParser(Class<?> beanClass, boolean required) {
        this.beanClass = beanClass;
        this.required = required;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return this.parse(element, parserContext, this.beanClass, this.required);
    }

    private BeanDefinition parse(Element element, ParserContext parserContext, Class<?> beanClass, boolean required) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");


        if (ProviderBean.class.equals(beanClass)) {
            if (StringUtils.isBlank(id) && required) {
                throw new IllegalStateException("This bean do not set spring bean id " + id);
            }

            // id肯定是必须的所以此处去掉对id是否为空的判断
            if (required) {
                if (parserContext.getRegistry().containsBeanDefinition(id)) {
                    throw new IllegalStateException("Duplicate spring bean id " + id);
                }
                parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
            }
        }

        for (Method setter : beanClass.getMethods()) {
            if (isProperty(setter, beanClass)){
                String name = setter.getName();
                String property = name.substring(3, 4)
                        .toLowerCase() + name.substring(4);
                String value = element.getAttribute(property).trim();
                Object reference = value;
                switch (property) {
                    case "interface":
                        if (StringUtils.isNotBlank(value)) {
                            beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                        }
                        break;
                    case "ref":
                        if (StringUtils.isNotBlank(value)) {
                            BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
                            if (!refBean.isSingleton()) {
                                throw new IllegalStateException("The exported service ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=" + value + " scope=singleton ...>");
                            }
                            //设置对象bean的属性
                            reference = new RuntimeBeanReference(value);
                        } else {
                            reference = null;
                        }
                        beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                        break;
                    default:
                        // 默认非空字符串只是绑定值到属性
                        if (StringUtils.isNotBlank(value)) {
                            beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                        }
                        break;
                }
            }
        }

        return beanDefinition;
    }

    private ManagedMap parseParameters(NodeList nodeList, RootBeanDefinition beanDefinition) {
        if (nodeList != null&& nodeList.getLength() > 0) {
            ManagedMap parameters = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    if ("param".equals(node.getNodeName()) || "param".equals(node.getLocalName())) {
                        if (parameters == null) {
                            parameters = new ManagedMap();
                        }
                        String fromName = ((Element)node).getAttribute("from");
                        String toName = ((Element)node).getAttribute("to");
                        parameters.put(fromName, new TypedStringValue(toName, String.class));
                    }
                }
            }
            return parameters;
        }
        return null;
    }

    private boolean isProperty(Method method, Class beanClass) {
        String methodName = method.getName();
        boolean flag = methodName.length() > 3
                && methodName.startsWith("set")
                && Modifier.isPublic(method.getModifiers())
                && method.getParameterTypes().length == 1;
        Method getter = null;
        if (flag) {
            Class<?> type = method.getParameterTypes()[0];
            try {
                getter = beanClass.getMethod("get"
                        + methodName.substring(3), new Class<?>[0]);
            } catch(NoSuchMethodException e) {
                try {
                    getter = beanClass.getMethod("is" + methodName.substring(3), new Class<?>[0]);
                } catch (NoSuchMethodException e2) {

                }
            }
        }

        return flag;
    }
}
