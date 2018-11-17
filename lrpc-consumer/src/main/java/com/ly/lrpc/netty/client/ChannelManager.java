package com.ly.lrpc.netty.client;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.CopyOnWriteArrayList;

public class ChannelManager {


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

    public static ChannelFuture get(int i){

       int size = channelFutures.size();
        ChannelFuture channelFuture = null;
        if(i>size){
           channelFuture = channelFutures.get(0);
           i=1;
       }else {
            channelFuture = channelFutures.get(i);
           i++;
       }

       return channelFuture;
    }



}
