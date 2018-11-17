package com.ly.lrpc.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.ly.lrpc.netty.handler.SimpleClentHandler;
import com.ly.lrpc.netty.zk.Constants;
import com.ly.lrpc.netty.zk.ZookeeperFactory;
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
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.Watcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TcpClient {


    static final Bootstrap b = new Bootstrap();
    static Set<String> realServerPath = new HashSet<>();
    static ChannelFuture f = null;
    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
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

        CuratorFramework clent = ZookeeperFactory.create();
        String host = "localhost";
        int port = 8080;
        try{
            List<String> serverPaths = clent.getChildren().forPath(Constants.SERVER_PATH);
            CuratorWatcher watcher = new ServerWatcher();
            clent.getChildren().usingWatcher(watcher).forPath(Constants.SERVER_PATH);
            for (String serverPath:serverPaths){
                realServerPath.add(serverPath.split("#")[0]);
            }

            if(realServerPath.size()>0){
                host = realServerPath.toArray()[0].toString();
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        // Start the client.
        try {
            f = b.connect(host, 8081).sync();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static Response send(ClientRequest request){
        f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush("\r\n");
        DefaultFuture df = new DefaultFuture(request);
        return df.get();
    }

    public static void main(String[] args) throws Exception{






    }


}
