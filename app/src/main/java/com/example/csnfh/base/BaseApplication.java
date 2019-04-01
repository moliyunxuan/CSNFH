package com.example.csnfh.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {





    public static Context getmContext() {
        return sContext;
    }

    static Context sContext;

}
