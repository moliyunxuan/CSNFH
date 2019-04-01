package com.example.csnfh.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.csnfh.R;
import com.example.csnfh.adapter.FarmAdapter;
import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.presenter.MyFarmPresenter;
import com.example.csnfh.utils.NetUtil;
import com.example.csnfh.view.IFarm;

import java.util.ArrayList;
import java.util.List;

public class MyFarmActivity extends AppCompatActivity implements IFarm {

    private SwipeRefreshLayout swipeRefreshLayout;  //刷新控件
    private RecyclerView rv_my_farm_list;
    private ImageView bImageView;       //返回
    private RelativeLayout loading;
    private LinearLayout tip;
    private MyFarmPresenter mPresenter;

    private List<FarmItem> mList = new ArrayList<>();  //临时容器
    private List<FarmItem> mFarmList;     //真正的农场数据

    private FarmAdapter farmAdapter;      //直接使用展示农场的adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);
        initView();
        initRefresh();

        //左上角返回
        bImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPresenter = new MyFarmPresenter(this);
        farmAdapter = new FarmAdapter(this, mList);
        rv_my_farm_list.setAdapter(farmAdapter);
        rv_my_farm_list.setLayoutManager(new LinearLayoutManager(this));

        if (NetUtil.checkNet(this)) {
            mPresenter.onRefresh();
        } else {
            loading.setVisibility(View.GONE);
            tip.setVisibility(View.VISIBLE);
            rv_my_farm_list.setVisibility(View.GONE);
        }

    }

    private void initRefresh() {

        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onRefresh();
            }
        });
    }

    private void initView() {

        swipeRefreshLayout = findViewById(R.id.srl_my_comm);
        rv_my_farm_list = findViewById(R.id.rv_my_community_list);
        //左上角返回图标
        bImageView = findViewById(R.id.iv_my_comm_back);

        loading = findViewById(R.id.loading);
        tip = findViewById(R.id.tip);

    }


    @Override
    public void onLoadMore(List<FarmItem> list) {
        loading.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        rv_my_farm_list.setVisibility(View.VISIBLE);

    }

    @Override
    public void onRefresh(List<FarmItem> list) {
        Log.i("htht", "我的社团  刷新完成了 ");
        mFarmList = list;
       farmAdapter.setDatas(mFarmList);
        farmAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        if (list.size() == 0) {
            loading.setVisibility(View.GONE);
            tip.setVisibility(View.VISIBLE);
            rv_my_farm_list.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
            rv_my_farm_list.setVisibility(View.VISIBLE);
        }
    }

}
