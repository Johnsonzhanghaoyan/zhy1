package com.zhy;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class SearchIndex {

    @Test
    public void testSearch() throws Exception{
        //指定索引库存储磁盘位置
        String path =  "D:/luceneIndex";
        //创建reader对象，读取索引库索引
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));
        //创建搜索索引库核心对象
        IndexSearcher searcher = new IndexSearcher(reader);
        //指定搜索关键词
        String qName = "搜索引擎";
        //创建查询解析器，对象搜索关键词进行解析分词
        //参数1：指定Lucene版本
        //参数2：指定从哪个域字段中进行搜索
        //参数3：指定搜索时候使用哪个分词器进行查询关键词分词
        QueryParser parser = new QueryParser(Version.LUCENE_4_10_3,"title",new IKAnalyzer());
        //使用查询解析器对查询关键词进行分词
        //返回分词后包装类对象
        Query query = parser.parse(qName);
        //使用搜索核心对象查询索引库
        //返回值：文档概要信息
        //1,文档id
        //2,文档得分
        //3,文档命中总记录数
        //返回结果：返回匹配度最高的10条数据
        //什么叫做匹配度最高？ ------ 得分越高 ，匹配度越高
        TopDocs topDocs = searcher.search(query, 10);
        //文档命中总记录数
        int totalHits = topDocs.totalHits;
        System.out.println("文档命中总记录数: "+totalHits);
        //获取文档得分,文档id
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //循环文档数组，获取文档id,文档得分
        for (ScoreDoc scoreDoc : scoreDocs) {
            //获得文档得分
            float score = scoreDoc.score;
            //获得文档id
            int docId = scoreDoc.doc;

            //根据文档id获得文档
            Document document = searcher.doc(docId);

            //获得文档中的值
            String id = document.get("id");
            System.out.println("文档域中的id："+id);
            //获得文档中的标题
            String title = document.get("title");
            System.out.println("文档域中的title："+title);
            //获得文档中的描述
            String desc = document.get("desc");
            System.out.println("文档中的desc："+desc);
            //内容
            String content = document.get("content");
            System.out.println("文档中的content："+content);
        }
    }
}
