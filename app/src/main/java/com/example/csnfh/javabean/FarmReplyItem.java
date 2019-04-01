package com.example.csnfh.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class FarmReplyItem extends BmobObject implements Serializable {


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;        //评论者

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FarmItem getFarm() {
        return farm;
    }

    public void setFarm(FarmItem farm) {
        this.farm = farm;
    }

    private String content;     //评论内容
    private FarmItem farm;        //评论的农场
}
