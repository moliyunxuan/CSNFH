package com.example.csnfh.view;


import com.example.csnfh.javabean.DynamicItem;

import java.util.List;

/**
 * 作用：DynamicFragment的View接口
 */
public interface IDynamic {
    //加载更多
    void onLoadMore(List<DynamicItem> list);

    //下拉刷新
    void onRefresh(List<DynamicItem> list);
}
