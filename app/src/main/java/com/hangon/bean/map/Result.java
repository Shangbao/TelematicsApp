package com.hangon.bean.map;

import java.util.List;

public class Result {
    private List<Datas> data;
    private PageInfo pageinfo;

    public PageInfo getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(PageInfo pageinfo) {
        this.pageinfo = pageinfo;
    }

    public List<Datas> getData() {
        return data;
    }

    public void setData(List<Datas> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result [data=" + data + ", pageinfo=" + pageinfo + "]";
    }
}
