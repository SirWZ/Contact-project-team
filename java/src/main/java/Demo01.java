import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ws.java.entiry.ResponseBean;
import com.ws.java.entiry.ResponseHeaderBean;
import com.ws.java.entiry.Sus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Map;

/**
 * <p>
 * 类名称：Demo01
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-19 17:18
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
    public static void main(String args[]) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\sun\\Desktop\\实例数据.json"));
        StringBuffer buffer = new StringBuffer();
        while (reader.ready()) {
            buffer.append(reader.readLine());
        }

        Map sus = JSONObject.parseObject(buffer.toString(), Map.class);

        String response = sus.get("response").toString();
        ResponseBean responseBean = JSONObject.parseObject(response, ResponseBean.class);

        JSONObject highlighting = (JSONObject)sus.get("highlighting");
        JSONObject o = (JSONObject)highlighting.get("http://www.capitalwater.cn/");
        Object o1 = o.get("content");

        StringBuffer result = new StringBuffer();

        FileWriter writer = new FileWriter("C:\\Users\\sun\\Desktop\\result.txt");

        responseBean.getDocs().stream().parallel().forEach(i-> {
            try {
                writer.write(i.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.flush();
        writer.close();


    }


}
