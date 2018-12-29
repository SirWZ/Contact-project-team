package com.ws.solr.distinct;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        HttpSolrClient solrClient = new HttpSolrClient.Builder("http://spider03:8983/solr")
                .build();

        final Map<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("q", "*:*");

        MapSolrParams queryParams = new MapSolrParams(queryParamMap);

        QueryResponse response = solrClient.query("SustainableDevelpoment", queryParams);
        SolrDocumentList documents = response.getResults();
        print("Found " + documents.getNumFound() + " documents");

        for(SolrDocument document : documents) {
            final String id = (String) document.getFirstValue("id");
            final String name = (String) document.getFirstValue("name");

            print("id: " + id + "; name: " + name);
        }

    }


     static void print(Object obj) {
        System.out.println(obj);
    }
}
