package com.ly.lrpc.netty.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;


public class SimpleClentHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if("ping".equals(msg.toString())){
            ctx.channel().writeAndFlush("ping\r\n");
            return ;
        }
        System.out.println(msg.toString());
        ctx.channel().attr(AttributeKey.valueOf("ssss")).set(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}