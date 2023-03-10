package com.example.conntest.pressure;

import android.bluetooth.BluetoothDevice;

public class ScanResult implements Result {
    private boolean discovered;

    private long spendTime;

    private BluetoothDevice device;

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(long spendTime) {
        this.spendTime = spendTime;
    }
}
