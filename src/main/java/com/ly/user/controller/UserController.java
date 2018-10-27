package com.ly.user.controller;

import com.ly.user.bean.User;
import com.ly.user.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserController {
    @Resource
    private UserService userService;

    public void saveUser(User user){
        userService.save(user);
    }


}
