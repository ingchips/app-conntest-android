package com.example.conntest.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtil {
    public static boolean checkThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private static final class MainHandlerHolder {
        static final Handler mainHandler = new Handler(Looper.getMainLooper());
    }

    public static void mainRun(Runnable runnable) {
        if (checkThread()) {
            runnable.run();
        } else {
            MainHandlerHolder.mainHandler.post(runnable);
        }
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
