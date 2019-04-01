package com.example.csnfh.presenter;

import com.example.csnfh.activity.MyFarmActivity;
import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.model.FarmModel;
import com.example.csnfh.model.impl.FarmModelImpl;

import java.util.List;

/**
 * 加载我加入的农场的数据
 * Created by Administrator on 2018/5/9.
 */
public class MyFarmPresenter  {


    private FarmModel mFarmModel = new FarmModel();
    private MyFarmActivity mView;



    public MyFarmPresenter(MyFarmActivity mView) {
        this.mView = mView;
    }

    public void onRefresh(){
        mFarmModel.getMyFarmItem(new FarmModelImpl.BaseListener() {
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
