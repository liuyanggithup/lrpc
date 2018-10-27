package com.ly.lrpc.netty.server;

import com.ly.lrpc.netty.constant.Constants;
import com.ly.lrpc.netty.factory.ZookeeperFactory;
import com.ly.lrpc.netty.handler.SimpleServerHandle;
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
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;

public class NettyServer {

    public static void main(String[] args) {



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
                            ch.pipeline().addLast(new SimpleServerHandle());
                            ch.pipeline().addLast(new StringEncoder());

                        }
                    });

            ChannelFuture f = bootstrap.bind(8081).sync();
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress inetAddress = InetAddress.getLocalHost();
            client.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(Constants.SERVER_PATH+inetAddress.getHostAddress());
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
