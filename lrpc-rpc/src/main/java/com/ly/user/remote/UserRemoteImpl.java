package com.ly.user.remote;

import com.ly.lrpc.netty.annotation.Remote;
import com.ly.lrpc.netty.client.Response;
import com.ly.lrpc.netty.util.ResponseUtil;
import com.ly.user.bean.User;
import com.ly.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote {

    @Resource
    private UserService userService;

    public Response saveUser(User user){
        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }

    public Response saveUsers(List<User> users){

        return ResponseUtil.createSuccessResult(users);
    }


}
