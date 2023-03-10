package com.example.conntest.utils;

import android.util.Log;

import com.example.conntest.common.MyContext;
import com.example.conntest.pojo.RoundInfo;
import com.example.conntest.pressure.ConnectResult;
import com.example.conntest.pressure.DisconnectResult;
import com.example.conntest.pressure.ScanResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LogUtil {
    private static final String TAG = LogUtil.class.getSimpleName();

    private static LogUtil sInstance;

    private String logFilePath;
    private boolean isReady;
    private ScheduledExecutorService executors;

    private LogUtil() {
        logFilePath = MyContext.logFilePath;
        File file = new File(logFilePath);
        if (!file.exists()) {
            try {
                isReady = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                isReady = false;
            }
        } else {
            isReady = file.isFile();
        }
        Log.i(TAG, "isReady: " + isReady);
        if (!isReady) {
            Log.i(TAG, "can not open file!");
        }
        executors = Executors.newScheduledThreadPool(10);
    }

    public static LogUtil getInstance() {
        if (sInstance == null) {
            synchronized (LogUtil.class) {
                if (sInstance == null) {
                    sInstance = new LogUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * Appends logs to specified file
     * no responding
     * Success is not ensured
     * @param str
     */
    private void appendLog(String str) {
        if (!isReady) return;
        executors.submit(() -> {
            BufferedWriter bufferedWriter = null;
            try {
                FileWriter fileWriter = new FileWriter(logFilePath, true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String getDateTime() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]", Locale.CHINA);
        return simpleDateFormat.format(new Date());
    }

    public void logInfo0(String tag, String str) {
        String output = getDateTime() + str;
        Log.i(tag, output);
        appendLog(output);
    }

    public static void logInfo(String tag, String str) {
        LogUtil instance = getInstance();
        if (instance == null) return;

        instance.logInfo0(tag, str);
    }
}
