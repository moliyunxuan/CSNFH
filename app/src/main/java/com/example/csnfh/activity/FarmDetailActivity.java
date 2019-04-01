package com.example.csnfh.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.csnfh.R;
import com.example.csnfh.adapter.FarmReplyAdapter;
import com.example.csnfh.customView.MyXListView;
import com.example.csnfh.customView.ResizableImageView;
import com.example.csnfh.customView.RiseNumberTextView;
import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.javabean.FarmReplyItem;
import com.example.csnfh.javabean.User;
import com.example.csnfh.model.FarmModel;
import com.example.csnfh.model.impl.FarmModelImpl;
import com.example.csnfh.presenter.FarmReplyPresenter;
import com.example.csnfh.utils.NetUtil;
import com.example.csnfh.view.IFarmReply;
import com.github.clans.fab.FloatingActionButton;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.maxwin.view.XListView;


/**
        * 有两种情况会进入此Activity
        * 1.浏览农场的时候。。。floadButtom是点击申请加入社团
        * 2.点击我的农场的时候，如果是管理员，则点击查看申请列表，如果是普通成员，则没有此按钮
        */
public class FarmDetailActivity extends AppCompatActivity implements XListView.IXListViewListener, IFarmReply {

    //完成修改农场回退两次
    public static FarmDetailActivity farmDetailActivity = null;

    private TextView tv_farm_detail_title;
   // private TextView tv_farm_detail_school;
    private TextView tv_farm_detail_content;
    private ResizableImageView iv_farm_icon;
    private ShineButton shineButton;
    private RiseNumberTextView tv_likes;

    //评论
    private ImageView comment;
    private TextView hide_down;
    private EditText comment_content;
    private Button comment_send;
    private LinearLayout rl_enroll;
    private RelativeLayout rl_comment;
    private ScrollView scrollView;


    private MyXListView xListView;
    private FarmReplyPresenter mPresenter;
    private FarmItem farmItem;
    private FarmReplyAdapter mAdapter;
    private List<FarmReplyItem> mList = new ArrayList<>();     //临时数据
    private List<FarmReplyItem> mDynamicList;              //真正的数据

    private FloatingActionButton menu_want_be_member;
    private FloatingActionButton menu_this_comm_event;
    private FloatingActionButton menu_apply_be_member;
    private FloatingActionButton menu_edit_comm;
    private boolean isLeader;           //true表示是这个部门的leader


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);
        farmDetailActivity = this;

        initView();
        farmItem = (FarmItem) getIntent().getSerializableExtra("COMM");
        //根据当前用户身份，判断显示FloatingActionButton显示哪些
        isLeader = farmItem.getFarmOwner().getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId());
        Log.i("htht", "是不是队长哦: " + isLeader);
        if (isLeader) {
            menu_apply_be_member.setVisibility(View.VISIBLE);
            menu_edit_comm.setVisibility(View.VISIBLE);

            menu_want_be_member.setVisibility(View.GONE);
        } else {
            menu_apply_be_member.setVisibility(View.GONE);
            menu_edit_comm.setVisibility(View.GONE);

            menu_want_be_member.setVisibility(View.VISIBLE);
        }

        //农场信息+点赞
        if (shineButton != null) {
            shineButton.init(FarmDetailActivity.this);
            shineButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean checked) {
                    Log.i("htht", "点赞了吗: " + checked);
                    if (checked) {
                        tv_likes.setText(Integer.parseInt(tv_likes.getText().toString()) + 1 + "");
                       farmItem.increment("likes");
                    } else {
                        tv_likes.setText(Integer.parseInt(tv_likes.getText().toString()) - 1 + "");
                        farmItem.increment("likes", -1);
                    }
                    farmItem.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("htht", "点赞成功！！ ");
                            }
                        }
                    });
                }
            });
        }

        tv_farm_detail_title.setText(farmItem.getFarmName());
        //tv_farm_detail_school.setText(farmItem.getCommSchool());
        tv_farm_detail_content.setText(farmItem.getFarmDescription());

        new FarmModel().getFarmLikes(farmItem, new FarmModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                Integer likes = ((FarmItem) o).getLikes();
                if (likes != null) {
                    tv_likes.setInteger(0, likes);
                    tv_likes.start();
                }
            }

            @Override
            public void getFailure() {

            }
        });
        if (farmItem.getFarmIcon() != null) {
            Glide.with(FarmDetailActivity.this)
                    .load(farmItem.getFarmIcon().getFileUrl())
                    .into(iv_farm_icon);
        }

        //评论的xListView
        xListView.setPullRefreshEnable(false);
        xListView.setPullLoadEnable(false);
        xListView.setXListViewListener(this);
        mAdapter = new FarmReplyAdapter(this, R.layout.activity_farm_reply_item, mList);

        mPresenter = new FarmReplyPresenter(this);

        xListView.setAdapter(mAdapter);

        if (NetUtil.checkNet(this) && farmItem != null) {
            mPresenter.onRefresh(farmItem);
        } else {
            xListView.setVisibility(View.GONE);
        }

    }

    private void initView() {
        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_farm_icon = findViewById(R.id.iv_comm_icon);
        tv_farm_detail_title = findViewById(R.id.tv_comm_detail_title);
       // tv_farm_detail_school = findViewById(R.id.tv_comm_detail_school);
        tv_farm_detail_content = findViewById(R.id.tv_comm_detail_content);
        shineButton = findViewById(R.id.shineButton);
        tv_likes = findViewById(R.id.tv_likes);
        scrollView = (ScrollView) findViewById(R.id.base_scrollView);

        //评论功能
        rl_enroll = (LinearLayout) findViewById(R.id.rl_enroll);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        comment = (ImageView) findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInputFromWindow(FarmDetailActivity.this, comment_content);
                // 显示评论框
                rl_enroll.setVisibility(View.GONE);
                rl_comment.setVisibility(View.VISIBLE);
            }
        });
        hide_down = (TextView) findViewById(R.id.hide_down);
        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏评论框
                rl_enroll.setVisibility(View.VISIBLE);
                rl_comment.setVisibility(View.GONE);
                //移动到最顶端
                scrollView.smoothScrollTo(0, 0);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
            }
        });
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
        xListView = findViewById(R.id.xListView_comm_reply);

        //申请成为农场成员
        menu_want_be_member = findViewById(R.id.menu_want_be_member);
        menu_want_be_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobRelation relation = new BmobRelation();
                relation.add(BmobUser.getCurrentUser(User.class));
                farmItem.setFarmApplies(relation);
                farmItem.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("htht", "申请加入本农场成功");
                            Toast.makeText(FarmDetailActivity.this,"申请成功，请等待农场管理员回应",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("htht", "申请加入本农场失败：" + e.getMessage());
                        }
                    }
                });
            }
        });

        //查看本农场活动
        menu_this_comm_event = findViewById(R.id.menu_this_comm_event);
        menu_this_comm_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmDetailActivity.this, FarmEventActivity.class);
                Bundle bundle = new Bundle();
                if (farmItem != null) {
                    bundle.putSerializable("COMM_EVENT",farmItem);
                }
                intent.putExtra("IS_LEADER",isLeader);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //查看申请加入这个农场的列表
        menu_apply_be_member = findViewById(R.id.menu_apply_be_member);
        menu_apply_be_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmDetailActivity.this,applyBeMemberActivity.class);
                Bundle bundle = new Bundle();
                if (farmItem != null) {
                    bundle.putSerializable("COMM_APPLY", farmItem);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //编辑农场信息
        menu_edit_comm = findViewById(R.id.menu_edit_comm);
        menu_edit_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (farmItem != null) {
                    Intent intent = new Intent(FarmDetailActivity.this,CreateFarmActivity.class);
                    Bundle bundle = new Bundle();
                    if (farmItem != null) {
                        bundle.putSerializable("COMM_CHANGE", farmItem);
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 发送评论
     */
    public void sendComment() {
        if (comment_content.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            FarmReplyItem communityReplyItem = new FarmReplyItem();
            if (farmItem != null) {
                communityReplyItem.setFarm(farmItem);
            }
            communityReplyItem.setContent(comment_content.getText().toString());
            communityReplyItem.setUser(BmobUser.getCurrentUser(User.class));
            mPresenter.onSendReply(communityReplyItem);
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(final Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        // 弹出输入法
        InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * xListview  回调
     */
    @Override
    public void onRefresh() {
        if (farmItem != null) {
            mPresenter.onRefresh(farmItem);
        }
    }

    /**
     * xListview  回调
     */
    @Override
    public void onLoadMore() {
        mPresenter.onLoadMore();
    }


    /**
     * view 回调
     *
     * @param list
     */
    @Override
    public void onLoadMore(List<FarmReplyItem> list) {

    }

    /**
     * view 回调
     *
     * @param list
     */
    @Override
    public void onRefresh(List<FarmReplyItem> list) {
        mDynamicList = list;
        mAdapter.setDatas(mDynamicList);
        mAdapter.notifyDataSetChanged();
//        onLoad();
    }

    /**
     * view 发送完数据的回调
     */
    @Override
    public void onSendFinish() {
        // 发送完，清空输入框
        comment_content.setText("");
        Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();
        Log.i("htht", "onSendFinish:刷新了！！！ ");
        if (farmItem != null) {
            mPresenter.onRefresh(farmItem);
        }
    }

    //暂时不需要这个方法了
    private void onLoad() {
        xListView.setVisibility(View.VISIBLE);

        xListView.stopRefresh();
        xListView.stopLoadMore();

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");//设置日期显示格式
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);// 将时间装换为设置好的格式
        xListView.setRefreshTime(str);//设置时间
    }
}
