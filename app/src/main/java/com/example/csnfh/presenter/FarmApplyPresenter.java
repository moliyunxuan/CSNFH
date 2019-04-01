package com.example.csnfh.presenter;

import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.javabean.User;
import com.example.csnfh.model.FarmModel;
import com.example.csnfh.model.impl.FarmModelImpl;
import com.example.csnfh.view.ICommunityApply;

import java.util.List;

public class FarmApplyPresenter {

    private FarmModel mFarmModel = new FarmModel();
    private ICommunityApply mView;

    public FarmApplyPresenter(ICommunityApply mView) {
        this.mView = mView;
    }

    public void onRefresh(FarmItem farmItem){
        mFarmModel.getFarmAppliers(farmItem, new FarmModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<User> list = (List<User>)o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }

    public void onLoadMore( int currPage){
    }
}
