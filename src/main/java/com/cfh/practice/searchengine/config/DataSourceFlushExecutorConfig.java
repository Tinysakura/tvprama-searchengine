package com.cfh.practice.searchengine.config;

import com.cfh.practice.searchengine.common.DataSourceFlushExecutor;
import com.cfh.practice.searchengine.common.RAMDirectoryControl;
import com.cfh.practice.searchengine.task.DataSourceFlushTask;
import com.cfh.practice.searchengine.task.TvFlushTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author: cfh
 * @Date: 2018/9/18 23:21
 * @Description: 配置数据源刷新线程池并加入并开启定时任务
 */
@Configuration
public class DataSourceFlushExecutorConfig {
    Logger log = LoggerFactory.getLogger(DataSourceFlushExecutorConfig.class);

    @Bean
    public DataSourceFlushExecutor dataSourceFlushExecutor(TvFlushTask tvFlushTask){
        log.info("开始注入DataSourceFlushExecutor");
        List<DataSourceFlushTask> tasks = new ArrayList<>();

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("lucene_index.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("DataSourceFlushExecutor注入失败");
        }

        long flushFixRate = new Long(properties.getProperty("flush_fix_rate")).longValue();
        log.info("flush_fix_rate:{}", flushFixRate);

        tasks.add(tvFlushTask);

        log.info("DataSourceFlushExecutor注入完成");
        return new DataSourceFlushExecutor(tasks, flushFixRate);
    }

    @Bean(name = "tvFlushTask")
    public TvFlushTask tvFlushTaskConfig(){
        log.info("开始注入TvFlushTask");
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("lucene_index.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String indexTvPath = properties.getProperty("index_tv_path");
        log.info("indexTvPath:{}", indexTvPath);

        log.info("TvFlushTask注入完成");
        return new TvFlushTask(indexTvPath);
    }

    @Bean
    public RAMDirectoryControl ramDirectoryControl(){
        return new RAMDirectoryControl();
    }

}
