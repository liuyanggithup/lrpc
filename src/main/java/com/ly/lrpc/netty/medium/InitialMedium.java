package com.ly.lrpc.netty.medium;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class InitialMedium implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean.getClass().isAnnotationPresent(Controller.class)){
            System.out.println(bean.getClass().isAnnotationPresent(Controller.class));
            Method[] methods = bean.getClass().getDeclaredMethods();
            for(Method method:methods){
               String key = bean.getClass().getName()+"."+method.getName();
                Map<String, BeanMethod> beanMap = Media.beanMap;
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                beanMap.put(key,beanMethod);
            }
        }

        return bean;
    }
}
