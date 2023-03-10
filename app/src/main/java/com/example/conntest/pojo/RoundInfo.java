package com.example.conntest.pojo;

import com.example.conntest.pressure.ConnectResult;
import com.example.conntest.pressure.DisconnectResult;
import com.example.conntest.pressure.ScanResult;

public class RoundInfo {

    public static final int ROUND_STATE_SCAN = 0;
    public static final int ROUND_STATE_CONNECT = 1;
    public static final int ROUND_STATE_DISCONNECT = 2;
    public static final int ROUND_STATE_OVER = 3;

    private int state;

    private int roundNum;

    private ScanResult scanResult;
    private ConnectResult connectResult;
    private DisconnectResult disconnectResult;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public ConnectResult getConnectResult() {
        return connectResult;
    }

    public void setConnectResult(ConnectResult connectResult) {
        this.connectResult = connectResult;
    }

    public DisconnectResult getDisconnectResult() {
        return disconnectResult;
    }

    public void setDisconnectResult(DisconnectResult disconnectResult) {
        this.disconnectResult = disconnectResult;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }
}
