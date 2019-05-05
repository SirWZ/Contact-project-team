package com.ws.tika;


import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ApplicationTest {

    @Test
    public void test() throws IOException, TikaException, SAXException {
        FileInputStream fileInputStream = new FileInputStream("C:\\software\\github\\root\\yarn\\src\\main\\resources\\core-site.xml");

        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(1000 * 10000);
        Metadata metadata = new Metadata();


        parser.parse(fileInputStream, handler, metadata);

        System.out.println(handler.toString());


    }
}