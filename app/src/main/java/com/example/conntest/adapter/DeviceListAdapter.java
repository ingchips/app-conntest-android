package com.example.conntest.adapter;


import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conntest.MainActivity;
import com.example.conntest.R;
import com.example.conntest.common.MyContext;
import com.example.conntest.pojo.Device;
import com.example.conntest.utils.BLEUtil;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Vector;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {

    private static final String TAG = DeviceListAdapter.class.getSimpleName();
    private final Vector<Device> localDeviceList;

    /**
     * Initialize the dataset of the Adapter.
     * @param deviceNameList
     */
    public DeviceListAdapter(Vector<Device> deviceNameList) {
        localDeviceList = deviceNameList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_device_list_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device device = localDeviceList.get(position);

        if (device.getName() == null) {
            holder.getDeviceNameTextView().setText("N/A");
            holder.getDeviceNameTextView().setTextColor(Color.parseColor("#555555"));
        } else {
            holder.getDeviceNameTextView().setText(device.getName());
            holder.getDeviceNameTextView().setTextColor(Color.parseColor("#000000"));
        }

        holder.getDeviceAddressTextView().setText(device.getAddress());

        Button button = holder.getDeviceConnectionButton();

        if (!device.getConnectable()) {
            button.setVisibility(View.INVISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);

            if (device.getConnected()) {

                button.setText("OPEN TAB");

                button.setOnClickListener(v -> {});

            } else {
                button.setText("CONNECT");

                button.setOnClickListener(v ->
                    MyContext.deviceTable.selectDevice(device.getAddress()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return localDeviceList.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView deviceNameTextView;
        private final TextView deviceAddressTextView;
        private final Button deviceConnectionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Define click listener for the ViewHolder's View
            deviceNameTextView = itemView.findViewById(R.id.device_name);
            deviceAddressTextView = itemView.findViewById(R.id.device_address);
            deviceConnectionButton = itemView.findViewById(R.id.device_connection);
        }

        public TextView getDeviceNameTextView() {
            return deviceNameTextView;
        }

        public TextView getDeviceAddressTextView() {
            return deviceAddressTextView;
        }

        public Button getDeviceConnectionButton() {
            return deviceConnectionButton;
        }
    }

}
