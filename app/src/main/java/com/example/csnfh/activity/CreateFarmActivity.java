package com.example.csnfh.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.csnfh.R;
import com.example.csnfh.dialog.ActionSheetDialog;
import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.javabean.User;
import com.example.csnfh.javabean.UserFarm;
import com.example.csnfh.model.FarmModel;
import com.example.csnfh.model.impl.FarmModelImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.csnfh.activity.FarmDetailActivity.farmDetailActivity;

public class CreateFarmActivity extends AppCompatActivity {



    private EditText ed_farm_name;
    private EditText ed_farm_description;
    //private EditText ed_comm_school;
    private TextView tv_font_count;
    private Button btn_create_comm;
    private ImageView iv_farm;

    private BmobFile bmobFile;
    private File tempFile;
    private Context context;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_farm_image.jpg";
    private static final String IMAGE_FILE_CROP_NAME = "temp_crop_farm_image.jpg";

    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 剪切照片结果
    private Bitmap bitmap;
    private FarmItem farmItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_farm);
        context = this;
        farmItem = (FarmItem) getIntent().getSerializableExtra("COMM_CHANGE");
        initView();
    }

    private void initView() {
        ed_farm_name = findViewById(R.id.ed_comm_name);
        ed_farm_description = findViewById(R.id.ed_comm_description);
      //  ed_comm_school = findViewById(R.id.ed_comm_school);
        tv_font_count = findViewById(R.id.tv_font_count);
        iv_farm = findViewById(R.id.iv_create_comm);
        btn_create_comm = findViewById(R.id.btn_create_comm);

        ed_farm_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_font_count.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //判断是创建社团还是修改社团
        if(farmItem == null) {
            btn_create_comm.setText("创建农场");
            btn_create_comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createFarm();
                }
            });
        }else{
            if(farmItem.getFarmIcon() != null) {
                Log.i("htht", "student.getUserIcon().getFileUrl(): "+ farmItem.getFarmIcon().getFileUrl());
                Glide.with(context)
                        .load(farmItem.getFarmIcon().getFileUrl())
                        .error(R.mipmap.ic_launcher)
                        .into(iv_farm);
            }
            ed_farm_name.setText(farmItem.getFarmName());
            //ed_farm_school.setText(communityItem.getCommSchool());
            ed_farm_description.setText(farmItem.getFarmDescription());
            btn_create_comm.setText("修改农场");
            btn_create_comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeComm();
                }
            });
        }
        iv_farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionSheetDialog(context)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("打开相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {

                                        gallery();
                                    }
                                })
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {

                                        camera();
                                    }
                                }).show();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    crop(uri);
                } catch (Exception e) {

                }
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        IMAGE_FILE_NAME);

                if (Build.VERSION.SDK_INT >= 24) {
                    crop(FileProvider.getUriForFile(context, "com.ht.communi.fileprovider", tempFile));
                }else {
                    crop(Uri.fromFile(tempFile));
                }
            } else {
                Toast.makeText(context, "未找到存储卡，无法存储照片！",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                bitmap = data.getParcelableExtra("data");
                iv_farm.setImageBitmap(bitmap);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 剪切图片
     */

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    public void deleteIcon(String path) {
        //检查剪裁图片删除没有，如果没有，则删除。
        tempFile = new File(Environment.getExternalStorageDirectory(),
                path);
        Log.i("htht", "文件存在吗======= " + tempFile);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    //将bitmap转换成file
    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_CROP_NAME);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /*
     * 从相册获取
     */
    private void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    /*
     * 从相机获取
     */
    private void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            if (Build.VERSION.SDK_INT >= 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(context, "com.ht.communi.fileprovider", new File(Environment
                                .getExternalStorageDirectory(), IMAGE_FILE_NAME)) );
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            }else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
            }
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

    }

    private void createFarm() {
        if (!validate()) {
            onSignupFailed();
            Log.i("htht", "createComm: 直接退出了");
            return;
        }

        btn_create_comm.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建农场中...");
        progressDialog.show();

        String name = ed_farm_name.getText().toString();
       // String school = ed_farm_school.getText().toString();
        String description = ed_farm_description.getText().toString();

        //将图片设置完之后，还要上传网络

        farmItem = new FarmItem();
        farmItem.setFarmDescription(description);
        if(bitmap != null){
            bmobFile = new BmobFile(saveBitmapFile(bitmap));
            farmItem.setFarmIcon(bmobFile);
        }

     //   farmItem.setFarmSchool(school);
        farmItem.setFarmOwner(BmobUser.getCurrentUser(User.class));
        farmItem.setFarmName(name);
        farmItem.setVerify(new Boolean(false));


        //step1:将当前用户加入到新建农场，也是农场的第一个成员------------社团表
        BmobRelation bmobRelation = new BmobRelation();
        bmobRelation.add(BmobUser.getCurrentUser(User.class));
        farmItem.setFarmMembers(bmobRelation);
        new FarmModel().addFarmItem(farmItem, new FarmModelImpl.BaseListener(){
            @Override
            public void getSuccess(Object o) {
                deleteIcon(IMAGE_FILE_CROP_NAME);
                onSignupSuccess();
                //step1:将当前用户加入到新建农场，也是农场的第一个成员------------用户农场表（用户表的一个附属）
                BmobQuery<UserFarm> queryStu = new BmobQuery<>();
               User user =  BmobUser.getCurrentUser(User.class);
                queryStu.addWhereEqualTo("student",new BmobPointer(user));
                queryStu.findObjects(new FindListener<UserFarm>() {
                    @Override
                    public void done(List<UserFarm> list, BmobException e) {
                        if (e == null) {
                            Log.i("htht", "done查到啦！！！ "+list.get(0).getObjectId());
                            BmobRelation relation = new BmobRelation();
                            relation.add(farmItem);
                            UserFarm userFarm = new UserFarm();
                            userFarm.setFarms(relation);
                            userFarm.update(list.get(0).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Log.i("htht", "done: 将当前用户加入到新建农场，也是农场的第一个成员------------用户农场表   " );
                                    } else {
                                        Log.i("htht", "将当前用户加入到新建农场，也是农场的第一个成员------------用户农场表：" + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }
                            });
                        } else {
                            Log.i("htht", "222 " + e.getMessage() + e.getErrorCode());
                        }
                    }
                });
            }

            @Override
            public void getFailure() {
                deleteIcon(IMAGE_FILE_CROP_NAME);
            }
        });

        //检查是否拍了照片，如果拍了则删除。
        deleteIcon(IMAGE_FILE_NAME);



    }
    private void changeComm() {

        if (!validate()) {
            onSignupFailed();
            Log.i("htht", "changeComm: 直接退出了");
            return;
        }

        btn_create_comm.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("修改中...");
        progressDialog.show();

        String name = ed_farm_name.getText().toString();
        // String school = ed_farm_school.getText().toString();
        String description = ed_farm_description.getText().toString();

        //将图片设置完之后，还要上传网络

        FarmItem newFarmItem = new FarmItem();


        if(bitmap != null){
            bmobFile = new BmobFile(saveBitmapFile(bitmap));
            newFarmItem.setFarmIcon(bmobFile);
        }
        newFarmItem.setFarmDescription(description);
        //newFarmItem.setCommSchool(school);
        newFarmItem.setFarmName(name);
        newFarmItem.setVerify(new Boolean(false));

        new FarmModel().changeFarmItem(farmItem.getObjectId(), newFarmItem, new FarmModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                deleteIcon(IMAGE_FILE_CROP_NAME);
                onSignupSuccess();
            }

            @Override
            public void getFailure() {
                deleteIcon(IMAGE_FILE_CROP_NAME);
            }
        });

        //检查是否拍了照片，如果拍了则删除。
        deleteIcon(IMAGE_FILE_NAME);
    }

    private void onSignupSuccess() {
        btn_create_comm.setEnabled(true);
        finish();
        farmDetailActivity .finish();

    }

    private void onSignupFailed() {
            btn_create_comm.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;
        String name = ed_farm_name.getText().toString();
    //    String school = ed_comm_school.getText().toString();
        String description = ed_farm_description.getText().toString();

        if (name.isEmpty()|| name.length() > 20) {
            ed_farm_name.setError("请输入小于20位农场名");
            valid = false;
        } else {
            ed_farm_name.setError(null);
        }

//        if (school.isEmpty()) {
//            ed_comm_school.setError("请输入一个学校名");
//            valid = false;
//        } else {
//            ed_comm_school.setError(null);
//        }

        if (description.isEmpty()) {
            ed_farm_description.setError("请输入您对您农场的详细描述");
            valid = false;
        } else {
            ed_farm_description.setError(null);
        }
        return valid;

    }


}
