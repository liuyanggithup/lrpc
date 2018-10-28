package com.ly.user.remote;

import com.ly.lrpc.netty.client.Response;
import com.ly.user.bean.User;

public interface UserRemote {

     Response saveUser(User user);
}
