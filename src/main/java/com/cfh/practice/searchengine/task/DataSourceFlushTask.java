package com.cfh.practice.searchengine.task;

import com.cfh.practice.searchengine.common.Consts;
import com.cfh.practice.searchengine.common.RAMDirectoryControl;
import com.cfh.practice.searchengine.util.ObjectTransferUtil;
import com.cfh.practice.searchengine.util.SearchUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: cfh
 * @Date: 2018/9/18 16:54
 * @Description: 定时询问数据源查看是否有新入库尚未建立索引的数据，对这部分数据建立索引并写入RAMDirectory
 */
public abstract class DataSourceFlushTask implements Runnable{
    //持久化磁盘的路径
    String persistenceIndexDirPath;
    IndexWriter indexWriter;
    final Logger logger = LoggerFactory.getLogger(DataSourceFlushTask.class);

    public DataSourceFlushTask(String persistenceIndexDirPath) {
        this.persistenceIndexDirPath = persistenceIndexDirPath;
    }

    public DataSourceFlushTask() {
    }

    /**
     * 更新索引
     */
    protected abstract void updateIndex();

    protected void consume(List flushList, Class clazz, Logger log) {
        //首先创建一个可以查询出所有doc的Query用于检测RAMDirectory中已经存储了多少篇doc
        Query query = new MatchAllDocsQuery();
        IndexSearcher searcher = null;

        try {
            searcher = RAMDirectoryControl.getRAMIndexSearcher();
        } catch (IOException e) {
            logger.info("e:",e.getCause());
            e.printStackTrace();
        }

        if(searcher == null){
            logger.info("searcher is null");
            return;
        }

        TopDocs topDocs = SearchUtils.getScoreDocsByPerPageAndSortField(searcher, query, 0, new Long(Consts.RAMDDirectory.RAM_LIMIT_DOC).intValue(), null);
        //从topDocs中获取到当前RAMD中的doc数
        long currentTotal = topDocs.totalHits;
        logger.info("RAMDirectory currentTotal:{}", currentTotal);

        //刷新的文档超出限制情况下的处理逻辑
        if(currentTotal + flushList.size() > Consts.RAMDDirectory.RAM_LIMIT_DOC){
            logger.info("RAMDirectory余量不足");
            long pulCount = Consts.RAMDDirectory.RAM_LIMIT_DOC - currentTotal;
            List<Document> docs = new LinkedList<>();

            if(pulCount <= 0){//内存已经撑爆的情况
                log.info("开始将RAMDirectory中的document持久化到磁盘中");
                TopDocs allDocs = SearchUtils.getScoreDocsByPerPageAndSortField(searcher, query, 0, (int)currentTotal, null);
                ScoreDoc[] scoreDocs = allDocs.scoreDocs;

                for(ScoreDoc scoreDoc : scoreDocs){
                    Document doc = null;

                    try {
                        doc = searcher.doc(scoreDoc.doc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String pollId = doc.get("id");
                    String clazz1 = doc.get("clazz");
                    //将内存中的文档删除
                    RAMDirectoryControl.deleteAppointDocument(pollId, clazz1);
                    //将内存中的文档持久化到磁盘
                    IndexWriter indexWriter = getIndexWriter();
                    if(indexWriter == null){
                        return;
                    }

                    if(doc.get("clazz").equals(clazz.getName())){
                        persistenceData(getIndexWriter(), doc);
                    }
                }
                try {
                    //一次持久化过程之后再commit而不是每写入一篇document就commit
                    getIndexWriter().commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //使用一个递归将更新的数据塞进已经进行过清理的RAMDirectory中
                consume(flushList, clazz, log);
            }else{//内存仍有余量但无法存储当次数据所有内容的情况
                List<Document> subHalfList = flushList.subList(0, (int)pulCount);
                consume(subHalfList, clazz, log);

                List<Document> rearHalfList = flushList.subList((int)pulCount, flushList.size());
                consume(rearHalfList, clazz, log);
            }
        }else{//未超出限制，直接放入RAMDirectory
            logger.info("RAMDirectory余量充足可以直接存入");
            for(Object object : flushList){
                try {
                    Document document = ObjectTransferUtil.bean2Doc(object, clazz);
                    IndexWriter rwIndexWriter = RAMDirectoryControl.getRAMIndexWriter();
                    //先删除旧的document防止重复
                    BooleanQuery.Builder builder = new BooleanQuery.Builder();
                    builder.add(new TermQuery(new Term("id", document.get("id"))), BooleanClause.Occur.MUST);
                    builder.add(new TermQuery(new Term("clazz", document.get("clazz"))), BooleanClause.Occur.MUST);
                    rwIndexWriter.deleteDocuments(builder.build());

                    //再写入新的document
                    rwIndexWriter.addDocument(document);
                    rwIndexWriter.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("bean转文档出错，未放入RAMD中");
                }
            }
        }

    }

    protected void persistenceData(IndexWriter indexWriter, Document document){
        try {
            //先删再增防止文档发生重复
            //先删除旧的document防止重复
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            builder.add(new TermQuery(new Term("id", document.get("id"))), BooleanClause.Occur.MUST);
            builder.add(new TermQuery(new Term("clazz", document.get("clazz"))), BooleanClause.Occur.MUST);
            indexWriter.deleteDocuments(builder.build());

            indexWriter.addDocument(document);
            //indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract IndexWriter getIndexWriter();

    @Override
    public void run(){
        updateIndex();
    }
}
