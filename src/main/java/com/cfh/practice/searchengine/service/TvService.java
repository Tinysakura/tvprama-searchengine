package com.cfh.practice.searchengine.service;

import com.cfh.practice.searchengine.common.IndexReaderLoader;
import com.cfh.practice.searchengine.dao.TvMapper;
import com.cfh.practice.searchengine.pojo.Tv;
import com.cfh.practice.searchengine.util.SearchUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cfh
 * @Date: 2018/9/19 09:39
 * @Description:
 */
@Service
public class TvService {
    @Autowired
    TvMapper tvMapper;

    @Autowired
    IndexReaderLoader indexReaderLoader;

    /**
     * 刷新数据源，同时将已刷新的数据的刷新状态修改，所以需要放在一个事务中
     * @return
     */
    @Transactional
    public List<Tv> flushDataSource() {
        List<Tv> results = tvMapper.flushDataSource();

        /**
         * 改变已刷新的数据的数据状态
         */
        for (Tv tv : results) {
            tv.setIsUpdate(0);
            tvMapper.updateByPrimaryKey(tv);
        }

        return results;
    }

    public List<Tv> fulltexrSearchInAppointIndex(String field, Query query, boolean realTimeSearch, int pageNum, int pagePer){
        IndexSearcher indexSearcher = null;

        if (realTimeSearch){
            indexSearcher = indexReaderLoader.getRealTimeSearch();
        } else{
            indexSearcher = indexReaderLoader.getUnRealTimeSearch();
        }

        TopDocs topDocs = null;

        if (pageNum == 1){
            topDocs = SearchUtils.getScoreDocsByPerPageAndSortField(indexSearcher, query, 0, pagePer, null);
        } else {
            int begin = pageNum * (pagePer - 1);
            topDocs = SearchUtils.getScoreDocsByPerPageAndSortField(indexSearcher, query, begin, pageNum, null);
        }

        List<Tv> searchResult = new ArrayList<>();

        for (ScoreDoc scoreDoc : topDocs.scoreDocs){
            Document document = null;
            try {
                document = indexSearcher.doc(scoreDoc.doc);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(document == null){
                continue;
            }

            Tv tv = tvMapper.selectByPrimaryKey(new Long(document.get("id")));
            searchResult.add(tv);
        }

        return searchResult;
    }

    public void addData(Tv tv){
        tvMapper.insert(tv);
    }
}
