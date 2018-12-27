package com.ws.java.entiry;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * <p>
 * 类名称：ResponseHeaderBean
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
public class ResponseHeaderBean {
    /**
     * status : 0
     * QTime : 80
     * params : {"q":"content:环境污染","hl":"on","hl.fl":"content","sort":"boost DESC","rows":"20"}
     */

    @JSONField(name = "status")
    private int status;
    @JSONField(name = "QTime")
    private int QTime;
    @JSONField(name = "params")
    private ParamsBean params;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQTime() {
        return QTime;
    }

    public void setQTime(int QTime) {
        this.QTime = QTime;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }
}
