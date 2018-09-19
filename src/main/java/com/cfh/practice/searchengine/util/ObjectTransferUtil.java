package com.cfh.practice.searchengine.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import javax.print.Doc;
import java.io.File;
import java.lang.reflect.Field;

/**
 * @Author: cfh
 * @Date: 2018/9/18 22:06
 * @Description: 对象之间相互转换的util类
 */
public class ObjectTransferUtil {

    /**
     * 将指定的bean转换为document
     * @param object
     * @param clazz
     * @return
     */
    public static Document bean2Doc(Object object, Class clazz) throws Exception{
        Field[] fields = clazz.getDeclaredFields();

        Document document = new Document();

        for (Field field : fields){
            String fieldName = field.getName();
            //设置可以访问类中的私有属性，也可以通过invoke字段对应的标准get方法进行获取
            field.setAccessible(true);
            String fieldContent = field.get(object).toString();

            //当索引的长度超过多少时选择分词
            if(fieldContent.getBytes().length > 50){
                document.add(new TextField(fieldName, fieldContent, org.apache.lucene.document.Field.Store.NO));
            }else{
                document.add(new TextField(fieldName, fieldContent, org.apache.lucene.document.Field.Store.YES));
            }
        }

        document.add(new StringField("clazz", clazz.getName(), org.apache.lucene.document.Field.Store.YES));

        return document;
    }
}
