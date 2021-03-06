package com.example.csnfh.customView;

import android.content.Context;
import android.util.AttributeSet;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2018/5/10.
 */

public class MyXListView extends XListView {
    public MyXListView(Context context) {
        super(context);
    }

    public MyXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyXListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
