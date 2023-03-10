package com.example.conntest.pressure;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.util.Log;

public abstract class PressureTemplate implements Runnable {
    abstract Boolean isTerminate();

    abstract Result connect(BluetoothDevice device);
    abstract Result scan(String address);
    abstract Result disconnect(BluetoothGatt gatt);

    private static final int STATE_CONNECTED = 0;
    private static final int STATE_DISCONNECTED = 1;
    private static final int STATE_DISCOVERED = 2;

    private int state;

    public PressureTemplate(String address, PressureListener listener) {
        this.address = address;
        this.state = STATE_DISCONNECTED;
        this.pressureListener = listener;
    }

    private PressureListener pressureListener;
    private String address;
    private BluetoothDevice device;
    private BluetoothGatt gatt;

    @Override
    public void run() {
        while (!isTerminate()) {
            switch (state) {
                case STATE_DISCONNECTED:
                    pressureListener.onScanPreStart();
                    ScanResult r = (ScanResult) scan(address);
                    if (r.isDiscovered() && r.getDevice() != null) {
                        device = r.getDevice();
                        state = STATE_DISCOVERED;
                        pressureListener.onScanSuccess(r);
                    } else {
                        pressureListener.onScanFailed(r);
                    }
                    break;
                case STATE_DISCOVERED:
                    pressureListener.onConnectPreStart();
                    ConnectResult cr = (ConnectResult) connect(device);
                    if (cr.isConnected() && cr.getGatt() != null) {
                        gatt = cr.getGatt();
                        state = STATE_CONNECTED;
                        pressureListener.onConnectSuccess(cr);
                    } else {
                        state = STATE_DISCONNECTED;
                        pressureListener.onConnectFailed(cr);
                    }
                    break;
                case STATE_CONNECTED:
                    pressureListener.onDisconnectPreStart();
                    DisconnectResult dr = (DisconnectResult) disconnect(gatt);
                    if (dr.isDisconnected()) {
                        gatt = null;
                        state = STATE_DISCONNECTED;
                        pressureListener.onDisconnectSuccess(dr);
                    } else {
                        pressureListener.onDisconnectFailed(dr);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
