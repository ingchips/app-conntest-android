package com.example.conntest.observer;

import android.util.Log;

import com.example.conntest.pojo.PressureTaskContext;
import com.example.conntest.pojo.RoundInfo;
import com.example.conntest.pojo.StatusCodeInfo;
import com.example.conntest.pressure.ConnectResult;
import com.example.conntest.pressure.DisconnectResult;
import com.example.conntest.pressure.ScanResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;

public class PressureContext extends Observable {

    public static final String TAG = PressureContext.class.getSimpleName();

    public static final int NOTIFY_PRESSURE_CONTEXT_CHANGED = 1;
    public static final int NOTIFY_PRESSURE_START_ROUND_RESULT = 2;
    public static final int NOTIFY_PRESSURE_SCAN_RESULT = 3;
    public static final int NOTIFY_PRESSURE_CONNECT_RESULT = 4;
    public static final int NOTIFY_PRESSURE_DISCONNECT_RESULT = 5;
    public static final int NOTIFY_PRESSURE_SCAN_START = 6;
    public static final int NOTIFY_PRESSURE_CONNECT_START = 7;
    public static final int NOTIFY_PRESSURE_DISCONNECT_START = 8;

    public static Hashtable<String, PressureTaskContext> pressureDataTable = new Hashtable<>();
    public static PressureTaskContext pressureContext = null;

    public void switchPressureContext(String bleDeviceAddress) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(bleDeviceAddress);
        if (pressureTaskContext == null)
            return;

        pressureContext = pressureTaskContext;

        HashMap<String, Object> arg = new HashMap<>();
        arg.put("type", NOTIFY_PRESSURE_CONTEXT_CHANGED);
        arg.put("address", bleDeviceAddress);
        setChanged();
        notifyObservers(arg);
    }

    public PressureTaskContext getPressureContext(String address) {

        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) {
            pressureTaskContext = new PressureTaskContext(address, false, 50);
            pressureDataTable.put(address, pressureTaskContext);
        }
        return pressureTaskContext;
    }

    public void postNewRound(String address, int roundNum) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) return;

        Vector<RoundInfo> roundInfoList = pressureTaskContext.getRoundInfoList();
        RoundInfo roundInfo = new RoundInfo();
        roundInfo.setRoundNum(roundNum);
        roundInfo.setState(RoundInfo.ROUND_STATE_SCAN);
        roundInfoList.add(roundInfo);

        assert pressureContext != null;
        if (pressureContext == pressureTaskContext) {
            HashMap<String, Object> arg = new HashMap<>();
            arg.put("type", NOTIFY_PRESSURE_START_ROUND_RESULT);
            arg.put("address", address);
            arg.put("roundNum", roundNum);
            setChanged();
            notifyObservers(arg);
        }
    }

    public void postScanResult(String address, int roundNum, ScanResult scanResult) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) return;

        Vector<RoundInfo> roundInfoList = pressureTaskContext.getRoundInfoList();
        RoundInfo roundInfo = roundInfoList.get(roundNum - 1);
        roundInfo.setScanResult(scanResult);
        roundInfo.setState(RoundInfo.ROUND_STATE_CONNECT);


        assert pressureContext != null;
        if (pressureContext == pressureTaskContext) {
            HashMap<String, Object> arg = new HashMap<>();
            arg.put("type", NOTIFY_PRESSURE_SCAN_RESULT);
            arg.put("address", address);
            arg.put("roundNum", roundNum);
            arg.put("isDiscovered", scanResult.isDiscovered());
            setChanged();
            notifyObservers(arg);
        }
    }

    public void postConnectResult(String address, int roundNum, ConnectResult connectResult) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) return;

        Vector<RoundInfo> roundInfoList = pressureTaskContext.getRoundInfoList();
        RoundInfo roundInfo = roundInfoList.get(roundNum - 1);
        roundInfo.setConnectResult(connectResult);
        roundInfo.setState(RoundInfo.ROUND_STATE_DISCONNECT);

        if (!connectResult.isConnected()) {
            pressureTaskContext.putStatusCode(connectResult.getStatus());

            Log.i(TAG, pressureTaskContext.getStatusCodeInfoList().toString());
        }

        assert pressureContext != null;
        if (pressureContext == pressureTaskContext) {
            HashMap<String, Object> arg = new HashMap<>();
            arg.put("type", NOTIFY_PRESSURE_CONNECT_RESULT);
            arg.put("address", address);
            arg.put("roundNum", roundNum);
            arg.put("isConnected", connectResult.isConnected());
            setChanged();
            notifyObservers(arg);
        }
    }

    public void postDisconnectResult(String address, int roundNum, DisconnectResult disconnectResult) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) return;

        Vector<RoundInfo> roundInfoList = pressureTaskContext.getRoundInfoList();
        RoundInfo roundInfo = roundInfoList.get(roundNum - 1);
        roundInfo.setDisconnectResult(disconnectResult);
        roundInfo.setState(RoundInfo.ROUND_STATE_OVER);

        if (!disconnectResult.isDisconnected()) {
            pressureTaskContext.putStatusCode(disconnectResult.getStatus());
        }

        assert pressureContext != null;
        if (pressureContext == pressureTaskContext) {
            HashMap<String, Object> arg = new HashMap<>();
            arg.put("type", NOTIFY_PRESSURE_DISCONNECT_RESULT);
            arg.put("address", address);
            arg.put("roundNum", roundNum);
            arg.put("isDisconnected", disconnectResult.isDisconnected());
            setChanged();
            notifyObservers(arg);
        }
    }

    public void notifyStartScan(String address) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) return;
        assert pressureContext != null;
        if (pressureContext == pressureTaskContext) {
            HashMap<String, Object> arg = new HashMap<>();
            arg.put("type", NOTIFY_PRESSURE_SCAN_START);
            setChanged();
            notifyObservers(arg);
        }
    }

    public void notifyStartConnect(String address) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) return;
        assert pressureContext != null;
        if (pressureContext == pressureTaskContext) {
            HashMap<String, Object> arg = new HashMap<>();
            arg.put("type", NOTIFY_PRESSURE_CONNECT_START);
            setChanged();
            notifyObservers(arg);
        }
    }

    public void notifyStartDisconnect(String address) {
        PressureTaskContext pressureTaskContext = pressureDataTable.get(address);
        if (pressureTaskContext == null) return;
        assert pressureContext != null;
        if (pressureContext == pressureTaskContext) {
            HashMap<String, Object> arg = new HashMap<>();
            arg.put("type", NOTIFY_PRESSURE_DISCONNECT_START);
            setChanged();
            notifyObservers(arg);
        }
    }
}
