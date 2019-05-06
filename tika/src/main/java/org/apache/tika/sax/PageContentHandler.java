package org.apache.tika.sax;

import org.xml.sax.ContentHandler;

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ClassName：MappingBodyContentHandler
 * </p>
 * <p>
 * Description：分页Handler
 * </p>
 * <p>
 * User：sun
 * </p>
 * <p>
 * CreateTime：2019-05-05 17:09
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
public class PageContentHandler extends ContentHandlerDecorator {

    private Map<String, Object> data = new HashMap<String, Object>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data.putAll(data);
    }

    public void addDate(String k, Object v) {
        this.data.put(k, v);
    }


    public PageContentHandler(ContentHandler handler) {
        super(handler);
    }


    public PageContentHandler() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.data == null)
            return "";
        for (Map.Entry<String, Object> entry : this.data.entrySet()) {
            builder.append(entry.getKey());
            builder.append("\r\n");
            builder.append(entry.getValue());
        }

        return builder.toString();
    }
}
