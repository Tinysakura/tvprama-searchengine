package com.cfh.practice.searchengine.task;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * @Author: cfh
 * @Date: 2018/9/18 15:42
 * @Description: 并行读取指定目录下的索引文件的任务类
 */
public class HardDiskIndexReaderBuilder implements Callable<IndexReader> {
    final Logger log = LoggerFactory.getLogger(HardDiskIndexReaderBuilder.class);
    //指定当前线程需要搜索的目录
    String indexPath;

    public HardDiskIndexReaderBuilder(String indexPath){
        this.indexPath = indexPath;
    }

    @Override
    public IndexReader call(){
        try {
            log.info("加载{}子目录下的索引", indexPath);
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            IndexReader indexReader = DirectoryReader.open(directory);

            log.info("{}子目录下的索引建立完成", indexPath);
            return indexReader;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
