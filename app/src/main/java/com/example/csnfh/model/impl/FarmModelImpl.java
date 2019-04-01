package com.example.csnfh.model.impl;

public interface FarmModelImpl {
    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}
