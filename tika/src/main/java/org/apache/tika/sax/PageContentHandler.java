package org.apache.tika.sax;

import org.xml.sax.ContentHandler;

import java.io.OutputStream;
import java.io.Writer;
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
public class PageContentHandler extends BodyContentHandler {

    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public PageContentHandler(ContentHandler handler) {
        super(handler);
    }

    public PageContentHandler(Writer writer) {
        super(writer);
    }

    public PageContentHandler(OutputStream stream) {
        super(stream);
    }

    public PageContentHandler(int writeLimit) {
        super(writeLimit);
    }

    public PageContentHandler() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : this.data.entrySet()) {
            builder.append(entry.getKey());
            builder.append("\r\n");
            builder.append(entry.getValue());
        }

        return builder.toString();
    }
}
