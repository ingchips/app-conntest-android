package com.example.conntest.observer;

import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.conntest.adapter.PressureStatusAdapter;
import com.example.conntest.common.MyContext;
import com.example.conntest.fragment.PressureStatsFragment;
import com.example.conntest.pressure.PressureData;
import com.example.conntest.utils.ThreadUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class PressureStatsObserver implements Observer {

    private static final String TAG = PressureStatsObserver.class.getSimpleName();

    WeakReference<PressureStatsFragment> pressureStatsFragmentWeakReference;

    public PressureStatsObserver(PressureStatsFragment pressureStatsFragment) {
        pressureStatsFragmentWeakReference = new WeakReference<>(pressureStatsFragment);
    }

    @Override
    public void update(Observable o, Object arg) {
        PressureStatsFragment statsFragment = pressureStatsFragmentWeakReference.get();
        if (statsFragment == null)
            return;

        HashMap<String, Object> param = (HashMap<String, Object>)arg;

        int type = (int)param.get("type");

        PressureStatusAdapter adapter =
                (PressureStatusAdapter)statsFragment.getBinding().pressureStatsRv.getAdapter();

        switch (type) {
            case PressureContext.NOTIFY_PRESSURE_CONTEXT_CHANGED:
                ThreadUtil.mainRun(() -> adapter.changeDataSet(MyContext.pressureContext.pressureContext.getStatusCodeInfoList()));
                break;
            case PressureContext.NOTIFY_PRESSURE_SCAN_RESULT:
                if (!(boolean)param.get("isDiscovered")) {
                    ThreadUtil.mainRun(() -> adapter.notifyDataSetChanged());
                }
                break;
            case PressureContext.NOTIFY_PRESSURE_CONNECT_RESULT:
                if (!(boolean)param.get("isConnected")) {
                    Log.i(TAG, "update: connect failure update statusTable");
                    ThreadUtil.mainRun(() -> adapter.notifyDataSetChanged());
                }
                ThreadUtil.mainRun(() -> {
                    PressureData data =
                            MyContext.pressureContext.pressureContext.getTaskHandle().getData();
                    int connectNum = data.getConnectNum();
                    int connectSuccessNum = data.getConnectSuccessNum();
                    float successRate = 100 * connectSuccessNum / (float)connectNum;
                    statsFragment.getBinding().connectionSucCountTv.setText(connectSuccessNum + "");
                    statsFragment.getBinding().connectionSucRateTv.setText(successRate + "%");
                });
                break;
            case PressureContext.NOTIFY_PRESSURE_DISCONNECT_RESULT:
                if (!(boolean)param.get("isDisconnected")) {
                    ThreadUtil.mainRun(() -> adapter.notifyDataSetChanged());
                }
                ThreadUtil.mainRun(() -> {
                    PressureData data =
                            MyContext.pressureContext.pressureContext.getTaskHandle().getData();
                    int disconnectNum = data.getDisconnectNum();
                    int disconnectSuccessNum = data.getDisconnectSuccessNum();
                    float successRate = 100 * disconnectSuccessNum / (float)disconnectNum;
                    statsFragment.getBinding().disconnectionSucCountTv.setText(disconnectSuccessNum + "");
                    statsFragment.getBinding().disconnectionSucRateTv.setText(successRate + "%");
                });
                break;
            case PressureContext.NOTIFY_PRESSURE_CONNECT_START:
                ThreadUtil.mainRun(() -> {
                    statsFragment.getBinding().connectionCountTv.setText("" +
                            MyContext.pressureContext
                                    .pressureContext.getTaskHandle().getData().getConnectNum());
                });
                break;
            case PressureContext.NOTIFY_PRESSURE_DISCONNECT_START:
                ThreadUtil.mainRun(() -> {
                    statsFragment.getBinding().disconnectionCountTv.setText("" +
                            MyContext.pressureContext
                                    .pressureContext.getTaskHandle().getData().getDisconnectNum());
                });
                break;
            case PressureContext.NOTIFY_PRESSURE_SCAN_START:
            default:
                break;
        }
    }
}
