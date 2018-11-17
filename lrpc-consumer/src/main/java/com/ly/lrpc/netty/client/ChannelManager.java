package com.ly.lrpc.netty.client;

import io.netty.channel.ChannelFuture;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelManager {
    static Set<String> realServerPath = new HashSet<>();
static AtomicInteger position = new AtomicInteger(0);
   public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();

   public static void removeChannel(ChannelFuture channelFuture){
       channelFutures.remove(channelFuture);
   }
    public static void addChannel(ChannelFuture channelFuture){
        channelFutures.add(channelFuture);
    }
    public static void clear(){
        channelFutures.clear();
    }

    public static ChannelFuture get(AtomicInteger i){

       int size = channelFutures.size();
        ChannelFuture channelFuture = null;
        if(i.get()>size){
           channelFuture = channelFutures.get(0);
           ChannelManager.position = new AtomicInteger(1);
       }else {
            channelFuture = channelFutures.get(i.get());
            ChannelManager.position = new AtomicInteger(i.incrementAndGet());
       }

       return channelFuture;
    }



}
