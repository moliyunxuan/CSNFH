package com.example.csnfh.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.csnfh.R;
import com.example.csnfh.adapter.DynamicPhotoChooseAdapter;
import com.example.csnfh.dialog.DialogBuilder;
import com.example.csnfh.javabean.DynamicItem;
import com.example.csnfh.javabean.DynamicPhotoItem;
import com.example.csnfh.javabean.User;
import com.example.csnfh.model.DynamicModel;
import com.example.csnfh.model.impl.DynamicModelImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import me.iwf.photopicker.PhotoPicker;
/*
        * 作者：
        * 作用：发朋友圈页面
        */



public class SendDynamicActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 0x01;

    private TextView cancel;
    private TextView send;
    private TextView contentCount;
    private EditText editContent;
    private GridView gridView;
    private DynamicPhotoChooseAdapter mDynamicPhotoChooseAdapter;


    private final String LOGINUSER = "loginuser";
    private User mUser;
    private Dialog mLoadingDialog;
    private Dialog mLoadingFinishDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_send_dynamic);
        mUser = (User) getIntent().getSerializableExtra("User");
        ButterKnife.bind(this);
        initview();
        init();
    }



    private void initview() {
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentCount = findViewById(R.id.tv_content_count);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        editContent = findViewById(R.id.edit_content);
        gridView = findViewById(R.id.gridView);
    }



    public void init() {

        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contentCount.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDynamicPhotoChooseAdapter = new DynamicPhotoChooseAdapter(SendDynamicActivity.this);
        gridView.setAdapter(mDynamicPhotoChooseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mDynamicPhotoChooseAdapter.getCount() - 1) {
                    PhotoPicker.builder()
                            .setPhotoCount(9)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .start(SendDynamicActivity.this, PhotoPicker.REQUEST_CODE);
                }
            }
        });

            mLoadingDialog = DialogBuilder.createLoadingDialog(SendDynamicActivity.this, "正在上传");
            mLoadingFinishDialog = DialogBuilder.createLoadingfinishDialog(SendDynamicActivity.this, "上传完成");
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            List<DynamicPhotoItem> list = new ArrayList<>();
            if (pathList.size() != 0) {
                for (String path : pathList) {
                    list.add(new DynamicPhotoItem(path, false));
                }
            }
            mDynamicPhotoChooseAdapter.addData(list);
            gridView.setAdapter(mDynamicPhotoChooseAdapter);
        }
    }

    private void send() {
        mLoadingDialog.show();
        DynamicItem dynamicItem = new DynamicItem();
        dynamicItem.setWriter(mUser);
        List<BmobFile> fileList = new ArrayList<>();
        ArrayList<DynamicPhotoItem> photoItems = (ArrayList<DynamicPhotoItem>) mDynamicPhotoChooseAdapter.getData();
        for (int i = 0; i < photoItems.size() - 1; i++) {
            fileList.add(new BmobFile(new File(photoItems.get(i).getFilePath())));
        }
        dynamicItem.setText(editContent.getText().toString());
        dynamicItem.setPhotoList(fileList);
        new DynamicModel().sendDynamicItem(dynamicItem, new DynamicModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                mLoadingDialog.dismiss();
                mLoadingFinishDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingFinishDialog.dismiss();
                        finish();
                    }
                }, 500);
            }

            @Override
            public void getFailure() {

            }
        });
    }
}