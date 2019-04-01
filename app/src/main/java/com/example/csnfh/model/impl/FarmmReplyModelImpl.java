package com.example.csnfh.model.impl;

public interface FarmmReplyModelImpl {

    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}
