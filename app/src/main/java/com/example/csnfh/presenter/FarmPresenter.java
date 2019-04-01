package com.example.csnfh.presenter;


import com.example.csnfh.fragment.SquareFragment;
import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.model.FarmModel;
import com.example.csnfh.view.IFarm;

import java.util.List;

/**
 * 加载农场数据
 * Created by Administrator on 2018/5/3.
 */

public class FarmPresenter {

    private FarmModel mFarmModel = new FarmModel();
    private IFarm mView;

    public FarmPresenter(SquareFragment mView) {
        this.mView = mView;
    }

    public void onRefresh(){
        mFarmModel.getFarmItem(new FarmModel.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<FarmItem> list = (List<FarmItem>)o;
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
