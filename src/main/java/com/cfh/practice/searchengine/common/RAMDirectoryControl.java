package com.cfh.practice.searchengine.common;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author: cfh
 * @Date: 2018/9/18 16:47
 * @Description: RAMDirectory相关控制类
 */
public class RAMDirectoryControl {
    private static RAMDirectory ramDirectory;
    private static IndexWriter indexWriter;
    Logger log = LoggerFactory.getLogger(RAMDirectoryControl.class);

//    static{
//        ramDirectory = new RAMDirectory();
//
//        indexWriter = getRAMIndexWriter();
//    }

    public RAMDirectoryControl(){
        log.info("init RAMDirectoryControl");
        ramDirectory = new RAMDirectory();

        indexWriter = getRAMIndexWriter();

        log.info("finish init RAMDirectoryControl");
    }

    public static RAMDirectory getRAMDirectory(){
        return ramDirectory;
    }

    public static IndexSearcher getRAMIndexSearcher() throws IOException{
        IndexReader reader = null;
        IndexSearcher searcher = null;
        try {
            reader = DirectoryReader.open(ramDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        searcher =  new IndexSearcher(reader);
        return searcher;
    }


    /**
     * 使用单例模式获取IndexWriter
     * @return
     */
    public static IndexWriter getRAMIndexWriter(){
        if(indexWriter == null){
            synchronized (IndexWriter.class){
                //使用中文分词器
                Analyzer analyzer = new SmartChineseAnalyzer();
                IndexWriterConfig iWConfig = new IndexWriterConfig(analyzer);
                //以追加模式写入索引
                iWConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

                try {
                    indexWriter = new IndexWriter(getRAMDirectory(), iWConfig);
                    //初始化时需要commit一次防止出现no segment异常
                    indexWriter.commit();
                    indexWriter.close();

                    /*
                      这里必须要重新new一个IndexWriterConfig，如果直接使用原先的会抛
                      Caused by: java.lang.IllegalStateException: do not share IndexWriterConfig instances across IndexWriters异常
                     */
                    iWConfig = new IndexWriterConfig(analyzer);
                    iWConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                    indexWriter = new IndexWriter(getRAMDirectory(), iWConfig);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return indexWriter;
    }

    /**
     * 根据指定的id信息删除document
     * @param id id
     * @param clazz 用于防止id相同的附加信息
     */
    public static void deleteAppointDocument(String id, String clazz){
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new TermQuery(new Term("id", id)), BooleanClause.Occur.MUST);
        builder.add(new TermQuery(new Term("clazz", clazz)), BooleanClause.Occur.MUST);
        try {
            getRAMIndexWriter().deleteDocuments(builder.build());
            getRAMIndexWriter().commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
