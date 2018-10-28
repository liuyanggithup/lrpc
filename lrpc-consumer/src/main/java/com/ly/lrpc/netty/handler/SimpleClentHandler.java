package com.ly.lrpc.netty.handler;


import com.alibaba.fastjson.JSONObject;
import com.ly.lrpc.netty.client.DefaultFuture;
import com.ly.lrpc.netty.client.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class SimpleClentHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if("ping".equals(msg.toString())){
            ctx.channel().writeAndFlush("ping\r\n");
            return ;
        }
        Response response = JSONObject.parseObject(msg.toString(),Response.class);
        DefaultFuture.recive(response);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}