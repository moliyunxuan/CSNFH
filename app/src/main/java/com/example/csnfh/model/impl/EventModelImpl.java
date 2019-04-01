package com.example.csnfh.model.impl;

public interface EventModelImpl {
    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}


