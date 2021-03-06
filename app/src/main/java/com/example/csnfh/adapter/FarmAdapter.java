package com.example.csnfh.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.csnfh.R;
import com.example.csnfh.activity.FarmDetailActivity;
import com.example.csnfh.javabean.FarmItem;

import java.util.List;

public class FarmAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int NORMAL_ITEM = 0;

    private Context mContext;
    private List<FarmItem> mDataList;
    private LayoutInflater mLayoutInflater;

    public FarmAdapter(Context mContext, List<FarmItem> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<FarmItem> mDatas) {
        mDataList.clear();
        mDataList.addAll(mDatas);
    }

    /**
     * 渲染具体的ViewHolder
     *
     * @param parent   ViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            return new NormalItemHolder(mLayoutInflater.inflate(R.layout.community_cardview_item, parent, false));
        }
        return null;
    }


    /**
     * 绑定ViewHolder的数据。
     *
     * @param viewHolder
     * @param i          数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final FarmItem entity = mDataList.get(i);
        if (null == entity)
            return;

        if (viewHolder instanceof NormalItemHolder) {
            NormalItemHolder holder = (NormalItemHolder) viewHolder;
            holder.commRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showFarmDetail(v,entity);
                }
            });
            bindNormalItem(entity,holder.commTitle, holder.commDescription, holder.commIcon);
        } else {
            NormalItemHolder holder = (NormalItemHolder) viewHolder;
            holder.commRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFarmDetail(v,entity);
                }
            });
            bindNormalItem(entity, holder.commTitle,holder.commDescription, holder.commIcon);
        }
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源List的下标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        return NORMAL_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void bindNormalItem(FarmItem entity, TextView commTitle, TextView commDescription, ImageView commIcon) {
        if (entity.getFarmIcon() == null) {
            if (commIcon.getVisibility() != View.GONE)
                commIcon.setVisibility(View.GONE);
        } else {
            Glide.with(mContext)
                    .load(entity.getFarmIcon().getFileUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(commIcon);

            if (commIcon.getVisibility() != View.VISIBLE)
                commIcon.setVisibility(View.VISIBLE);
        }
        commTitle.setText(entity.getFarmName());
        commDescription.setText(entity.getFarmDescription());
    }

    void showFarmDetail(View v,FarmItem item) {
        Intent intent = new Intent(v.getContext(), FarmDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("COMM", item);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
        Log.i("htht", "onItemClick: 点中了 ==  "+item.getFarmName());
    }

    /**
     * 新闻标题
     */
    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView commTitle;
        TextView commDescription;
        ImageView commIcon;
        LinearLayout commRoot;


        public NormalItemHolder(View itemView) {
            super(itemView);
            commTitle = itemView.findViewById(R.id.base_swipe_item_title);
            commDescription = itemView.findViewById(R.id.base_swipe_item_content);
            commIcon = itemView.findViewById(R.id.base_swipe_item_icon);
            commRoot = itemView.findViewById(R.id.base_swipe_item_container);
        }
    }


}
