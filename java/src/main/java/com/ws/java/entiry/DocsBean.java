package com.ws.java.entiry;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * <p>
 * 类名称：DocsBean
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
public class DocsBean {
    /**
     * tstamp : ["2018-12-14T05:59:19.350Z"]
     * digest : ac6d01ffd04d4639ff7a161e1774c5e8
     * host : ["www.capitalwater.cn"]
     * boost : [1.0310167]
     * id : http://www.capitalwater.cn/
     * title : 北京首创股份有限公司
     * url : ["http://www.capitalwater.cn/"]
     * content : 北京首创股份有限公司
     * level : 1
     * host_str : ["www.capitalwater.cn"]
     * url_str : ["http://www.capitalwater.cn/"]
     * title_str : ["北京首创股份有限公司"]
     * _version_ : 1619811585179516928
     * content_str : ["北京首创股份有限公司"]
     * digest_str : ["ac6d01ffd04d4639ff7a161e1774c5e8"]
     */

    @JSONField(name = "digest")
    private String digest;
    @JSONField(name = "id")
    private String id;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "level")
    private int level;
    @JSONField(name = "_version_")
    private long version;
    @JSONField(name = "tstamp")
    private List<String> tstamp;
    @JSONField(name = "host")
    private List<String> host;
    @JSONField(name = "boost")
    private List<Double> boost;
    @JSONField(name = "url")
    private List<String> url;
    @JSONField(name = "host_str")
    private List<String> hostStr;
    @JSONField(name = "url_str")
    private List<String> urlStr;
    @JSONField(name = "title_str")
    private List<String> titleStr;
    @JSONField(name = "content_str")
    private List<String> contentStr;
    @JSONField(name = "digest_str")
    private List<String> digestStr;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<String> getTstamp() {
        return tstamp;
    }

    public void setTstamp(List<String> tstamp) {
        this.tstamp = tstamp;
    }

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
    }

    public List<Double> getBoost() {
        return boost;
    }

    public void setBoost(List<Double> boost) {
        this.boost = boost;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public List<String> getHostStr() {
        return hostStr;
    }

    public void setHostStr(List<String> hostStr) {
        this.hostStr = hostStr;
    }

    public List<String> getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(List<String> urlStr) {
        this.urlStr = urlStr;
    }

    public List<String> getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(List<String> titleStr) {
        this.titleStr = titleStr;
    }

    public List<String> getContentStr() {
        return contentStr;
    }

    public void setContentStr(List<String> contentStr) {
        this.contentStr = contentStr;
    }

    public List<String> getDigestStr() {
        return digestStr;
    }

    public void setDigestStr(List<String> digestStr) {
        this.digestStr = digestStr;
    }

    @Override
    public String toString() {
        return id+"\t"+title+"\t"+boost.get(0)+"\t"+content.replace("\n","")+"\n";
    }
}
