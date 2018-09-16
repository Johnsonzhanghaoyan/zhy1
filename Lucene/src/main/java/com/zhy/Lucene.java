package com.zhy;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;

/**
 * 需求：创建索引库
 * 搜索：
 * 前提条件：必须先有索引库数据，才能实现搜索
 */
public class Lucene {
    @Test
    public void createIndex() throws Exception {
        //1.  指定索引文件存储位置 基于内存存储 基于磁盘存储
        //  索引文件保存在内存中
        //RAMDirectory ramDirectory = new RAMDirectory();
        //  保存在磁盘
        String path = "D:/abc";
        FSDirectory fsDirectory = FSDirectory.open(new File(path));
        //2.  创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        //3.  创建索引写入配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        //4.  创建索引写入器
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        //5.  创建索引数据
        Document document = new Document();
        //6.  给 docuemnt 添加域 文本域
        /**
         *  第一个参数：域名
         *  第二个参数：域值
         *  第三个参数：存储 存 | 不存
         *  * 存：域值保存到索引文件中
         * 不存：不会将域值保存到文件中
         */
        document.add(new IntField("id", 1, Field.Store.YES));
        document.add(new TextField("title", " 谁是最可爱的人 ", Field.Store.YES));
        document.add(new Field("content", new StringReader(" 北京欢迎您，欢迎您来百知学习 java"), TextField.TYPE_STORED));
        //6.  添加索引数据
        indexWriter.addDocument(document);
        Document document1 = new Document();
        document1.add(new IntField("id", 2, Field.Store.YES));
        document1.add(new TextField("title", " 可爱的中国 ", Field.Store.YES));
        document1.add(new Field("content", new StringReader(" 河南欢迎您，这里有河南大学一群年轻貌美的男子 "), TextField.TYPE_NOT_STORED));
        indexWriter.addDocument(document1);
        indexWriter.commit();
    }

    @Test
    public void searchIndex() throws Exception {
        //  索引文件目录
        String path = "D:/abc";
        FSDirectory fsDirectory = FSDirectory.open(new File(path));
        //  创建 Reader
        DirectoryReader directoryReader = DirectoryReader.open(fsDirectory);
        //  创建索引检索器
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        //1.  检索关键词
        //String keyword =" 爱 ";
        String keyword = "爱";
        //2.  分词技术
        //3.  检索索引
        //  第一个参数： Query  代表查询的条件 第二个参数：查几条
        // 创建一个词元对象 第一个参数：代表检索的域名 第二个参数：需要检索的词元
        Term term = new Term("title", keyword);
        //  创建 TermQuery  基于词元的查询
        TermQuery query = new TermQuery(term);
        //4.  返回结果 封装查询的结果 查询符合条件的文档数 符合条件结果
        TopDocs topDocs = indexSearcher.search(query, 8);
        System.out.println(" 总命中数： " + topDocs.totalHits);
        // socreDocs  封装了文档的编号 文档得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println(" 文档的得分： " + scoreDoc.score);
            System.out.println(" 文档的编号： " + scoreDoc.doc);
            //  通过文档编号 获取文档对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //  指定域名获取域值
            System.out.println(document.get("id") + " " + document.get("title") + " " + document.get("content"));
        }
        directoryReader.close();
    }

}
