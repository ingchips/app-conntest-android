package com.example.conntest.observer;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class DeviceScanObserver implements Observer {

    private static final String TAG = DeviceScanObserver.class.getSimpleName();

    WeakReference<RecyclerView> recyclerViewWR;

    public DeviceScanObserver(RecyclerView recyclerView) {
        this.recyclerViewWR = new WeakReference<RecyclerView>(recyclerView);
    }

    /**
     *
     * @param o
     * @param arg
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void update(Observable o, Object arg) {
        RecyclerView recyclerView = recyclerViewWR.get();

        if (recyclerView == null)
            return;

        Log.i(TAG, "Observer: " + (String)arg);

        try {
            JSONObject jsonObject = new JSONObject((String) arg);
            int position;

            int type = jsonObject.getInt("type");
            switch (type) {
                case DeviceTable.NOTIFY_TYPE_SCAN_INSERT:
                    position = jsonObject.getInt("position");
                    Objects.requireNonNull(recyclerView.getAdapter())
                            .notifyItemInserted(position);
                    break;
                case DeviceTable.NOTIFY_TYPE_CLEAR:
                case DeviceTable.NOTIFY_TYPE_SELECT_ONE_DEVICE:
                    Objects.requireNonNull(recyclerView.getAdapter())
                            .notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
