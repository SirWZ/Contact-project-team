package com.ws.solr;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类名称：Utils
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-10-10 15:56
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
public class Utils {
    public static String readPDF(String filePath) throws IOException {
        int startPager = 1;
        PdfReader pdfReader = new PdfReader(filePath);
        PdfReaderContentParser readerContentParser = new PdfReaderContentParser(pdfReader);
        int numberPages = pdfReader.getNumberOfPages();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i <= numberPages; i++) {
            String text = readerContentParser.processContent(i, new SimpleTextExtractionStrategy()).getResultantText();
            stringBuffer.append(text);
        }
        return stringBuffer.toString();
    }

}
