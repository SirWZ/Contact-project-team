package com.ws.java.entiry;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * <p>
 * 类名称：ResponseBean
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-12-14 16:35
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
public class ResponseBean {
    /**
     * numFound : 109
     * start : 0
     * docs : [{"tstamp":["2018-12-14T05:59:19.350Z"],"digest":"ac6d01ffd04d4639ff7a161e1774c5e8","host":["www.capitalwater.cn"],"boost":[1.0310167],"id":"http://www.capitalwater.cn/","title":"北京首创股份有限公司","url":["http://www.capitalwater.cn/"],"content":"北京首创股份有限公司","level":1,"host_str":["www.capitalwater.cn"],"url_str":["http://www.capitalwater.cn/"],"title_str":["北京首创股份有限公司"],"_version_":1619811585179516928,"content_str":["北京首创股份有限公司"],"digest_str":["ac6d01ffd04d4639ff7a161e1774c5e8"]}]
     */

    @JSONField(name = "numFound")
    private int numFound;
    @JSONField(name = "start")
    private int start;
    @JSONField(name = "docs")
    private List<DocsBean> docs;

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<DocsBean> getDocs() {
        return docs;
    }

    public void setDocs(List<DocsBean> docs) {
        this.docs = docs;
    }
}
