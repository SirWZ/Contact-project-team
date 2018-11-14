import com.ws.solr.HttpClientUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类名称：HdfsRestApiDemo
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-10-16 16:01
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
public class HdfsRestApiDemo {


    @Test
    public void openFile() {

        String hdfsFilePath = "http://10.101.127.172:50070/webhdfs/v1/user/video/test/demo.txt?op=OPEN";

        HttpClientUtil.HttpResponse response = HttpClientUtil.httpGet(hdfsFilePath, null, null);

        int statusCode = response.getStatusCode();

        if (statusCode == 307) {
            String tempAddress = "";
            for (Header header : response.getHeaders()) {
                if (header.getName().equals("Location")) {
                    tempAddress = header.getValue();
                    break;
                }
            }
            System.out.println("重定向获取文件:" + tempAddress);

            HttpClientUtil.HttpResponse response1 = HttpClientUtil.httpGet(tempAddress, null, null);

            response1.getStatusCode();
        }


    }


    @Test
    public void upFile1() {
        //本地文件绝对路径
        String filePath = "C:\\Users\\sun\\Desktop\\banner2.jpg";

        File file = new File(filePath);

        String fileName = file.getName();

        String url = "http://10.101.240.83:50070/webhdfs/v1/user/sustainableDevelpoment/annex/CA9160F8617F4654AA70569839DCC5F2/banner2.jpg?op=CREATE";
        String returnAddress = null;

        HttpClientUtil.HttpResponse multipart = HttpClientUtil.httpPutFormMultipart(url, Collections.singletonList(file), null, "gbk");
        for (Header header : multipart.getHeaders()) {
            if (header.getName().equals("Location")) {
                returnAddress = header.getValue();
                break;
            }
        }
    }


    @Test
    public void upFile() {

        //本地文件绝对路径
        String filePath = "C:\\Users\\sun\\Desktop\\banner2.jpg";

        File file = new File(filePath);

        String fileName = file.getName();

        //将文件转成List集合
        List<File> files = Collections.singletonList(file);

        String url = "http://10.101.240.83:50070/webhdfs/v1/user/sustainableDevelpoment/annex/CA9160F8617F4654AA70569839DCC5F2/banner2.jpg?op=CREATE&overwrite=true";
        String address = null;
        String returnAddress = null;

        //第一次发送 成功返回307 从消息头获取文件上传地址
        HttpClientUtil.HttpResponse response = HttpClientUtil.httpPutRaw(url, "{}", null, null);
//        HttpClientUtil.HttpResponse response = HttpClientUtil.httpPutFormMultipart(url, files, null, "gbk");
        Header[] headers = response.getHeaders();

        //获取文件上传地址
        for (Header header : headers) {
            if (header.getName().equals("Location")) {
                address = header.getValue();
                break;
            }
        }


        if (address != null) {
            HttpClientUtil.HttpResponse multipart = HttpClientUtil.httpPutFormMultipart(address, files, null, "gbk");
            for (Header header : multipart.getHeaders()) {
                if (header.getName().equals("Location")) {
                    returnAddress = header.getValue();
                    break;
                }
            }
        }

        System.out.println("Hdfs file path :" + returnAddress);

    }

    @Test
    public void downloadFile() throws IOException {

        String url = "http://10.101.240.83:50070/webhdfs/v1/user/video/test/34aa2b52e9504e9ba6f08e726bec3cf3.flv?op=OPEN";

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse httpResponse = null;
        httpResponse = closeableHttpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        InputStream input = entity.getContent();

        File outputFile = new File("C:\\Users\\sun\\Desktop\\doc\\34aa2b52e9504e9ba6f08e726bec3cf3.flv");
        OutputStream outputStream = new FileOutputStream(outputFile);

        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buf)) > 0) {
            outputStream.write(buf, 0, bytesRead);
        }
        outputStream.flush();

        httpResponse.close();
        closeableHttpClient.close();


    }

}
