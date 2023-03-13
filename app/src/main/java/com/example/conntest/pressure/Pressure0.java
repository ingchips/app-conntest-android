package com.example.conntest.pressure;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import com.example.conntest.utils.BLEUtil;
import com.example.conntest.utils.ThreadUtil;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Pressure0 extends PressureTemplate{

    public Pressure0(String address, boolean rememberDevice, PressureListener listener) {
        super(address, listener);

        remember = rememberDevice;
        terminateSem = new Semaphore(0);
        scanDevices = new LinkedBlockingDeque<>(1);

    }

    private boolean remember;
    private BluetoothDevice rememberDevice;
    private final Semaphore terminateSem;
    private final BlockingDeque<BluetoothDevice> scanDevices;

    private boolean scanning = false;

    public void terminate() {
        terminateSem.release();
        BLEUtil.stopScan();
    }

    @Override
    Boolean isTerminate() {
        return terminateSem.tryAcquire();
    }

    /**
     * [min, max)
     * @param min
     * @param max
     * @return
     */
    private int random(int min, int max) {
        return min + (int)(Math.random() * (max - min));
    }

    @Override
    Result connect(BluetoothDevice device) {
        long t1 = System.currentTimeMillis();
        BLEUtil.ConnectResult connectResult0 = null;
        connectResult0 = BLEUtil.connect0(device);
        long t2 = System.currentTimeMillis();

        ConnectResult connectResult = new ConnectResult();
        connectResult.setConnected(connectResult0.getGatt() != null);
        connectResult.setGatt(connectResult0.getGatt());
        connectResult.setStatus(connectResult0.getStatus());
        connectResult.setNewState(connectResult0.getNewState());
        connectResult.setSpendTime(t2 - t1);
        return connectResult;
    }

    @Override
    Result disconnect(BluetoothGatt gatt) {
        long t1 = System.currentTimeMillis();
        BLEUtil.DisconnectResult disconnectResult0 = BLEUtil.disconnect0(gatt);
        long t2 = System.currentTimeMillis();

        DisconnectResult disconnectResult = new DisconnectResult();
        disconnectResult.setDisconnected(disconnectResult0.getDisconnected());
        disconnectResult.setStatus(disconnectResult0.getStatus());
        disconnectResult.setNewState(disconnectResult0.getNewState());
        disconnectResult.setSpendTime(t2 - t1);

        return disconnectResult;
    }

    @Override
    Result scan(String address) {
        if (!scanning) {
            BLEUtil.startScanSpecifiedAddress(address, result -> {
                if (result == null)
                    return;
                BluetoothDevice device = result.getDevice();
                if (device == null)
                    return;
                try {
                    scanDevices.putFirst(device);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, Integer.MAX_VALUE / 2);
            scanning = true;
        }

        if (remember && rememberDevice != null) {

            ScanResult scanResult = new ScanResult();
            scanResult.setDevice(rememberDevice);
            scanResult.setSpendTime(0);
            scanResult.setDiscovered(true);
            return scanResult;
        }

        scanDevices.clear();
        try {
            long t1 = System.currentTimeMillis();
            BluetoothDevice bluetoothDevice = scanDevices.pollLast(5000, TimeUnit.MILLISECONDS);
            long t2 = System.currentTimeMillis();


            if (bluetoothDevice != null) {
                if (rememberDevice == null && remember)
                    rememberDevice = bluetoothDevice;

                ScanResult scanResult = new ScanResult();
                scanResult.setDiscovered(true);
                scanResult.setDevice(bluetoothDevice);
                scanResult.setSpendTime(t2 - t1);
                return scanResult;
            } else {
                ScanResult scanResult = new ScanResult();
                scanResult.setDiscovered(false);
                scanResult.setSpendTime(t2 - t1);
                return scanResult;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ScanResult scanResult = new ScanResult();
        scanResult.setDiscovered(false);
        return scanResult;
    }
}
