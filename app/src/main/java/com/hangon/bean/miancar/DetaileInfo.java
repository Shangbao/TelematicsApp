package com.hangon.bean.miancar;

import com.blueware.agent.android.util.T;

/**
 * Created by mykonons on 2016/8/4.
 */
public class DetaileInfo {

    private int distance;
    private String tag;
    private String type;
    private String detail_url;
    private String overall_rating;
    private String service_rating;
    private String environment_rating;
    private String image_num;
    private String comment_num;

    public DetaileInfo(int distance, String tag, String type, String detail_url, String overall_rating, String service_rating, String environment_rating, String image_num, String comment_num) {
        this.distance = distance;
        this.tag = tag;
        this.type = type;
        this.detail_url = detail_url;
        this.overall_rating = overall_rating;
        this.service_rating = service_rating;
        this.environment_rating = environment_rating;
        this.image_num = image_num;
        this.comment_num = comment_num;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(String overall_rating) {
        this.overall_rating = overall_rating;
    }

    public String getService_rating() {
        return service_rating;
    }

    public void setService_rating(String service_rating) {
        this.service_rating = service_rating;
    }

    public String getEnvironment_rating() {
        return environment_rating;
    }

    public void setEnvironment_rating(String environment_rating) {
        this.environment_rating = environment_rating;
    }

    public String getImage_num() {
        return image_num;
    }

    public void setImage_num(String image_num) {
        this.image_num = image_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }



}
