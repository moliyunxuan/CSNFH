package com.example.csnfh.view;

import com.example.csnfh.javabean.FarmReplyItem;

import java.util.List;

public interface IFarmReply {

    //加载更多
    void onLoadMore(List<FarmReplyItem> list);

    //下拉刷新
    void onRefresh(List<FarmReplyItem> list);

    //下拉刷新
    void onSendFinish();
}
