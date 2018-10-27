package com.ly.lrpc.netty.handler;


import com.alibaba.fastjson.JSONObject;
import com.ly.lrpc.netty.client.Response;
import com.ly.lrpc.netty.handler.param.ServerRequest;
import com.ly.lrpc.netty.medium.Media;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ServerRequest serverRequest = JSONObject.parseObject(msg.toString(), ServerRequest.class);

        Media media = Media.newInstance();
        Object result = media.process(serverRequest);

        System.out.println(serverRequest.getContent());
        Response response = new Response();
        response.setId(serverRequest.getId());
        response.setResult("is ok");
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
        ctx.channel().writeAndFlush("\r\n");

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state().equals(IdleState.READER_IDLE)){
                System.out.println("读空闲---");
                ctx.channel().close();
            }else if(event.state().equals(IdleState.WRITER_IDLE)){
                System.out.println("写空闲---");
            }else if(event.state().equals(IdleState.ALL_IDLE)){
                System.out.println("读写空闲---");
                ctx.channel().writeAndFlush("ping\r\n");
            }
        }
    }
}
