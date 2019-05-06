package com.ws.tika;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.PageContentHandler;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;

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
 * CreateTime：2019-04-04 8:40
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

    public static void main(String[] args) throws IOException, TikaException, SAXException {
        TikaConfig config = new TikaConfig("C:\\software\\github\\root\\tika\\src\\main\\resources\\tika-config.xml");


        FileInputStream inputStream = new FileInputStream("C:\\Users\\sun\\Desktop\\杂七杂八\\Google三篇论文\\Google-MapReduce中文版_1.0.pdf");

        String content = "";
        AutoDetectParser parser = new AutoDetectParser(config);
        PageContentHandler handler = new PageContentHandler();
        BodyContentHandler handlerBody = new BodyContentHandler(10000 * 10000);
        Metadata metadata = new Metadata();

        try {

            parser.parse(inputStream, handler, metadata);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        content += handler.toString();
        content += metadata.toString();
        System.out.println(content);
    }

}
