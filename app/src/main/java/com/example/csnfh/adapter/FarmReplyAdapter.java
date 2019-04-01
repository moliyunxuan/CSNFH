package com.example.csnfh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.csnfh.R;
import com.example.csnfh.customView.CircleImageView;
import com.example.csnfh.javabean.FarmReplyItem;

import java.util.List;

public class FarmReplyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FarmReplyItem> mDatas;
    private int mLayoutRes;
    private Context mContext;

    public FarmReplyAdapter(Context context, int layoutRes, List<FarmReplyItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutRes = layoutRes;
        mInflater = LayoutInflater.from(context);
    }


    public List<FarmReplyItem> returnmDatas() {
        return this.mDatas;
    }

    public void addAll(List<FarmReplyItem> mDatas) {
        this.mDatas.addAll(mDatas);
    }

    public void setDatas(List<FarmReplyItem> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            holder = new ViewHolder();
            holder.write_photo = (CircleImageView) convertView.findViewById(R.id.farm_reply_stuIcon_item);
            holder.write_name = (TextView) convertView.findViewById(R.id.farm_reply_stuName_item);
            holder.write_date = (TextView) convertView.findViewById(R.id.farm_reply_time_item);
            holder.dynamic_text = (TextView) convertView.findViewById(R.id.farm_reply_text_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FarmReplyItem FarmReplyItem = mDatas.get(position);
        final ViewHolder viewHolder = holder;

        if (FarmReplyItem.getUser().getUserIcon() != null) {
            Glide.with(mContext)
                    .load(FarmReplyItem.getUser().getUserIcon().getFileUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.write_photo);
        }else {
            viewHolder.write_photo.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.write_name.setText(FarmReplyItem.getUser().getUserName());
        viewHolder.write_date.setText(FarmReplyItem.getCreatedAt());
        holder.dynamic_text.setText(FarmReplyItem.getContent());
        return convertView;
    }

    private final class ViewHolder {
        CircleImageView write_photo;
        TextView write_name;
        TextView write_date;
        TextView dynamic_text;
    }
}
