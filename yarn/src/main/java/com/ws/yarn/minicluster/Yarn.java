package com.ws.yarn.minicluster;

import com.google.common.io.Files;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.yarn.server.MiniYARNCluster;

import java.io.File;
import java.io.IOException;


/**
 * <p>
 * ClassName：Yarn
 * </p>
 * <p>
 * Description：Yarn
 * </p>
 * <p>
 * User：sun
 * </p>
 * <p>
 * CreateTime：2019-06-27 11:29
 * </p>
 * <p>
 * Modify：
 * </p>
 * <p>
 * ModifyTime：
 * </p>
 * <p>
 * Commont：
 * </p>
 * <p>
 * Copyright (c)
 * </p>
 *
 * @author sun
 * @version 1.0.0
 */
public class Yarn {

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
//        File tempDir = Files.createTempDir();
//        conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, tempDir.getAbsolutePath());
//        MiniDFSCluster dfsCluster = new MiniDFSCluster.Builder(conf).build();

        MiniYARNCluster yarnCluster = new MiniYARNCluster("test", 1, 1, 1);
        yarnCluster.init(conf);
        yarnCluster.start();

    }
}
