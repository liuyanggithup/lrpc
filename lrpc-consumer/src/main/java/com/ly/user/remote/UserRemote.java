package com.ly.user.remote;

import com.ly.user.bean.User;
import com.ly.lrpc.netty.client.Response;

public interface UserRemote {

     Response saveUser(User user);
}
