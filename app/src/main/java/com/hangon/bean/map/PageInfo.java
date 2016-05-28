package com.hangon.bean.map;


public class PageInfo {
    private String pnums;
    private String current;
    private String allpage;

    public String getPnums() {
        return pnums;
    }

    public void setPnums(String pnums) {
        this.pnums = pnums;
    }


    public String getAllpage() {
        return allpage;
    }

    public void setAllpage(String allpage) {
        this.allpage = allpage;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "PageInfo [pnums=" + pnums + ", current=" + current
                + ", allpage=" + allpage + "]";
    }


}
