package com.ly.user.controller;

import com.ly.lrpc.netty.client.Response;
import com.ly.lrpc.netty.util.ResponseUtil;
import com.ly.user.bean.User;
import com.ly.user.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController {
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
