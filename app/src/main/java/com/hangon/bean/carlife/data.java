package com.hangon.bean.carlife;

import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class data {
    private  int status;
    private  String message;
    private int total;
    private List<result> results;

    public data(int status, String messahe, int total, List<result> results) {
        this.status = status;
        this.message = messahe;
        this.total = total;
        this.results = results;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessahe() {
        return message;
    }

    public void setMessahe(String messahe) {
        this.message = messahe;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<result> getResults() {
        return results;
    }

    public void setResults(List<result> results) {
        this.results = results;
    }


}
