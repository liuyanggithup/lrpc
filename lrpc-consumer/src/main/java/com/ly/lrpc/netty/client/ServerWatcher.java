package com.ly.lrpc.netty.client;

import com.ly.lrpc.netty.zk.ZookeeperFactory;
import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.HashSet;
import java.util.List;

public class ServerWatcher implements CuratorWatcher {
    @Override
    public void process(WatchedEvent event) throws Exception {
        CuratorFramework clent = ZookeeperFactory.create();
        String path = event.getPath();
        clent.getChildren().usingWatcher(this).forPath(path);
        List<String> serverPaths = clent.getChildren().forPath(path);
        ChannelManager.realServerPath.clear();
        for (String serverPath:serverPaths){
            String[] strs = serverPath.split("#");
            ChannelManager.realServerPath.add(strs[0]+"#"+strs[1]);
        }

        ChannelManager.clear();
        for (String realServer:ChannelManager.realServerPath){
            String[] strs = realServer.split("#");
            ChannelFuture channelFuture = TcpClient.b.connect(strs[0], Integer.parseInt(strs[1]));
            ChannelManager.addChannel(channelFuture);
        }

    }
}
