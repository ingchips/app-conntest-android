package com.example.conntest.pressure;

public abstract class PressureTaskListener implements PressureListener {

    private int maxRoundNum;

    public PressureTaskListener(int maxRoundNum) {
        this.maxRoundNum = maxRoundNum;
    }

    public abstract int getCurrentRoundNum();
    public abstract void onTaskStart();
    public abstract void onTaskOver();
    public abstract void onRoundStart();
    public abstract void onRoundOver();

    @Override
    public void onScanPreStart() {
        if (getCurrentRoundNum() == 0)
            onTaskStart();
        onRoundStart();
    }

    @Override
    public void onDisconnectFailed(DisconnectResult result) {
        onRoundOver();
        if (getCurrentRoundNum() == maxRoundNum)
            onTaskOver();
    }
    @Override
    public void onDisconnectSuccess(DisconnectResult result) {
        onRoundOver();
        if (getCurrentRoundNum() == maxRoundNum)
            onTaskOver();
    }
}
