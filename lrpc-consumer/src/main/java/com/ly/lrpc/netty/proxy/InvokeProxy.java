package com.ly.lrpc.netty.proxy;

import com.ly.lrpc.netty.annotation.RemoteInvoke;
import com.ly.lrpc.netty.client.ClientRequest;
import com.ly.lrpc.netty.client.Response;
import com.ly.lrpc.netty.client.TcpClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokeProxy implements BeanPostProcessor {


    private void putMethodClass(Map<Method,Class> methodClassMap,Field field){
        Method[] methods = field.getType().getMethods();
        for (Method m:methods){
            methodClassMap.put(m,field.getType());
        }

    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field:fields){
            if(field.isAnnotationPresent(RemoteInvoke.class)){
                field.setAccessible(true);
                final Map<Method,Class> methodClassMap = new HashMap<>();
                putMethodClass(methodClassMap,field);
                Enhancer enhancer = new Enhancer();
                enhancer.setInterfaces(new Class[]{field.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        ClientRequest request = new ClientRequest();
                        request.setCommand(methodClassMap.get(method).getName()+"."+method.getName());
                        request.setContent(args[0]);
                        Response response = TcpClient.send(request);
                        return response;
                    }
                });

                try {
                    field.set(bean,enhancer.create());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
