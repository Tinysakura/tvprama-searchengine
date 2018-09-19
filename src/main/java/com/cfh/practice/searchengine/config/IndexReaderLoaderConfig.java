package com.cfh.practice.searchengine.config;

import com.cfh.practice.searchengine.common.IndexReaderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: cfh
 * @Date: 2018/9/18 23:22
 * @Description: 配置并注入IndexReaderLoader
 */
@Configuration
public class IndexReaderLoaderConfig {
    Logger log = LoggerFactory.getLogger(IndexReaderLoaderConfig.class);

    @Bean
    public IndexReaderLoader indexReaderLoader(){
        log.info("开始注入IndexReaderLoader");

        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("lucene_index.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("出现错误，注入失败");
        }

        String indexMainPath = properties.getProperty("index_main_path");
        int coreThread = new Integer(properties.getProperty("reader_load_core_thread_size")).intValue();
        ExecutorService executorService = Executors.newFixedThreadPool(coreThread);

        log.info("IndexReaderLoader注入完成");
        return new IndexReaderLoader(indexMainPath, executorService);
    }
}
