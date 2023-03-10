package com.example.conntest.pojo;

import com.example.conntest.pressure.PressureTask;

import java.util.Vector;

public class PressureTaskContext {

    private String address;
    private Vector<RoundInfo> roundInfoList;
    private Vector<StatusCodeInfo> statusCodeInfoList;
    private PressureTask taskHandle;

    public PressureTaskContext(String address, boolean remember, int roundTotal) {
        this.address = address;
        this.roundInfoList = new Vector<>();
        this.statusCodeInfoList = new Vector<>();
        this.taskHandle = new PressureTask(address, remember, roundTotal, roundInfoList);
    }

    public Vector<RoundInfo> getRoundInfoList() {
        return roundInfoList;
    }

    public void setRoundInfoList(Vector<RoundInfo> roundInfoList) {
        this.roundInfoList = roundInfoList;
    }

    public PressureTask getTaskHandle() {
        return taskHandle;
    }

    public void setTaskHandle(PressureTask taskHandle) {
        this.taskHandle = taskHandle;
    }

    public String getAddress() {
        return address;
    }

    public Vector<StatusCodeInfo> getStatusCodeInfoList() {
        return statusCodeInfoList;
    }

    public void setStatusCodeInfoList(Vector<StatusCodeInfo> statusCodeInfoList) {
        this.statusCodeInfoList = statusCodeInfoList;
    }

    public void putStatusCode(int statusCode) {
        StatusCodeInfo statusCodeInfoTemp = null;
        for (StatusCodeInfo statusCodeInfo : statusCodeInfoList) {
            if (statusCodeInfo.getStatusCode() == statusCode) {
                statusCodeInfoTemp = statusCodeInfo;
            }
        }
        if (statusCodeInfoTemp == null) {
            statusCodeInfoTemp = new StatusCodeInfo();
            statusCodeInfoTemp.setStatusCode(statusCode);
            statusCodeInfoList.add(statusCodeInfoTemp);
        }
        statusCodeInfoTemp.setCount(statusCodeInfoTemp.getCount() + 1);
    }

}
