package com.cfh.practice.searchengine.task;

import com.cfh.practice.searchengine.pojo.Tv;
import com.cfh.practice.searchengine.service.TvService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: cfh
 * @Date: 2018/9/18 17:12
 * @Description: 刷新存储了tv相关信息的数据源
 */
public class TvFlushTask extends DataSourceFlushTask{
    final Logger log = LoggerFactory.getLogger(TvFlushTask.class);
    //IndexWriter indexWriter;

    //注入数据源
    @Autowired
    TvService tvService;

    public TvFlushTask(String persistenceIndexDirPath) {
        super(persistenceIndexDirPath);
    }

    @Override
    protected void updateIndex() {
        //获取数据源中新刷新的数据
        List<Tv> flushList = tvService.flushDataSource();

        if (flushList != null && flushList.size() != 0){
            log.info("数据源发现更新数据，共{}条", flushList.size());
            consume(flushList, Tv.class, log);
        }else{
            log.info("数据源无更新,当次任务结束");
        }
    }


    /**
     * 使用单例模式获取IndexWriter
     * @return
     * @throws Exception
     */
    @Override
    protected IndexWriter getIndexWriter(){
        if (indexWriter == null) {
            synchronized (TvFlushTask.class) {
                try {
                    Directory directory = FSDirectory.open(Paths.get(persistenceIndexDirPath));
                    Analyzer analyzer = new SmartChineseAnalyzer();

                    IndexWriterConfig iWConfig = new IndexWriterConfig(analyzer);
                    iWConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

                    indexWriter = new IndexWriter(directory, iWConfig);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return indexWriter;
    }
}
