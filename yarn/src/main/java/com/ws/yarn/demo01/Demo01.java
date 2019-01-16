package com.ws.yarn.demo01;

import java.io.*;

/**
 * <p>
 * 类名称：Demo01
 * </p>
 * <p>
 * 类描述：a
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：19-1-15 下午10:48
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
    public static void main(String argsp[]) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("/opt/ftp/hbase1.json")));
        long count = 0;
        while (reader.ready()) {
            count++;
            String line = reader.readLine();

        }
        System.out.println(count);

    }
}
