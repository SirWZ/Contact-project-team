package com.ws.yarn.demo01;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync;
import org.apache.hadoop.yarn.client.api.async.NMClientAsync;
import org.apache.hadoop.yarn.util.ConverterUtils;

/**
 * <p>
 * 类名称：Application
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-12-05 8:13
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
public class Application {
    public static void main(String args[]) {
        Configuration configuration = new Configuration();
        configuration.addResource("yarn-site.xml");
        configuration.addResource("core-site.xml");

//        YarnClient yarnClient = YarnClient.createYarnClient();
//        yarnClient.init(configuration);
//        yarnClient.start();

        String hostname = NetUtils.getHostname();
        System.out.println(hostname);

    }
}
