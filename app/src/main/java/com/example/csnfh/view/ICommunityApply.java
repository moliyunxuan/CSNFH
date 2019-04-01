package com.example.csnfh.view;

import com.example.csnfh.javabean.User;

import java.util.List;

public interface ICommunityApply {

        //加载更多
        void onLoadMore(List<User> list);

        //下拉刷新
        void onRefresh(List<User> list);
}
