package com.ws.solr;

import org.apache.http.Header;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 类名称：UpFile2HDFS
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-09 8:26
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
public class UpFile2HDFS {

    public static void main(String args[]) {

        File file = new File(args[0]);
        String hdfsPth = args[1];

        String fileName = null;
        fileName = file.getName();
        String url = "http://10.101.240.83:50070/webhdfs/v1" + hdfsPth + "/" + fileName + "?op=CREATE&overwrite=true";
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
        System.out.println(returnAddress);

    }
}
