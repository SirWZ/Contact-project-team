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
import java.io.IOException;
import java.io.PrintWriter;


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
public class De {
    public static void main(String args[]) throws IOException {
        File file = new File("hbase.json");
        PrintWriter writer = new PrintWriter(file);

        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "10.101.127.166:2181");
        HTable table = new HTable(configuration, "HUAYILIEJIE");
        Scan scan = new Scan();
        scan.setStartRow("2018-01-01 00:00".getBytes());
        scan.setStopRow("2018-01-03 00:00".getBytes());


        ResultScanner scanner = table.getScanner(scan);

        scanner.forEach(r -> {
            JSONObject object = new JSONObject();
            String value = Bytes.toString(r.getValue("fam1".getBytes(), "IP_INPUT_VALUE".getBytes()));
            String name = Bytes.toString(r.getValue("fam1".getBytes(), "IP_INPUT_NAME".getBytes()));
            String row = Bytes.toString(r.getRow());
            object.put("name", name);
            object.put("value", value);
            object.put("row", row);
            writer.write(object.toJSONString());
        });
        writer.flush();
        writer.close();


    }
}
