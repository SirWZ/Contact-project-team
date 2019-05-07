package com.zyxx;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;

/**
 * <p>
 * ClassName：Application
 * </p>
 * <p>
 * Description：${DESCRIPTION}
 * </p>
 * <p>
 * User：sun
 * </p>
 * <p>
 * CreateTime：2019-04-22 10:41
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
public class Application {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ConfigurationException, IOException {
        Logger logger = LoggerFactory.getLogger(Application.class);

        XMLConfiguration configuration = new XMLConfiguration("config.xml");
        configuration.setExpressionEngine(new XPathExpressionEngine());

        String url = configuration.getString("/dataSource[@valid='true']/properties[name='url']/value");
        String user = configuration.getString("/dataSource[@valid='true']/properties[name='user']/value");
        String paswd = configuration.getString("/dataSource[@valid='true']/properties[name='passwd']/value");

        logger.info("url:{}", url);
        logger.info("user:{}", user);
        logger.info("paswd:{}", paswd);

        if (StringUtils.isEmpty(url) || args.length < 1) {
            throw new RuntimeException("sql or url is empty");
        }
        String sql = args[0];
        logger.info("sql:{}", sql);

        String resultFilePath = configuration.getString("/result/properties[name='filePath']/value");
        resultFilePath = StringUtils.isEmpty(resultFilePath) ? System.getProperty("basedir") : resultFilePath;
        if (StringUtils.isEmpty(resultFilePath)) {
            throw new RuntimeException("resultFilePath is null");
        }
        File file = new File(resultFilePath + File.separator + "Result.txt");


        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

        Connection connection = DriverManager.getConnection(url, user, paswd);
        logger.info("连接数据库成功!");

        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        PrintWriter printWriter = new PrintWriter(new FileWriter(file, true));


        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i + 1);
                String data = columnName + ":" + resultSet.getObject(columnName).toString() + "\t";
                printWriter.write(data);
            }
            printWriter.write("\r\n");
        }
        printWriter.flush();
        printWriter.close();


    }


}
