package com.zhy;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;


/**
 * 需求：创建索引库
 * 搜索：
 * 前提条件：必须先有索引库数据，才能实现搜索
 */
public class CreateIndex {
    @Test
    public void testCreate() throws Exception{
        //1.指定索引库存储磁盘位置
        String path =  "D:/luceneIndex";
        //2.创建目录对象，关联索引库存储路径
        FSDirectory fsDirectory = FSDirectory.open(new File(path));
        //3.创建分词器对象
        //3.1,基本分词器
        //Analyzer analyzer = new StandardAnalyzer();

        //3.2,cjk分词器
        //Analyzer analyzer = new CJKAnalyzer();

        //3.3聪明的中国人分词器
        //Analyzer analyzer  = new SmartChineseAnalyzer();

        //3.4  ik分词器创建索引库
        Analyzer analyzer = new IKAnalyzer();

        //4.创建写索引库核心对象配置对象
            //指定使用Lucene版本
            //指定使用何种分词器  （对文档对象进行分词---产生索引单词单词词典）
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        //5.创建写索引库核心对象
        IndexWriter indexWriter  = new IndexWriter(fsDirectory,iwc);
        //模拟文档对象数据
            //文档对象数据结构： 以字段方式存储数据
            //doc(id，title,desc,contnet) 类似javabean对象
            //1,网页 抽象设计： id,title,desc,content,url
            //2,文件 抽象设计：id,title,desc,content,url
            //3,数据库数据: id,title,desc,content
        //6.创建文档对象
        Document document = new Document();
        //StringField:索引库域字段类型 类似数据库字段类型：varchar
        //特点：不分词，有索引
        //Store.YES Store.NO 是否存储 （是否在文档中存储id数据）
        //是否存储：主要看搜索结果是否需要展示
        document.add(new StringField("id","10000", Field.Store.YES));
        //TextField:索引库域字段类型
        //特点：必须分词，有索引
        //Store.YES  搜索结果必须展示标题
        document.add(new Field("title","Lucene并不是现成的搜索引擎产品，但可以用来制作搜索引擎产品,黄晓明在传智播客学习java",TextField.TYPE_STORED));
        document.add(new TextField("content","搜索引擎是全文检索技术最主要的一个应用，例如百度。搜索引擎起源于传统的信息全文检索理论",Field.Store.NO));
        document.add(new TextField("desc","全文检索系统是按照全文检索理论建立起来的用于提供全文检索服务的软件系统",Field.Store.YES));
        //写索引库
        indexWriter.addDocument(document);
        indexWriter.commit();
    }
}
