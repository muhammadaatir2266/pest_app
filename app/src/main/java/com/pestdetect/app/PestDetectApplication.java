package com.pestdetect.app;

import android.app.Application;
import android.content.Context;
import com.pestdetect.app.utils.LocaleHelper;

public class PestDetectApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
