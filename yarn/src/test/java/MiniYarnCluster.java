
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.JarFinder;
import org.apache.hadoop.yarn.applications.ApplicationMaster;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.MiniYARNCluster;
import org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler;
import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.net.URL;

/**
 * <p>
 * 类名称：MiniYarnCluster
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-12-12 11:32
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
public class MiniYarnCluster {
    private static final Log LOG =
            LogFactory.getLog("MiniYarnCluster");

    protected MiniYARNCluster yarnCluster = null;
    protected YarnConfiguration conf = null;
    private static final int NUM_NMS = 1;

    protected final static String APPMASTER_JAR =
            JarFinder.getJar(ApplicationMaster.class);

    @Test
    public void test() throws Exception {

        LOG.info("Starting up YARN cluster");

        conf = new YarnConfiguration();
        conf.setInt(YarnConfiguration.RM_SCHEDULER_MINIMUM_ALLOCATION_MB, 128);
        conf.set("yarn.log.dir", "target");
        conf.setBoolean(YarnConfiguration.TIMELINE_SERVICE_ENABLED, true);
        conf.set(YarnConfiguration.RM_SCHEDULER, CapacityScheduler.class.getName());

        if (yarnCluster == null) {
            yarnCluster =
                    new MiniYARNCluster(MiniYarnCluster.class.getSimpleName(), 1,
                            NUM_NMS, 1, 1, true);
            yarnCluster.init(conf);

            yarnCluster.start();

            waitForNMsToRegister();

            URL url = Thread.currentThread().getContextClassLoader().getResource("yarn-site.xml");
            if (url == null) {
                throw new RuntimeException("Could not find 'yarn-site.xml' dummy file in classpath");
            }
            Configuration yarnClusterConfig = yarnCluster.getConfig();
            yarnClusterConfig.set("yarn.application.classpath", new File(url.getPath()).getParent());
            //write the document to a buffer (not directly to the file, as that
            //can cause the file being written to get read -which will then fail.
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            yarnClusterConfig.writeXml(bytesOut);
            bytesOut.close();
            //write the bytes to the file in the classpath
            OutputStream os = new FileOutputStream(new File(url.getPath()));
            os.write(bytesOut.toByteArray());
            os.close();
        }
        FileContext fsContext = FileContext.getLocalFSFileContext();

        fsContext
                .delete(
                        new Path(conf
                                .get("yarn.timeline-service.leveldb-timeline-store.path")),
                        true);
        System.in.read();
    }

    @After
    public void tearDown() throws IOException {
        if (yarnCluster != null) {
            try {
                yarnCluster.stop();
            } finally {
                yarnCluster = null;
            }
        }
        FileContext fsContext = FileContext.getLocalFSFileContext();
        fsContext
                .delete(
                        new Path(conf
                                .get("yarn.timeline-service.leveldb-timeline-store.path")),
                        true);
    }


    protected void waitForNMsToRegister() throws Exception {
        int sec = 60;
        while (sec >= 0) {
            if (yarnCluster.getResourceManager().getRMContext().getRMNodes().size()
                    >= NUM_NMS) {
                break;
            }
            Thread.sleep(1000);
            sec--;
        }
    }
}
