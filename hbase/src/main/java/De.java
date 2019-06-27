import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * 类名称：De
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-10 13:26
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
public class De implements Runnable {

    private String startTime;
    private String stopTime;
    private String filename;
    private CountDownLatch c;

    public De(String startTime, String stopTime, String filename, CountDownLatch c) {
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.filename = filename;
        this.c = c;
    }

    @Override
    public void run() {
        System.out.println("提取文件开始:" + filename);
        File file = new File(filename);
        final PrintWriter writer;
        try {
            writer = new PrintWriter(file);

            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", "10.101.127.166:2181,10.101.127.167:2181,10.101.127.168:2181");
            HTable table = new HTable(configuration, "YIERCHUN");
            Scan scan = new Scan(startTime.getBytes(), stopTime.getBytes());

            ResultScanner scanner = table.getScanner(scan);

            scanner.forEach(r -> {
                JSONObject object = new JSONObject();
                String value = Bytes.toString(r.getValue("fam1".getBytes(), "IP_INPUT_VALUE".getBytes()));
                String name = Bytes.toString(r.getValue("fam1".getBytes(), "IP_NAME".getBytes()));
                String row = Bytes.toString(r.getRow());
                String time = row.substring(0, 16);
                object.put("name", name);
                object.put("value", value);
                object.put("time", time);
                String[] split = time.substring(0, 9).split("-");
                writer.write(object.toJSONString() + "\n");
            });
            writer.flush();
            writer.close();
            System.out.println(filename + "完成!");
            c.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String args[]) throws IOException, InterruptedException {

        String[] arr = {
                "2019-02-01 00:00",
                "2019-03-01 00:00",
                "2019-04-01 00:00"
        };
        int threadNumber = arr.length - 1;
        final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < arr.length; i++) {
            String start = arr[i];
            String stop = arr.length - 2 == i ? arr[++i] : arr[i + 1];
            String filename = start.substring(0, 7);

            De de = new De(start, stop, "C:\\software\\github\\root\\hbase\\tmp\\" + filename + ".json", countDownLatch);
            executorService.submit(de);
        }

        countDownLatch.await();
    }
}
