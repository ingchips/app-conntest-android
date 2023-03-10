package com.example.conntest.pressure;

import java.util.ArrayList;
import java.util.List;

public class PressureData {
    private int roundTotal;
    private int roundNum;
    private int scanNum;
    private int connectNum;
    private int disconnectNum;
    private int scanSuccessNum;
    private int connectSuccessNum;
    private int disconnectSuccessNum;

    private long startTime;
    private long endTime;

    private List<ScanResult> scanResultList;
    private List<ConnectResult> connectResultList;
    private List<DisconnectResult> disconnectResultList;

    public PressureData() {
        scanResultList = new ArrayList<>();
        connectResultList = new ArrayList<>();
        disconnectResultList = new ArrayList<>();
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public int getScanNum() {
        return scanNum;
    }

    public void setScanNum(int scanNum) {
        this.scanNum = scanNum;
    }

    public int getConnectNum() {
        return connectNum;
    }

    public void setConnectNum(int connectNum) {
        this.connectNum = connectNum;
    }

    public int getDisconnectNum() {
        return disconnectNum;
    }

    public void setDisconnectNum(int disconnectNum) {
        this.disconnectNum = disconnectNum;
    }

    public int getScanSuccessNum() {
        return scanSuccessNum;
    }

    public void setScanSuccessNum(int scanSuccessNum) {
        this.scanSuccessNum = scanSuccessNum;
    }

    public int getConnectSuccessNum() {
        return connectSuccessNum;
    }

    public void setConnectSuccessNum(int connectSuccessNum) {
        this.connectSuccessNum = connectSuccessNum;
    }

    public int getDisconnectSuccessNum() {
        return disconnectSuccessNum;
    }

    public void setDisconnectSuccessNum(int disconnectSuccessNum) {
        this.disconnectSuccessNum = disconnectSuccessNum;
    }

    public List<ScanResult> getScanResultList() {
        return scanResultList;
    }

    public void setScanResultList(List<ScanResult> scanResultList) {
        this.scanResultList = scanResultList;
    }

    public List<ConnectResult> getConnectResultList() {
        return connectResultList;
    }

    public void setConnectResultList(List<ConnectResult> connectResultList) {
        this.connectResultList = connectResultList;
    }

    public List<DisconnectResult> getDisconnectResultList() {
        return disconnectResultList;
    }

    public void setDisconnectResultList(List<DisconnectResult> disconnectResultList) {
        this.disconnectResultList = disconnectResultList;
    }

    public int getRoundTotal() {
        return roundTotal;
    }

    public void setRoundTotal(int roundTotal) {
        this.roundTotal = roundTotal;
    }

    public void increaseRoundNum() {
        roundNum++;
    }
    public void increaseScanNum() {
        scanNum++;
    }
    public void increaseConnectNum() {
        connectNum++;
    }
    public void increaseDisconnectNum() {
        disconnectNum++;
    }
    public void increaseScanSuccessNum() {
        scanSuccessNum++;
    }
    public void increaseConnectSuccessNum() {
        connectSuccessNum++;
    }
    public void increaseDisconnectSuccessNum() {
        disconnectSuccessNum++;
    }

    @Override
    public String toString() {
        return "PressureData{" +
                "roundTotal=" + roundTotal +
                ", roundNum=" + roundNum +
                ", scanNum=" + scanNum +
                ", connectNum=" + connectNum +
                ", disconnectNum=" + disconnectNum +
                ", scanSuccessNum=" + scanSuccessNum +
                ", connectSuccessNum=" + connectSuccessNum +
                ", disconnectSuccessNum=" + disconnectSuccessNum +
                ", scanResultList=" + scanResultList +
                ", connectResultList=" + connectResultList +
                ", disconnectResultList=" + disconnectResultList +
                '}';
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
