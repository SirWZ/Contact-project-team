import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.solr.HttpClientUtil;
import com.ws.solr.Utils;
import org.apache.http.Header;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 类名称：PutHDFSandSolr
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-10-12 10:31
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
public class PutHDFSandSolr {
    String[] firms = {"测试公司a", "测试公司b", "测试公司c", "测试公司d", "测试公司e", "测试公司f", "测试公司g"};
    int i = 0;
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    @Test
    public void test() throws UnsupportedEncodingException, InterruptedException {
        File file = new File("C:\\Users\\sun\\Desktop\\pdf");

        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(this::upload);

        fixedThreadPool.awaitTermination(333, TimeUnit.HOURS);

    }

    public void upload(File file) {
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String fileAddress = upHdfs(file);
                System.out.println(++i);
                System.out.println("File name is :" + file.getName());
                System.out.println("Current Thread Name :" + Thread.currentThread().getName());
                String content = null;
                try {
                    content = Utils.readPDF(file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                for (int j = 0; j < 10; j++) {
                    JSONArray array = new JSONArray();
                    for (int k = 0; k < 10; k++) {
                        Random random = new Random();
                        int anInt = random.nextInt(9);

                        JSONObject object = new JSONObject();
                        object.put("content", content);
                        object.put("level", anInt + 1);
                        object.put("firm", firms[anInt % 7]);
                        object.put("filetype", "PDF");
                        object.put("fileaddress", fileAddress);
                        array.add(object);
                    }

                    upSolr(content, fileAddress, array.toJSONString());

                }

            }
        });


    }

    public void upSolr(String content, String fileAddress, String jsonStr) {

        String url = "http://spider03:8983/solr/test/update?commitWithin=1000&overwrite=true&wt=json";

        HttpClientUtil.HttpResponse response = HttpClientUtil.httpPostRaw(url, jsonStr, null, null);
        System.out.println(response);

    }

    @Test
    public void test11() throws UnsupportedEncodingException {

        File file = new File("C:\\Users\\sun\\Desktop\\pdf\\【】大唐电信科技产业集团2017社会责任报告.PDF");
        upHdfs(file);


    }


    public String upHdfs(File file) {

        String fileName = null;
        fileName = file.getName();
        String url = "http://10.101.240.83:50070/webhdfs/v1/tmp/" + fileName + "?op=CREATE&overwrite=true";
        String address = null;
        String returnAddress = null;
        HttpClientUtil.HttpResponse response = HttpClientUtil.httpPutRaw(url, "{}", null, null);
        Header[] headers = response.getHeaders();
        for (Header header : headers) {
            if (header.getName().equals("Location")) {
                address = header.getValue();
                break;
            }
        }
        List<File> files = Collections.singletonList(file);
        if (address != null) {
            HttpClientUtil.HttpResponse multipart = HttpClientUtil.httpPutFormMultipart(address, files, null, "utf-8");
            for (Header header : multipart.getHeaders()) {
                if (header.getName().equals("Location")) {
                    returnAddress = header.getValue();
                    break;
                }
            }
            System.out.println(response);
            System.out.println(multipart);
        }
        returnAddress = returnAddress.substring(0, returnAddress.lastIndexOf("/")) + "/" + fileName;

        return returnAddress;
    }

    public String upHdfds1(File file) {
        String fileName = null;
        fileName = file.getName();
        String url = "http://10.101.240.83:50070/webhdfs/v1/user/tmp/a.PDF?op=CREATE&overwrite=true";
        List<File> files = Collections.singletonList(file);
        HttpClientUtil.HttpResponse multipart = HttpClientUtil.httpPutFormMultipart(url, files, null, "gbk");

        return "";
    }

}
