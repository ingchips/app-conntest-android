package com.example.conntest.observer;

import com.example.conntest.pojo.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

public class DeviceTable extends Observable {

    public static final int NOTIFY_TYPE_CLEAR               = 1;
    public static final int NOTIFY_TYPE_SCAN_INSERT         = 2;
    public static final int NOTIFY_TYPE_SELECT_ONE_DEVICE   = 3;
    public static final int NOTIFY_TYPE_SELECTED_REMOVE     = 4;
    public static final int NOTIFY_TYPE_DEVICE_CONNECT      = 5;
    public static final int NOTIFY_TYPE_DEVICE_DISCONNECT   = 6;

    private static final String TAG = DeviceTable.class.getSimpleName();

    public static Map<String, Device> scanDeviceTable = new Hashtable<>();
    public static Vector<Device> scanDeviceList = new Vector<>();

    public static Map<String, Device> selectedDeviceTable = new Hashtable<>();
    public static Vector<Device> selectedDeviceList = new Vector<>();

    public DeviceTable() {

    }

    public Vector<Device> getScanDeviceList() {
        return scanDeviceList;
    }
    public Vector<Device> getSelectedDeviceList() {
        return selectedDeviceList;
    }

    public boolean containsDevice(String address) {
        return scanDeviceTable.containsKey(address) || selectedDeviceTable.containsKey(address);
    }

    public synchronized void putScanDevice(String address, Device device) {
        scanDeviceTable.put(address, device);
        scanDeviceList.add(device);

        setChanged();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", NOTIFY_TYPE_SCAN_INSERT);
            jsonObject.put("address", address);
            jsonObject.put("position", scanDeviceList.size() - 1);
            notifyObservers(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void clearScanDeviceTable() {
        scanDeviceTable.clear();
        scanDeviceList.clear();

        setChanged();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", NOTIFY_TYPE_CLEAR);
            notifyObservers(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void selectDevice(String address) {
        Device device = scanDeviceTable.remove(address);
        boolean remove = scanDeviceList.remove(device);
        assert device != null && remove;

        selectedDeviceTable.put(address, device);
        selectedDeviceList.add(device);

        setChanged();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", NOTIFY_TYPE_SELECT_ONE_DEVICE);
            jsonObject.put("address", address);
            notifyObservers(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeSelectDevice(String address) {
        Device device = selectedDeviceTable.remove(address);
        boolean remove = selectedDeviceList.remove(device);
        assert device != null && remove;


        setChanged();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", NOTIFY_TYPE_SELECTED_REMOVE);
            jsonObject.put("address", address);
            notifyObservers(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notifyDeviceConnectedInSelectedList(int position) {

        setChanged();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", NOTIFY_TYPE_DEVICE_CONNECT);
            jsonObject.put("position", position);
            notifyObservers(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notifyDeviceDisconnectedInSelectedList(int position) {

        setChanged();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", NOTIFY_TYPE_DEVICE_DISCONNECT);
            jsonObject.put("position", position);
            notifyObservers(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
