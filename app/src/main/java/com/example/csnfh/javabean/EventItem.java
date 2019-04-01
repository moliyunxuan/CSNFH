package com.example.csnfh.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;


/**
 * Created by liuwei on 2019/2/17.
 */
public class EventItem extends BmobObject implements Serializable {

    private FarmItem farm;   //举办社团
    private String eventName;    //活动主题
    private String eventContent;    //活动详细内容

    public FarmItem getFarm() {
        return farm;
    }

    public void setFarm(FarmItem farm) {
        this.farm = farm;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public BmobDate getEventStart() {
        return eventStart;
    }

    public void setEventStart(BmobDate eventStart) {
        this.eventStart = eventStart;
    }

    public BmobDate getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(BmobDate eventEnd) {
        this.eventEnd = eventEnd;
    }

    private String eventPlace;      //活动地点
    private BmobDate eventStart;       //活动开始时间
    private BmobDate eventEnd;      //活动结束时间

}
