package com.cfh.practice.market.lucene_test;

import com.cfh.practice.searchengine.common.IndexReaderLoader;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: cfh
 * @Date: 2018/9/19 09:20
 * @Description: 测试近实时搜索
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class LRUSearchTest {
    @Autowired
    private IndexReaderLoader indexReaderLoader;

    @Test
    public void firstTest() throws Exception{
        IndexSearcher indexSearcher = indexReaderLoader.getRealTimeSearch();

        Query query = new TermQuery(new Term("title", "为了N"));

        TopDocs topDocs = indexSearcher.search(query, 10);

        for(ScoreDoc scoreDoc : topDocs.scoreDocs){
            Document doc = indexSearcher.doc(scoreDoc.doc);

            System.out.println("id:" + doc.get("id"));
            System.out.println("title" + doc.get("title"));
            System.out.println("director:" + doc.get("director"));
            System.out.println("description:" + doc.get("description"));
        }
    }
}
