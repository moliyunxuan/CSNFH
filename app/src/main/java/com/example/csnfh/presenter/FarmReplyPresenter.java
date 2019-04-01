package com.example.csnfh.presenter;


import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.javabean.FarmReplyItem;
import com.example.csnfh.model.FarmReplyModel;
import com.example.csnfh.model.impl.FarmmReplyModelImpl;
import com.example.csnfh.view.IFarmReply;

import java.util.List;

        /**
        * 加载农场的评论数据
        *
        */
public class FarmReplyPresenter{

    private FarmReplyModel farmReplyModel = new FarmReplyModel();
    private IFarmReply mView;

    public FarmReplyPresenter(IFarmReply mView) {
        this.mView = mView;
    }

    public void onRefresh(FarmItem FarmItem){
       farmReplyModel.getFarmReplyItem(FarmItem, new FarmReplyModel.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<FarmReplyItem> list= (List<FarmReplyItem>) o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });

    }

    public void onLoadMore(){
    }

    public void onSendReply(FarmReplyItem farmReplyItem){
       farmReplyModel.addFarmReplyItem(farmReplyItem, new FarmmReplyModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                mView.onSendFinish();
            }

            @Override
            public void getFailure() {
            }
        });
    }
}
