package com.ly.lrpc.netty.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg.toString());
        ctx.channel().writeAndFlush("hello client!\r\n");
        ctx.channel().close();
    }

}
