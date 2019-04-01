package com.example.csnfh.presenter;

import com.example.csnfh.javabean.DynamicItem;
import com.example.csnfh.model.DynamicModel;
import com.example.csnfh.model.impl.DynamicModelImpl;
import com.example.csnfh.view.IDynamic;

import java.util.List;

/*
        * 加载朋友圈的数据
        * 朋友圈的Presenter
        */
public class DynamicPresenter {
    private DynamicModel mDynamicModel = new DynamicModel();
    private IDynamic mView;

    public DynamicPresenter(IDynamic mView) {
        this.mView = mView;
    }

    public void onRefresh(){
        mDynamicModel.getDynamicItem(0, DynamicModel.STATE_REFRESH, new DynamicModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<DynamicItem> list= (List<DynamicItem>) o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }

    public void onLoadMore( int currPage){
        mDynamicModel.getDynamicItem(currPage, DynamicModel.STATE_MORE, new DynamicModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<DynamicItem> list= (List<DynamicItem>) o;
                mView.onLoadMore(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }

}

