package com.ws.solr;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类名称：HttpClient
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-10-11 8:17
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
public class HttpClient {
    private static final CloseableHttpClient httpclient;
    public static final String CHARSET = "UTF-8";


    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).build();
        httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @return 页面内容
     */
    public static String sendGet(String url, Map<String, Object> params) throws ParseException, UnsupportedEncodingException, IOException {

        if (params != null && !params.isEmpty()) {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());

            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);
        }

        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet);

        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            response.close();
            return result;
        } else {
            return null;
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @return 页面内容
     * @throws IOException
     * @throws ClientProtocolException
     */

    public static String sendPost(String url, Map<String, Object> params) throws ClientProtocolException, IOException {

        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
        }
        HttpPost httpPost = new HttpPost(url);

        StringEntity stringEntity = new StringEntity("application/json", "utf-8");
        httpPost.setEntity(stringEntity);



        if (pairs != null && pairs.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
        }
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            response.close();
            return result;
        } else {
            return null;
        }
    }

    public static String sendPut(String url, Map<String, Object> params) throws IOException {
        if (params != null && !params.isEmpty()) {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());

            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);
        }

        HttpPut httpPut = new HttpPut(url);


        CloseableHttpResponse response = httpclient.execute(httpPut);


        int statusCode = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            response.close();
            return result;
        } else {
            return null;
        }
    }


}
