import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.solr.HttpClient;
import com.ws.solr.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 类名称：Test
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-10-11 8:07
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
public class Test {

    @org.junit.Test
    public void test01() throws IOException {
        String text = Utils.readPDF("C:\\Users\\sun\\Desktop\\可持续化发展\\杭州海康威视数字技术股份有限公司_年报.pdf");
        System.out.println(text);
    }

    @org.junit.Test
    public void testHdfs() throws IOException {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("op", "LISTSTATUS");
//
//        String sendGet = HttpClient.sendGet("http://spider01:50070/webhdfs/v1/testrest/file1", hashMap);
//        System.out.println(sendGet);
//        JSONObject liststatusJSON = JSONObject.parseObject(sendGet);
//
//        HashMap<String, Object> hashMap1 = new HashMap<>();
//        hashMap1.put("op", "OPEN");
//        hashMap1.put("length", "23083");
//
//        String sendGet1 = HttpClient.sendGet("http://spider01:50070/webhdfs/v1/testrest/file1", hashMap1);
//        System.out.println(sendGet1);
//


    }

    @org.junit.Test
    public void testPost() throws IOException {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("id", "asdasd");
        object.put("content", "nanine?");
        object.put("source","asdzxcasdcxz");


        array.add(object);
        String url = "http://spider03:8983/solr/plugin/update?commitWithin=1000&overwrite=true&wt=json";
        String json = array.toJSONString();


        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        StringEntity s = new StringEntity(json);
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");
        post.setEntity(s);

        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "utf-8");
        EntityUtils.consume(entity);
        response.close();
        System.out.println(result);

    }
}
