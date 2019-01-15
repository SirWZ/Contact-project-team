package com.ws.solr.distinct;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 类名称：Demo01
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-12-29 11:34
 * </p>
 * <p>
 * 修改人：
 * </p>
 * <p>
 * 修改时间：
 * </p>
 * <p>
 * 修改备注：
 * </p>
 * <p>
 * Copyright (c) 版权所有
 * </p>
 *
 * @version 1.0.0
 */
public class Demo01 {

    public static void main(String args[]) throws IOException, SolrServerException {


        ArrayList<String> list = new ArrayList<>();

        File file = new File("C:\\Users\\sun\\Desktop\\中国南车股份有限公司_未清洗.txt");
        PrintWriter writer = new PrintWriter(file);

        HttpSolrClient solrClient = new HttpSolrClient.Builder("http://spider03:8983/solr")
                .build();

        final Map<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("q", "host:www.crrcgc.cc");
        queryParamMap.put("rows", "6500");

        MapSolrParams queryParams = new MapSolrParams(queryParamMap);

        QueryResponse response = solrClient.query("sustainableDevelpoment", queryParams);
        SolrDocumentList documents = response.getResults();
        print("Found " + documents.getNumFound() + " documents");

        for (SolrDocument document : documents) {

            String id = (String) document.getFirstValue("id");
            String content = (String) document.getFirstValue("content");
            String title = (String) document.getFirstValue("title");
            content = content.replaceAll("[\r\n]", "");
//            double f = 0.0;
//            for (int i = 0; i < content.length(); i++) {
//                char str = content.charAt(i);
//                String s = Character.toString(str);
//                if (isChinese(s)) {
//                    f += 1.0;
//                }
//            }
//            boolean b = f / content.length() > 0.4;
//            if (!b)
//                continue;
//
//            if (list.contains(content)) {
//                continue;
//            }
            writer.println(id + "\t" + title + "\t" + content);
//            list.add(content);
        }
        writer.flush();
        writer.close();
    }


    static void print(Object obj) {
        System.out.println(obj);
    }

    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }
}
