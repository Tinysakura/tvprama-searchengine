package com.cfh.practice.market.service.impl;

import com.cfh.practice.market.common.Consts;
import com.cfh.practice.market.common.ServerResponse;
import com.cfh.practice.market.dao.UserMapper;
import com.cfh.practice.market.pojo.User;
import com.cfh.practice.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: cfh
 * @Date: 2018/9/17 21:45
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse<Integer> login(String name, String password) {
        User user = userMapper.selectByName(name);
        ServerResponse serverResponse = null;

        if(user == null){
            serverResponse = ServerResponse.createErrorResponse();
            serverResponse.setData(Consts.LoginStatus.unknownName);
        }

        if(!user.getPassword().equals(password)){
            serverResponse = ServerResponse.createErrorResponse();
            serverResponse.setData(Consts.LoginStatus.uncorrectPassword);
        }else{
            serverResponse = ServerResponse.createSuccessResponse();
            serverResponse.setData(Consts.LoginStatus.loginSuccess);
        }


        return serverResponse;
    }
}
