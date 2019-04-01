package com.example.csnfh.model;

import android.util.Log;

import com.example.csnfh.javabean.EventItem;
import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.model.impl.EventModelImpl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class EventModel implements EventModelImpl {

    /**
     * 创建农场
     *
     * @param eventItem 农场活动item
     */
    public void addEventItem(final EventItem eventItem, final BaseListener listener) {
        eventItem.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("htht", "农场活动创建创建成功！！！");
                    listener.getSuccess(null);
                } else {
                    listener.getFailure();
                    Log.i("htht", "农场活动创建失败！！！");
                }
            }
        });
    }


    /**
     * 获取所有的农场消息
     *
     * @param listener
     */
    public void getEventItem(final BaseListener listener) {
        BmobQuery<EventItem> query = new BmobQuery<>();
        query.order("-eventStart");
        query.include("community");
        query.findObjects(new FindListener<EventItem>() {
            @Override
            public void done(List<EventItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询农场成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询农场失败：" + e.getMessage() + "," + e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }

    /**
     *
     * 获取某个农场的所有活动
     * @param farmItem
     * @param listener
     */
    public void getFarmEventItem(FarmItem farmItem, final BaseListener listener) {
        BmobQuery<EventItem> query = new BmobQuery<>();
        query.order("-eventStart");
        query.include("farm");
        query.addWhereEqualTo("farm",new BmobPointer(farmItem));
        query.findObjects(new FindListener<EventItem>() {
            @Override
            public void done(List<EventItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询某个农场的所有活动成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询某个农场的所有活动失败：" + e.getMessage() + "," + e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }

}
