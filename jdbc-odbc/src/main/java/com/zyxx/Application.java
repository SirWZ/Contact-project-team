package com.zyxx;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.lang.StringUtils;
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

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ConfigurationException {

        XMLConfiguration configuration = new XMLConfiguration("config.xml");
        configuration.setExpressionEngine(new XPathExpressionEngine());


        String url = configuration.getString("/dataSource[@valid='true']/properties[name='url']/value");
        String user = configuration.getString("/dataSource[@valid='true']/properties[name='user']/value");
        String paswd = configuration.getString("/dataSource[@valid='true']/properties[name='passwd']/value");


        if (StringUtils.isEmpty(url) || args.length < 1) {
            throw new RuntimeException("sql or url is empty");
        }
        String sql = args[0];

        String resultFilePath = configuration.getString("/result/properties[name='filePath']/value");
        resultFilePath = StringUtils.isEmpty(resultFilePath) ? System.getProperty("basedir") : resultFilePath;
        if (StringUtils.isEmpty(resultFilePath)) {
            throw new RuntimeException("resultFilePath is null");
        }


        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

        Connection connection = DriverManager.getConnection(url, user, paswd);

        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData data = resultSet.getMetaData();
        int columnCount = data.getColumnCount();

        while (resultSet.next()) {
            for (int i = 0; i < columnCount; i++) {
                String columnName = data.getColumnName(i);
                Object object = resultSet.getObject(columnName);


                System.out.println(object);
            }
        }


    }


}
