package com.example.csnfh.view;

import com.example.csnfh.javabean.FarmItem;

import java.util.List;

public interface IFarm {
    //加载更多
    void onLoadMore(List<FarmItem> list);

    //下拉刷新
    void onRefresh(List<FarmItem> list);
}
