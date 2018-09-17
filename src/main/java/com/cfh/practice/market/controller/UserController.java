package com.cfh.practice.market.controller;

import com.cfh.practice.market.common.ServerResponse;
import com.cfh.practice.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: cfh
 * @Date: 2018/9/17 22:00
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ServerResponse<Integer> login(@RequestParam("name") String name, @RequestParam("password") String password){
        return userService.login(name, password);
    }
}
