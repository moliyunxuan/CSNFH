package com.example.csnfh.model.impl;

import com.example.csnfh.javabean.User;

public interface DynamicModelImpl {

    void getDynamicItem(BaseListener listener);
    void getDynamicItemByPhone(User user, BaseListener listener);



    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}
