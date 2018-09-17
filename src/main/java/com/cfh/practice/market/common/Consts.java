package com.cfh.practice.market.common;

/**
 * @Author: cfh
 * @Date: 2018/9/17 21:49
 * @Description:
 */
public class Consts {
    /**
     * 与登录相关的状态
     */
    public interface LoginStatus{
        int loginSuccess = 1;
        int unknownName = 2;
        int uncorrectPassword = 3;
    }
}
