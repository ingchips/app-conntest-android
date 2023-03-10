package com.example.conntest.common;

import com.example.conntest.observer.DeviceTable;
import com.example.conntest.observer.PressureContext;
import com.example.conntest.pojo.PressureTaskContext;

import java.util.Hashtable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class MyContext {
    public static DeviceTable deviceTable = new DeviceTable();

    public static AtomicInteger currentSelectedDevicePosition = new AtomicInteger(-1);

    public static PressureContext pressureContext = new PressureContext();

    public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public static String logFilePath = "/storage/emulated/0/conn_test.log";
    public static String csvFilePath = "/storage/emulated/0/conn_test.csv";
}
