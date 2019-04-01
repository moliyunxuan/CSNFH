package com.example.csnfh.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csnfh.R;
import com.example.csnfh.activity.CreateFarmActivity;
import com.example.csnfh.activity.MyFarmActivity;
import com.example.csnfh.adapter.FarmAdapter;
import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.presenter.FarmPresenter;
import com.example.csnfh.utils.NetUtil;
import com.example.csnfh.view.IFarm;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SquareFragment extends Fragment implements IFarm {

    private SwipeRefreshLayout swipeRefreshLayout;  //刷新控件
    private RecyclerView rv_farm_list;
    private FarmAdapter farmAdapter;
    private FarmPresenter mPresenter;
    private List<FarmItem> mList = new ArrayList<>();  //临时容器
    private List<FarmItem> mFarmList;     //真正的农场数据
    private FloatingActionButton menu_add_comm;
    private FloatingActionButton menu_my_comm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_square, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRefresh();

        mPresenter = new FarmPresenter(this);
        farmAdapter = new FarmAdapter(getContext(), mList);

        rv_farm_list.setAdapter(farmAdapter);
        rv_farm_list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (NetUtil.checkNet(getActivity())) {
            mPresenter.onRefresh();
        }
    }

    private void initView() {
        rv_farm_list = getActivity().findViewById(R.id.rv_community_list);
        menu_add_comm = getActivity().findViewById(R.id.menu_add_comm);
        menu_add_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateFarmActivity.class);
                startActivity(intent);
            }
        });
        menu_my_comm = getActivity().findViewById(R.id.menu_my_comm);
        menu_my_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                Intent intent = new Intent(getContext(), MyFarmActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = getActivity().findViewById(R.id.srl_comm);
    }

    private void initRefresh(){
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

    //view的onLoadMore
    @Override
    public void onLoadMore(List<FarmItem> list) {

    }

    //view的onRefresh
    @Override
    public void onRefresh(List<FarmItem> list) {
        Log.i("htht", "CommunityItemonRefresh:刷新完成了 ");
        mFarmList = list;
        farmAdapter.setDatas(mFarmList);
        farmAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }



}
