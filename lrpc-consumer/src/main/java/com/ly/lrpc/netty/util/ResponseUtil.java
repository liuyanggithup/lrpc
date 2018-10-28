package com.ly.lrpc.netty.util;

import com.ly.lrpc.netty.client.Response;

public class ResponseUtil {


    public static Response createSuccessResult(){
        return new Response();
    }

    public static Response createFailResult(String code, String msg){
         Response response = new Response();
         response.setCode(code);
         response.setMessage(msg);
         return response;
    }

    public static Response createSuccessResult(Object result){
        Response response = new Response();
        response.setResult(result);
        return response;
    }

}
