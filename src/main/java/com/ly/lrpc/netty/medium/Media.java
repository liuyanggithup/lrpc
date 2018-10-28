package com.ly.lrpc.netty.medium;

import com.alibaba.fastjson.JSONObject;
import com.ly.lrpc.netty.client.Response;
import com.ly.lrpc.netty.handler.param.ServerRequest;
import io.netty.handler.codec.json.JsonObjectDecoder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Media {

    public static Map<String,BeanMethod> beanMap;

    static {
        beanMap = new HashMap<>();
    }

    private static Media m = null;

    private Media(){

    }

    public static Media newInstance(){
        if(m == null){
            m = new Media();
        }
        return m;
    }

    public Response process(ServerRequest serverRequest){

        Response result = null;
        try{
            String command = serverRequest.getCommand();
            BeanMethod beanMethod = beanMap.get(command);
            if(beanMethod == null){
                return null;
            }
            Object bean = beanMethod.getBean();
            Method method = beanMethod.getMethod();
            Class<?> paramType = method.getParameterTypes()[0];
            Object content = serverRequest.getContent();
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content), paramType);
            result = (Response)method.invoke(bean, args);
            result.setId(serverRequest.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
