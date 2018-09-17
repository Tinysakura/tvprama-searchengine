package com.cfh.practice.market.service;

import com.cfh.practice.market.common.ServerResponse;

/**
 * @Author: cfh
 * @Date: 2018/9/17 21:45
 * @Description:
 */
public interface UserService {
    ServerResponse<Integer> login(String name, String password);
}
