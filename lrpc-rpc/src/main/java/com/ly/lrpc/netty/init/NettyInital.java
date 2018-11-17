package com.ly.lrpc.netty.init;

import com.ly.lrpc.netty.constant.Constants;
import com.ly.lrpc.netty.factory.ZookeeperFactory;
import com.ly.lrpc.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Component
public class NettyInital implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.start();
    }

    public void start() {

        EventLoopGroup parenGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try{
            bootstrap.group(parenGroup,childGroup);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535,Delimiters.lineDelimiter()[0]));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new IdleStateHandler(6,4,2,TimeUnit.SECONDS));
                            ch.pipeline().addLast(new ServerHandler());
                            ch.pipeline().addLast(new StringEncoder());

                        }
                    });

            int port = 8081;
            ChannelFuture f = bootstrap.bind(port).sync();
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress inetAddress = InetAddress.getLocalHost();
            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(Constants.SERVER_PATH+"/"+inetAddress.getHostAddress()+"#"+port+"#");
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
            parenGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }


    }

}
