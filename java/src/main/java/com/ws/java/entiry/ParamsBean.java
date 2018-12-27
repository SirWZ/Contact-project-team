package com.ws.java.entiry;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * <p>
 * 类名称：ParamsBean
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
public class ParamsBean {
    /**
     * q : content:环境污染
     * hl : on
     * hl.fl : content
     * sort : boost DESC
     * rows : 20
     */

    @JSONField(name = "q")
    private String q;
    @JSONField(name = "hl")
    private String hl;
    @JSONField(name = "hl.fl")
    private String _$HlFl183; // FIXME check this code
    @JSONField(name = "sort")
    private String sort;
    @JSONField(name = "rows")
    private String rows;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String get_$HlFl183() {
        return _$HlFl183;
    }

    public void set_$HlFl183(String _$HlFl183) {
        this._$HlFl183 = _$HlFl183;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }
}
