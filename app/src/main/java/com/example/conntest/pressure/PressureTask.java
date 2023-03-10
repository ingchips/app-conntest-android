package com.example.conntest.pressure;

import android.util.Log;

import com.example.conntest.common.MyContext;
import com.example.conntest.pojo.RoundInfo;
import com.example.conntest.utils.LogUtil;
import com.example.conntest.utils.ThreadUtil;

import java.util.Vector;

public class PressureTask extends PressureTaskListener {

    private static final String TAG = PressureTask.class.getSimpleName();

    private String address;
    public PressureData data;
    private Vector<RoundInfo> roundInfoList;
    private Pressure0 pressure0;

    public PressureTask(
            String address,
            boolean rememberDevice,
            int roundTotal, Vector<RoundInfo> roundInfoList) {
        super(roundTotal);

        this.address = address;
        this.data = new PressureData();
        this.data.setRoundTotal(roundTotal);
        this.roundInfoList = roundInfoList;
        this.pressure0 = new Pressure0(address, rememberDevice, this);
    }

    public PressureData getData() {
        return data;
    }

    public void finalResult() {
        data.setEndTime(System.currentTimeMillis());
        appendLog("连接测试结束！耗时：<font color='#222288'>" +
                (data.getEndTime() - data.getStartTime()) + "ms</font>");
        Log.i(TAG, data.toString());
    }

    public boolean isRunning() {
        return !pressure0.isTerminate();
    }

    public void terminate() {
        pressure0.terminate();
        finalResult();
    }

    public void start() {
        MyContext.executor.submit(pressure0);
    }

    private void appendLog(String text) {
        LogUtil.logInfo(TAG, text);
    }

    @Override
    public int getCurrentRoundNum() {
        return data.getRoundNum();
    }

    @Override
    public void onTaskStart() {
        data.setStartTime(System.currentTimeMillis());
        appendLog("开始");
    }

    @Override
    public void onTaskOver() {
        pressure0.terminate();
        finalResult();
    }

    @Override
    public void onRoundStart() {
        data.increaseRoundNum();
        Log.i(TAG, "onRoundStart: ");
        MyContext.pressureContext.postNewRound(address, getCurrentRoundNum());
    }

    @Override
    public void onRoundOver() {
        Log.i(TAG, "onRoundOver: ");
    }

    @Override
    public void onScanPreStart() {
        super.onScanPreStart();
        data.increaseScanNum();

        MyContext.pressureContext.notifyStartScan(address);

        appendLog("第 "+data.getRoundNum()+" 轮 扫描...");
    }
    @Override
    public void onScanSuccess(ScanResult result) {
        data.increaseScanSuccessNum();
        data.getScanResultList().add(result);
        MyContext.pressureContext.postScanResult(address, getCurrentRoundNum(), result);
        appendLog("<font color='#00FF00'>成功</font> <font color='#222288'>" + result.getSpendTime() + "ms</font>");
    }
    @Override
    public void onScanFailed(ScanResult result) {
        data.getScanResultList().add(result);
        MyContext.pressureContext.postScanResult(address, getCurrentRoundNum(), result);
        appendLog("<font color='#FFFF66'>超时</font> <font color='#222288'>" + result.getSpendTime() + "ms</font>");
        onRoundOver();
    }

    @Override
    public void onConnectPreStart() {
        data.increaseConnectNum();

        MyContext.pressureContext.notifyStartConnect(address);

        appendLog("第 "+data.getRoundNum()+" 轮 连接...");
    }
    @Override
    public void onConnectSuccess(ConnectResult result) {
        data.increaseConnectSuccessNum();
        data.getConnectResultList().add(result);

        MyContext.pressureContext.postConnectResult(address, getCurrentRoundNum(), result);

        appendLog("<font color='#00FF00'>成功</font> <font color='#222288'>" + result.getSpendTime() + "ms</font>");
    }
    @Override
    public void onConnectFailed(ConnectResult result) {
        data.getConnectResultList().add(result);

        MyContext.pressureContext.postConnectResult(address, getCurrentRoundNum(), result);

        appendLog("<font color='#FF0000'>失败</font> status:<font color='#222288'>" + result.getStatus() + "</font>");

        onRoundOver();
    }

    @Override
    public void onDisconnectPreStart() {
        data.increaseDisconnectNum();

        MyContext.pressureContext.notifyStartDisconnect(address);

        appendLog("第 "+data.getRoundNum()+" 轮 断连...");
    }
    @Override
    public void onDisconnectSuccess(DisconnectResult result) {
        data.increaseDisconnectSuccessNum();
        data.getDisconnectResultList().add(result);
        MyContext.pressureContext.postDisconnectResult(address, getCurrentRoundNum(), result);
        appendLog("<font color='#00FF00'>成功</font> <font color='#222288'>" + result.getSpendTime() + "ms</font>");

        super.onDisconnectSuccess(result);
    }
    @Override
    public void onDisconnectFailed(DisconnectResult result) {
        data.getDisconnectResultList().add(result);
        MyContext.pressureContext.postDisconnectResult(address, getCurrentRoundNum(), result);
        appendLog("<font color='#FF0000'>失败</font> status:<font color='#222288'>" + result.getStatus() + "</font>");

        super.onDisconnectFailed(result);
    }
}
