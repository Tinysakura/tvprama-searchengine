package com.cfh.practice.searchengine.controller;

import com.cfh.practice.searchengine.pojo.Tv;
import com.cfh.practice.searchengine.service.TvService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: cfh
 * @Date: 2018/9/19 11:10
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {
    Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TvService tvService;


    /**
     * 针对英文文档的索引搜索文档，中文文档使用可能会出现一些问题
     * @param field
     * @param content
     * @param pagePer
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/searchByIndex/{field}/{content}/{pagePer}/{pageNum}", method = RequestMethod.POST)
    public List<Tv> searchByIndex(@PathVariable(value = "field") String field, @PathVariable(value = "content") String content, @PathVariable(value = "pagePer") int pagePer, @PathVariable(value = "pageNum") int pageNum){
        Analyzer analyzer = new SmartChineseAnalyzer();
        QueryParser queryParser = new QueryParser(field, analyzer);

        Query query = null;

        try {
            query = queryParser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(query == null){
            return null;
        }

        List<Tv> searchResult = tvService.fulltexrSearchInAppointIndex(field, query, true, pagePer, pageNum);

        return  searchResult;
    }

    @RequestMapping(value = "/fullsearch")
    public List<Tv> fullSearch(){
        Query query = new MatchAllDocsQuery();

        List<Tv> searchResult = tvService.fulltexrSearchInAppointIndex("description", query, true, 1, 10000);

        return searchResult;
    }

    @RequestMapping(value = "/addData/{num}")
    public void addData(@PathVariable(value = "num") int num) throws Exception {
        for (int i = 0; i < num; i++){
            Tv tv = new Tv();
            tv.setTitle("仁医");
            tv.setDirector("平川雄一朗");
            String dateStr = "2009-11-11";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            tv.setBroadcastTime(simpleDateFormat.parse(dateStr));
            tv.setDescription("《仁医》是TBS电视台制作，改编自日本漫画家村上纪香以幕末为背景的科幻类电视剧，由导演平川雄一朗、山室大辅、川嶋龙太郎执导，大泽隆夫、中谷美纪、绫濑遥等主演。《仁医》系列日剧共有两部。" +
                    "该剧讲述了一名脑科医生穿越回古代的故事。" +
                    "《仁医》第一部于2009年10月11日起，在TBS系列的‘日曜剧场’播放， 《仁医2》于2011年4月17日起开始播放。香港无线电视（TVB）方面于2010年05月23日在无线剧集台播出第一部，2011年5月7日紧贴日本播出第二部。");
            tv.setGmtCreate(new Date());
            tv.setGmtModified(new Date());
            tv.setIsUpdate(1);

            tvService.addData(tv);
        }
    }

}
