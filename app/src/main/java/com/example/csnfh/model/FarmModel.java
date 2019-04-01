package com.example.csnfh.model;

import android.util.Log;

import com.example.csnfh.javabean.FarmItem;
import com.example.csnfh.javabean.User;
import com.example.csnfh.javabean.UserFarm;
import com.example.csnfh.model.impl.FarmModelImpl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class FarmModel implements FarmModelImpl {

    /**
     * 创建农场
     *
     * @param farmItem 农场tem
     */
    public void addFarmItem(final FarmItem farmItem, final BaseListener listener) {
        final BmobFile farmIcon = farmItem.getFarmIcon();
        if (farmIcon != null) {
            farmIcon.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("htht", "上传农场icon成功:");
                        farmItem.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Log.i("htht", "农场创建成功，等待验证！！！");
                                    listener.getSuccess(null);
                                } else {
                                    Log.i("htht", "农场创建失败！！！");
                                }
                            }
                        });
                    } else {
                        listener.getFailure();
                    }

                }
            });
        } else {
            farmItem.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("htht", "农场创建成功，等待验证！！！");
                        listener.getSuccess(null);
                    } else {
                        listener.getFailure();
                        Log.i("htht", "农场创建失败！！！");
                    }
                }
            });
        }
    }

    /**
     * 修改农场信息
     *
     * @param farmItem
     * @param listener
     */
    public void changeFarmItem(final String oldFarmItemId, final FarmItem farmItem, final BaseListener listener) {
        final BmobFile farmIcon = farmItem.getFarmIcon();
        if (farmIcon != null) {
            farmIcon.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("htht", "上传修改后的农场icon成功:");
                        farmItem.update(oldFarmItemId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i("htht", "农场修改成功，等待验证！！！");
                                    listener.getSuccess(null);
                                } else {
                                    Log.i("htht", "农场修改失败！！！");
                                }
                            }
                        });
                    } else {
                        listener.getFailure();
                    }

                }
            });
        } else {
            farmItem.update(oldFarmItemId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("htht", "社团修改成功，等待验证！！！");
                        listener.getSuccess(null);
                    } else {
                        Log.i("htht", "社团修改失败！！！");
                    }
                }
            });
        }
    }


    //查询当前农场的申请列表
    public void getFarmAppliers(FarmItem farmItem, final BaseListener listener) {

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("farmApplies", new BmobPointer(farmItem));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    listener.getSuccess(list);
                    Log.i("htht", "查询个数：" + list.size());
                } else {
                    listener.getFailure();
                    Log.i("htht", "失败：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 获取所有的农场消息
     *
     * @param listener
     */
    public void getFarmItem(final BaseListener listener) {
        BmobQuery<FarmItem> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("commLeader");
        query.findObjects(new FindListener<FarmItem>() {
            @Override
            public void done(List<FarmItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询农场成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询农场失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 查找某个农场的点赞数
     *
     * @param item
     * @param listener
     */
    public void getFarmLikes(FarmItem item, final BaseListener listener) {
        BmobQuery<FarmItem> query = new BmobQuery<>();
        query.getObject(item.getObjectId(), new QueryListener<FarmItem>() {
            @Override
            public void done(FarmItem farmItem, BmobException e) {
                if (e == null) {
                    //获得playerName的信息
                    farmItem.getLikes();
                    listener.getSuccess(farmItem);
                } else {
                    listener.getFailure();
                    Log.i("htht", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 查询加入某个农场的所有用户
     *
     * @param item
     * @param listener
     */
    public void getMyUserItem(FarmItem item, final BaseListener listener) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("farmMembers", new BmobPointer(item));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询农场用户成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    listener.getFailure();
                    Log.i("htht", "查询农场用户失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    /**
     * 查询某个用户加入的所有农场
     *
     * @param listener
     */
    public void getMyFarmItem(final BaseListener listener) {
        BmobQuery<UserFarm> queryStu = new BmobQuery<>();
        User user = BmobUser.getCurrentUser(User.class);
        queryStu.addWhereEqualTo("user", new BmobPointer(user));
        queryStu.findObjects(new FindListener<UserFarm>() {
            @Override
            public void done(List<UserFarm> list, BmobException e) {
                if (e == null) {
                    BmobQuery<FarmItem> query = new BmobQuery<>();
                    query.include("commLeader");
                    query.addWhereRelatedTo("communities", new BmobPointer(list.get(0)));
                    query.findObjects(new FindListener<FarmItem>() {
                        @Override
                        public void done(List<FarmItem> list, BmobException e) {
                            if (e == null) {
                                Log.i("htht", "done: 查询我的农场成功：共   " + list.size() + "  条数据。");
                                listener.getSuccess(list);
                            } else {
                                listener.getFailure();
                                Log.i("htht", "查询我的农场失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                    Log.i("htht", "done查到啦！！！ " + list.get(0).getObjectId());
                } else {
                    Log.i("htht", "222 " + e.getMessage() + e.getErrorCode());
                }
            }
        });


    }
}
