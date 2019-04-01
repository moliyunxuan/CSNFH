package com.example.csnfh.javabean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class DynamicItem extends BmobObject implements Serializable {

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public List<BmobFile> getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(List<BmobFile> photoList) {
        PhotoList = photoList;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public User getWriter() {
        return Writer;
    }

    public void setWriter(User writer) {
        Writer = writer;
    }

    //作者描述
    public String Text;

    //作者上传图片集合
    public List<BmobFile> PhotoList;

    //作者描述文字
    public String Detail;

    //外键
    public User Writer;

}
