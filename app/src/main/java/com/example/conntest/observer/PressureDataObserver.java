package com.example.conntest.observer;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.conntest.adapter.PressureLogAdapter;
import com.example.conntest.common.MyContext;
import com.example.conntest.utils.ThreadUtil;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class PressureDataObserver implements Observer {

    private static final String TAG = PressureDataObserver.class.getSimpleName();

    private RecyclerView recyclerView;

    public PressureDataObserver(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.i(TAG, "notify update: ");
        HashMap<String, Object> param = (HashMap<String, Object>)arg;
        int type = (int)param.get("type");

        PressureLogAdapter adapter = (PressureLogAdapter) recyclerView.getAdapter();

        switch(type) {
            case PressureContext.NOTIFY_PRESSURE_CONTEXT_CHANGED:
                ThreadUtil.mainRun(() -> adapter.changeDataSet(MyContext.pressureContext.pressureContext.getRoundInfoList()));
                break;
            case PressureContext.NOTIFY_PRESSURE_START_ROUND_RESULT:
            case PressureContext.NOTIFY_PRESSURE_CONNECT_RESULT:
            case PressureContext.NOTIFY_PRESSURE_SCAN_RESULT:
            case PressureContext.NOTIFY_PRESSURE_DISCONNECT_RESULT:
                ThreadUtil.mainRun(() -> adapter.notifyDataSetChanged());
                break;
        default:
            break;
        }
    }
}
