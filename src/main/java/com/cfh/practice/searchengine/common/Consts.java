package com.cfh.practice.searchengine.common;

/**
 * @Author: cfh
 * @Date: 2018/9/18 19:53
 * @Description:
 */
public class Consts {
    public interface RAMDDirectory{
        //内存中的doc数量的设置与部署机器与应用环境有关，这里为了测试设置一个较小的值
        Long RAM_LIMIT_DOC = 10L;
    }
}
