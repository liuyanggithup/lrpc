package com.ly.lrpc.netty.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {

    public final static ConcurrentHashMap<Long,DefaultFuture> allDefaultFuture = new ConcurrentHashMap<>();
    final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Response response;
    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(),this);
    }


    private boolean done(){
        if(this.getResponse()!=null){
            return true;
        }
        return false;
    }

    public static void recive(Response response){
        DefaultFuture df = allDefaultFuture.get(response.getId());
        if(df != null){
            Lock lock = df.lock;
            lock.lock();
            try{
                df.setResponse(response);
                df.condition.signal();
                allDefaultFuture.remove(response.getId());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }

    public Response get(){
        lock.lock();
        try {
            while (!done()){
                condition.await();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return this.response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
