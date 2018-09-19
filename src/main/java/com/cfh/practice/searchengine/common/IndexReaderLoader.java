package com.cfh.practice.searchengine.common;

import com.cfh.practice.searchengine.task.HardDiskIndexReaderBuilder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Author: cfh
 * @Date: 2018/9/18 16:02
 * @Description: IndexSearcher的构建类，根据传入的参数决定是否需要开启近实时查询
 */
public class IndexReaderLoader {
    String dirPath;
    ExecutorService executorService;
    Logger log = LoggerFactory.getLogger(IndexReaderLoader.class);

    public IndexReaderLoader(String dirPath, ExecutorService executorService) {
        this.dirPath = dirPath;
        this.executorService = executorService;

        //先在所有子目录下commit一遍防止读取时出现no segments异常
        try {
            writeAndCommit(dirPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("索引目录初始化出现错误:{}",e.getCause());
        }

        loadIndexReaderFromHardDisk(dirPath, executorService);
    }

    public void writeAndCommit(String dirPath) throws Exception{
        log.info("初始化索引目录");
        File rootPath = new File(dirPath);
        File[] files = rootPath.listFiles();

        for(File file : files){
            if(file.exists() && file.isDirectory()){
                Directory directory = FSDirectory.open(Paths.get(file.getPath()));
                Analyzer analyzer = new SmartChineseAnalyzer();

                IndexWriterConfig iWConfig = new IndexWriterConfig(analyzer);
                IndexWriter indexWriter = new IndexWriter(directory, iWConfig);

                indexWriter.commit();
                indexWriter.close();
            }
        }

        log.info("索引初始化完成");
    }

    private List<IndexReader> loadIndexReaderFromHardDisk(String dirPath, ExecutorService executorService){
        List<IndexReader> readers = new ArrayList<>();

        log.info("从根路径{}下加载索引", dirPath);
        File rootPath = new File(dirPath);

        File[] files = rootPath.listFiles();
        log.info("路径{}下有{}个子目录", dirPath, files.length);
        List<Future<IndexReader>> futures = new ArrayList<>();

        for(File file : files){
            if(file.isDirectory() && file.exists()){
                Future<IndexReader> future = executorService.submit(new HardDiskIndexReaderBuilder(file.getPath()));
                futures.add(future);
            }
        }

        //异步获取读取到的索引的结果
        for(Future<IndexReader> future : futures){
            try {
                IndexReader result = future.get();
                readers.add(result);
                log.info("读取完{}条索引", result.getRefCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return readers;
    }

    /**
     * 获取非近实时的IndexSearcher
     * 这种写法每次获取索引都需要读取一遍磁盘，一般只适用于对搜索实时性特别高的情况
     * @return
     */
    public IndexSearcher getUnRealTimeSearch() {
        List<IndexReader> indexReaderList = loadIndexReaderFromHardDisk(dirPath, executorService);
        IndexReader[] indexReaders = indexReaderList.toArray(new IndexReader[indexReaderList.size()]);
        MultiReader multiReader = null;
        try {
            multiReader = new MultiReader(indexReaders);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new IndexSearcher(multiReader);
    }

    /**
     * 获取近实时的IndexSearcher
     * @return
     */
    public IndexSearcher getRealTimeSearch(){
        IndexReader reader = null;
        try {
            //使用RAMDirectory缓存的索引达到近实时查询的功能
            reader = DirectoryReader.open(RAMDirectoryControl.getRAMDirectory());
            List<IndexReader> indexReaderList = loadIndexReaderFromHardDisk(dirPath, executorService);
            indexReaderList.add(reader);
            IndexReader[] indexReaders = indexReaderList.toArray(new IndexReader[indexReaderList.size()]);
            MultiReader multiReader = new MultiReader(indexReaders);
            return new IndexSearcher(multiReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //若出现异常则降级为获取非实时的IndexSearcher
        return getUnRealTimeSearch();
    }
}
