package com.example.conntest.pressure;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

import com.example.conntest.pojo.Device;
import com.example.conntest.utils.BLEUtil;

import java.util.concurrent.Semaphore;

public class Pressure implements Runnable {

    private static final String TAG = Pressure.class.getSimpleName();
    private int roundNum;
    private int connectionKeepTime;
    private int disconnectionKeepTime;

    private Device device;

    private int state;

    private Semaphore terminateSem;

    public Pressure(Device device, int roundNum, int connectionKeepTime, int disconnectionKeepTime) {
        this.device = device;
        this.roundNum = roundNum;
        this.connectionKeepTime = connectionKeepTime;
        this.disconnectionKeepTime = disconnectionKeepTime;

        this.terminateSem = new Semaphore(0);
    }

    public void terminate() {
        this.terminateSem.release();
    }

    @Override
    public void run() {
        while(true) {
            if (terminateSem.tryAcquire()) break;

            if (device.getConnected()) {
                boolean b = BLEUtil.disconnect0(device.getBluetoothGatt()).getDisconnected();
                if (b) {
                    Log.i(TAG, "DisconnectOk");
                    device.setBluetoothGatt(null);
                    device.setConnected(false);

                    try {
                        Thread.sleep(connectionKeepTime * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i(TAG, "DisconnectFail");
                }
            } else {
                BluetoothGatt bluetoothGatt = BLEUtil.connect0(device.getDeviceHandle()).getGatt();
                if (bluetoothGatt == null) {
                    Log.i(TAG, "ConnectFail");
                } else {
                    Log.i(TAG, "ConnectOk");
                    device.setBluetoothGatt(bluetoothGatt);
                    device.setConnected(true);

                    try {
                        Thread.sleep(disconnectionKeepTime * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
