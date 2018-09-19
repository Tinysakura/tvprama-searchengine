package com.cfh.practice.searchengine.common;

import com.cfh.practice.searchengine.task.DataSourceFlushTask;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: cfh
 * @Date: 2018/9/18 23:03
 * @Description: 开启一个定时任务定时检索数据源并进行索引的增量更新
 */
public class DataSourceFlushExecutor {
    List<DataSourceFlushTask> dataSourceFlushTaskList;
    //指定定时任务的间隔时间
    long fixRate;

    public DataSourceFlushExecutor(List<DataSourceFlushTask> dataSourceFlushTaskList, long fixRate) {
        this.dataSourceFlushTaskList = dataSourceFlushTaskList;
        this.fixRate = fixRate;

        init();
    }

    public void init(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(dataSourceFlushTaskList.size());

        for(DataSourceFlushTask task : dataSourceFlushTaskList){
            executor.scheduleAtFixedRate(task, fixRate, fixRate, TimeUnit.MILLISECONDS);
        }
    }
}
