import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * ClassName：GetTableRegions
 * </p>
 * <p>
 * Description：获取表格所有Region
 * </p>
 * <p>
 * User：sun
 * </p>
 * <p>
 * CreateTime：2019-05-29 16:42
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
public class GetTableRegions {

    public static void main(String[] args) throws IOException, InterruptedException {

        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "10.101.127.166:2181,10.101.127.167:2181,10.101.127.168:2181");
        HTable table = new HTable(configuration, TableName.META_TABLE_NAME);
        PrefixFilter filter = new PrefixFilter("HUAYILIEJIE".getBytes());
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        scanner.forEach(row -> {
            List<KeyValue> column = row.getColumn("info".getBytes(), "regioninfo".getBytes());
            List<Cell> cells = row.getColumnCells("info".getBytes(), "regioninfo".getBytes());

            String string = Bytes.toString(row.getRow());
            System.out.println(string);
        });


    }

}
