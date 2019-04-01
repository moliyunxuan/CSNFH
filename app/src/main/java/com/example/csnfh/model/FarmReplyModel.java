package com.example.csnfh.model;

import android.util.Log;

import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.javabean.FarmReplyItem;
import com.example.csnfh.model.impl.FarmmReplyModelImpl;


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2018/5/10.
 */

public class FarmReplyModel implements FarmmReplyModelImpl {

    /**
     * 获取所有的朋友圈消息
     *
     * @param listener
     */
    public void getFarmReplyItem(FarmItem farmItem, final BaseListener listener) {
        BmobQuery<FarmReplyItem> query = new BmobQuery<>();
        query.addWhereEqualTo("community", new BmobPointer(farmItem));
        query.include("user");
        query.order("-createdAt");
        query.findObjects(new FindListener<FarmReplyItem>() {
            @Override
            public void done(List<FarmReplyItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询农场评论成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询广场失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    public void addFarmReplyItem(FarmReplyItem communityReplyItem, final BaseListener baseListener) {
        communityReplyItem.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("htht", "农场评论回复成功！！！");
                    baseListener.getSuccess(null);
                } else {
                    baseListener.getFailure();
                    Log.i("htht", "农场评论回复失败！！！");
                }
            }
        });
    }
}
