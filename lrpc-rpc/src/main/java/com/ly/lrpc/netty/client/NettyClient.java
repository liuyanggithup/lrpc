package com.ly.lrpc.netty.client;

import com.ly.lrpc.netty.handler.SimpleClentHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.util.Objects;

public class NettyClient {


    public static void main(String[] args) throws Exception{




        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,Delimiters.lineDelimiter()[0]));
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new SimpleClentHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 8081).sync();
            f.channel().writeAndFlush("hello server!");
            f.channel().writeAndFlush("\r\n");
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
            Object result = f.channel().attr(AttributeKey.valueOf("ssss")).get();
            System.out.println("收到服务器返回的数据==="+result.toString());
        }finally{
            workerGroup.shutdownGracefully();
        }


    }


}
