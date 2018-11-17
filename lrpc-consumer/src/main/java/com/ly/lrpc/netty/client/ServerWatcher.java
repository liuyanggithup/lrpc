package com.ly.lrpc.netty.client;

import com.ly.lrpc.netty.zk.ZookeeperFactory;
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
        clent.getChildren().usingWatcher(this);
        List<String> serverPaths = clent.getChildren().forPath(path);
        TcpClient.realServerPath = new HashSet<>();
        for (String serverPath:serverPaths){
            TcpClient.realServerPath.add(serverPath.split("#")[0]);
        }



    }
}
