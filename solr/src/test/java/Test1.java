import com.ws.solr.HttpClientUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 * 类名称：Test1
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-10-12 8:49
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
public class Test1 {
    @Test
    public void test() {
//        HttpClientUtil.HttpResponse response = HttpClientUtil.httpPutRaw("http://10.101.240.83:50070/webhdfs/v1/testrest/file2?op=CREATE", "{}", null, null);

        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("op", "CREATE");
        parameter.put("namenoderpcaddress", "spider01:8020");
        parameter.put("createflag", "");
        parameter.put("createparent", "true");
        parameter.put("overwrite", "false");

        ArrayList<File> files = new ArrayList<>();
        File file = new File("C:\\Users\\sun\\Desktop\\doc\\hadoop\\index.html");
        files.add(file);

        HttpClientUtil.HttpResponse response = HttpClientUtil.httpPutFormMultipart("http://spider03:50075/webhdfs/v1/testrest/file2?op=CREATE&namenoderpcaddress=spider01:8020&createflag=&createparent=true&overwrite=false", files, null, null);


        String a = "";
        System.out.println(response);

    }
}
