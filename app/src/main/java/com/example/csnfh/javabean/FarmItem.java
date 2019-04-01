package com.example.csnfh.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class FarmItem extends BmobObject implements Serializable {

    private BmobRelation farmApplies;            //农场申请人
    private Integer likes;
    private Boolean verify;                  //审核是否同意创建该社团
    private String farmName;
    private String farmDescription;
    private BmobFile farmIcon;
    private User farmOwner ;                     //农场所有人
    private BmobRelation farmMembers;            //农场成员

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getFarmDescription() {
        return farmDescription;
    }

    public void setFarmDescription(String farmDescription) {
        this.farmDescription = farmDescription;
    }

    public BmobFile getFarmIcon() {
        return farmIcon;
    }

    public void setFarmIcon(BmobFile farmIcon) {
        this.farmIcon = farmIcon;
    }

    public User getFarmOwner() {
        return farmOwner;
    }

    public void setFarmOwner(User farmOwner) {
        this.farmOwner = farmOwner;
    }

    public BmobRelation getFarmMembers() {
        return farmMembers;
    }

    public void setFarmMembers(BmobRelation farmMembers) {
        this.farmMembers = farmMembers;
    }

    public BmobRelation getFarmApplies() {
        return farmApplies;
    }

    public void setFarmApplies(BmobRelation farmApplies) {
        this.farmApplies = farmApplies;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }





}
