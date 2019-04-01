package com.example.csnfh.view;

import com.example.csnfh.javabean.EventItem;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public interface IEvent {
    //加载更多
    void onLoadMore(List<EventItem> list);

    //下拉刷新
    void onRefresh(List<EventItem> list);

}
