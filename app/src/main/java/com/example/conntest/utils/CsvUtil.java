package com.example.conntest.utils;

import android.util.Log;

import com.example.conntest.common.MyContext;
import com.example.conntest.pojo.RoundInfo;
import com.example.conntest.pressure.ConnectResult;
import com.example.conntest.pressure.DisconnectResult;
import com.example.conntest.pressure.ScanResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CsvUtil {

    private static final String TAG = CsvUtil.class.getSimpleName();

    private static CsvUtil sInstance;

    public static CsvUtil getInstance() {
        if (sInstance == null)
            synchronized (CsvUtil.class) {
                if (sInstance == null)
                    sInstance = new CsvUtil();
            }
        Log.i(TAG, "getInstance: ");
        return sInstance;
    }

    private String csvFilePath;
    private boolean isReady;

    private CsvUtil() {
        csvFilePath = MyContext.csvFilePath;
        File file = new File(csvFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            isReady = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            isReady = false;
        }
        Log.i(TAG, "isReady: " + isReady);
        if (!isReady) {
            Log.i(TAG, "can not open csv file!");
        }
    }

    private static final String TEXT_SUCCESS = "成功";
    private static final String TEXT_FAILED = "失败";
    private static final String TEXT_TIMEOUT = "超时";
    private static final String TEXT_STATUS = "status:0x";
    private static final String TEXT_MS = "ms";
    private static final String TEXT_NONE = "--";
    private static final String TEXT_SEPARATOR = ",";

    /**
     * timeout false
     * @param text
     * @return
     */
    private boolean writeToFile(String text) {
        if (!isReady) return false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> feature = executorService.submit(() -> {
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(csvFilePath);
                outputStream.write(text.getBytes("gb2312"));
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        });

        try {
            return feature.get(1000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean exportCsv(Vector<RoundInfo> roundInfoVector) {
        CsvUtil instance = getInstance();
        if (instance == null) return false;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("轮数,扫描,,连接,,断连,");
        stringBuilder.append(System.lineSeparator());

        for (RoundInfo roundInfo : roundInfoVector) {
            instance.csvAppendRoundInfo(stringBuilder, roundInfo);
        }

        return instance.writeToFile(stringBuilder.toString());
    }

    private void csvAppendRoundInfo(StringBuilder stringBuilder, RoundInfo roundInfo) {

        stringBuilder.append(roundInfo.getRoundNum());
        stringBuilder.append(TEXT_SEPARATOR);


        if (roundInfo.getScanResult() != null) {
            ScanResult scanResult = roundInfo.getScanResult();
            if (scanResult.isDiscovered()) {
                stringBuilder.append(TEXT_SUCCESS);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(scanResult.getSpendTime() + TEXT_MS);
            } else {
                stringBuilder.append(TEXT_TIMEOUT);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(scanResult.getSpendTime() + TEXT_MS);
            }
        } else {
            {
                stringBuilder.append(TEXT_NONE);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(TEXT_NONE);
            }
        }
        stringBuilder.append(TEXT_SEPARATOR);

        if (roundInfo.getConnectResult() != null) {
            ConnectResult connectResult = roundInfo.getConnectResult();
            if (connectResult.isConnected()) {
                stringBuilder.append(TEXT_SUCCESS);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(connectResult.getSpendTime() + TEXT_MS);
            } else {
                stringBuilder.append(TEXT_FAILED);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(TEXT_STATUS + Integer.toHexString(connectResult.getStatus()));
            }
        } else {
            {
                stringBuilder.append(TEXT_NONE);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(TEXT_NONE);
            }
        }
        stringBuilder.append(TEXT_SEPARATOR);

        if (roundInfo.getDisconnectResult() != null) {
            DisconnectResult disconnectResult = roundInfo.getDisconnectResult();
            if (disconnectResult.isDisconnected()) {
                stringBuilder.append(TEXT_SUCCESS);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(disconnectResult.getSpendTime() + TEXT_MS);
            } else {
                stringBuilder.append(TEXT_FAILED);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(TEXT_STATUS + Integer.toHexString(disconnectResult.getStatus()));
            }
        } else {
            {
                stringBuilder.append(TEXT_NONE);
                stringBuilder.append(TEXT_SEPARATOR);
                stringBuilder.append(TEXT_NONE);
            }
        }
        stringBuilder.append(System.lineSeparator());
    }
}
