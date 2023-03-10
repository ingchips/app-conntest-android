package com.example.conntest.observer;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class DeviceSelectedObserver implements Observer {

    private static final String TAG = DeviceSelectedObserver.class.getSimpleName();

    WeakReference<RecyclerView> recyclerViewWR;

    public DeviceSelectedObserver(RecyclerView recyclerView) {
        this.recyclerViewWR = new WeakReference<>(recyclerView);
    }


    @Override
    public void update(Observable o, Object arg) {
        RecyclerView recyclerView = recyclerViewWR.get();

        if (recyclerView == null)
            return;


        try {
            JSONObject jsonObject = new JSONObject((String) arg);
            int position;

            int type = jsonObject.getInt("type");
            switch (type) {
                case DeviceTable.NOTIFY_TYPE_SELECT_ONE_DEVICE:
                case DeviceTable.NOTIFY_TYPE_SELECTED_REMOVE:
                    Objects.requireNonNull(recyclerView.getAdapter())
                            .notifyDataSetChanged();
                    break;
                case DeviceTable.NOTIFY_TYPE_DEVICE_CONNECT:
                case DeviceTable.NOTIFY_TYPE_DEVICE_DISCONNECT:

                    int position1 = jsonObject.getInt("position");

                    Log.i(TAG, "update: position: " + position1);

                    recyclerView.getAdapter().notifyItemChanged(position1);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
