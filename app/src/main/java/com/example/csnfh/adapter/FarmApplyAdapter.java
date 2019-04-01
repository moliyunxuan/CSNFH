package com.example.csnfh.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.csnfh.R;
import com.example.csnfh.javabean.User;

import java.util.List;

public class FarmApplyAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> mDataList;

    public FarmApplyAdapter(Context mContext, List<User> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    public void setDatas(List<User> datas){
        mDataList = datas;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public User getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.activity_farm_apply_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        User user = getItem(position);
        if(user.getUserIcon()!= null) {
            Log.i("htht", "list.get(0).getUserIcon(): " + user.getUserIcon().getFileUrl());
            Glide.with(mContext)
                    .load(user.getUserIcon().getFileUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .error(R.mipmap.ic_launcher)
                    .into(holder.iv_icon);
        } else {
            holder.iv_icon.setImageResource(R.mipmap.ic_launcher);
        }

        holder.tv_name.setText(user.getUserName());
        holder.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("htht", "iv_icon onClick: ");
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("htht", "tv_name iv_icon onClick: ");
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(this);
        }
    }


}
