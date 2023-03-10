package com.example.conntest.pressure;

public interface PressureListener {
    void onScanPreStart();
    void onConnectPreStart();
    void onDisconnectPreStart();
    void onScanSuccess(ScanResult result);
    void onConnectSuccess(ConnectResult result);
    void onDisconnectSuccess(DisconnectResult result);
    void onScanFailed(ScanResult result);
    void onConnectFailed(ConnectResult result);
    void onDisconnectFailed(DisconnectResult result);
}
