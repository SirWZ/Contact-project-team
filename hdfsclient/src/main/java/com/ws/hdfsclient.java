package com.ws;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;

/**
 * <p>
 * 类名称：hdfsclient
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-13 17:14
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
public class hdfsclient {
    public static void main(String args[]) throws IOException {
        Configuration conf = new Configuration();
        conf.addResource("core-site.xml");
        conf.addResource("hdfs-site.xml");
        FileSystem fileSystem = FileSystem.get(conf);
        RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/user/spark/applicationHistory"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus next = listFiles.next();
            Path path = next.getPath();
            if (!path.toString().endsWith("inprogress")) {
                fileSystem.delete(path);
            }

        }

    }
}
