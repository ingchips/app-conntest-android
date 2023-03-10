package com.example.conntest.pojo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

public class Device {
    private String name;
    private String address;
    private Boolean isConnectable;
    private Boolean isConnected;
    private BluetoothGatt bluetoothGatt;
    private Long spendTime;

    private BluetoothDevice deviceHandle;

    public Device(String name, String address, Boolean inIsConnectable, BluetoothDevice inDeviceHandle) {
        this.name = name;
        this.address = address;
        this.isConnectable = inIsConnectable;

        this.deviceHandle = inDeviceHandle;
        this.isConnected = false;
        this.bluetoothGatt = null;
        this.spendTime = 0l;
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getConnectable() {
        return isConnectable;
    }

    public void setConnectable(Boolean connectable) {
        isConnectable = connectable;
    }

    public BluetoothDevice getDeviceHandle() {
        return deviceHandle;
    }

    public void setDeviceHandle(BluetoothDevice deviceHandle) {
        this.deviceHandle = deviceHandle;
    }

    public Long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
    }
}
