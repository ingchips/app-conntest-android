package com.example.conntest.adapter;


import android.bluetooth.BluetoothGatt;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conntest.R;
import com.example.conntest.common.MyContext;
import com.example.conntest.pojo.Device;
import com.example.conntest.utils.BLEUtil;

import java.lang.ref.WeakReference;
import java.util.Vector;

public class SelectedDeviceListAdapter extends RecyclerView.Adapter<SelectedDeviceListAdapter.ViewHolder> {

    private static final String TAG = SelectedDeviceListAdapter.class.getSimpleName();
    private final Vector<Device> localDeviceList;
    private WeakReference<RecyclerView> recyclerViewWeakReference;

    private int defaultPosition = 0;
    private int prevPosition = -1;
    private int currPosition = -1;

    private long lastClickTime = System.currentTimeMillis();

    public int getCurrPosition() {
        return currPosition;
    }

    /**
     * Initialize the dataset of the Adapter.
     * @param deviceNameList
     */
    public SelectedDeviceListAdapter(RecyclerView recyclerView, Vector<Device> deviceNameList) {
        localDeviceList = deviceNameList;
        recyclerViewWeakReference = new WeakReference<>(recyclerView);
    }


    private ViewHolder findViewHolderByPosition(int position) {
        RecyclerView recyclerView = recyclerViewWeakReference.get();

        return (ViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
    }

    private void choose(ViewHolder holder, int position) {
        Log.i(TAG, "choose: " + position);

        if (currPosition == position)
            return;
        Log.i(TAG, "current: " + currPosition);

        prevPosition = currPosition;
        currPosition = position;

        MyContext.currentSelectedDevicePosition.set(currPosition);

        if (currPosition != -1) {
            ConstraintLayout currLayout = holder.getLayout();
            currLayout.setBackgroundColor(Color.parseColor("#dddddd"));
        }
        if (prevPosition != -1) {
            ViewHolder prevViewHolder = findViewHolderByPosition(prevPosition);
            ConstraintLayout prevLayout = prevViewHolder.getLayout();
            prevLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_selected_device, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device device = localDeviceList.get(position);

        TextView deviceNameTv = holder.getDeviceNameTv();

        if (device.getName() == null) {
            deviceNameTv.setText("N/A");
            deviceNameTv.setTextColor(Color.parseColor("#555555"));
        } else {
            deviceNameTv.setText(device.getName());
            deviceNameTv.setTextColor(Color.parseColor("#000000"));
        }

        if (currPosition == -1 && defaultPosition == position) {
            Log.i(TAG, "onBindViewHolder: init");
            choose(holder, position);
        }

        ConstraintLayout layout = holder.getLayout();

        if (currPosition == position) {
            layout.setBackgroundColor(Color.parseColor("#dddddd"));
        } else {
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        layout.setOnClickListener(v -> {
            Log.i(TAG, "onBindViewHolder: ");

            choose(holder, holder.getAdapterPosition());
        });

//        TextView spendTimeTv = holder.getSpendTimeTv();
//        TextView connectionStateTv = holder.getConnectionStateTv();

//        Button connectionBtn = holder.getConnectionBtn();
//        if (device.getConnected()) {
//            spendTimeTv.setText("耗时：" + device.getSpendTime() + "ms");
//            connectionStateTv.setText("已连接");
//            connectionBtn.setText("Dis");
//            connectionBtn.setEnabled(true);
//            connectionBtn.setOnClickListener(v -> {
//                if (System.currentTimeMillis() - lastClickTime < 1000) return;
//                lastClickTime = System.currentTimeMillis();
//
//                connectionBtn.setEnabled(false);
//
//                BluetoothGatt gatt = device.getBluetoothGatt();
//                assert gatt != null;
//
//                long l = System.currentTimeMillis();
//                boolean b = BLEUtil.disconnect0(device.getBluetoothGatt()).getDisconnected();
//                l = System.currentTimeMillis() - l;
//                device.setSpendTime(l);
//
//                if (b) {
//                    device.setConnected(false);
//                    MyContext.deviceTable.notifyDeviceDisconnectedInSelectedList(holder.getAdapterPosition());
//                } else {
//                    connectionBtn.setEnabled(true);
//                }
//            });
//        } else {
//            spendTimeTv.setText("耗时：" + device.getSpendTime() + "ms");
//            connectionStateTv.setText("空闲");
//            connectionBtn.setText("Con");
//            connectionBtn.setEnabled(true);
//            connectionBtn.setOnClickListener(v -> {
//                if (System.currentTimeMillis() - lastClickTime < 1000) return;
//                lastClickTime = System.currentTimeMillis();
//
//                connectionBtn.setEnabled(false);
//
//                long l = System.currentTimeMillis();
//                BluetoothGatt bluetoothGatt = BLEUtil.connect0(device.getDeviceHandle()).getGatt();
//                l = System.currentTimeMillis() - l;
//                device.setSpendTime(l);
//
//                Log.i(TAG, "onBindViewHolder: " + bluetoothGatt);
//
//                if (bluetoothGatt != null) {
//                    connectionBtn.setEnabled(false);
//
//                    device.setConnected(true);
//                    device.setBluetoothGatt(bluetoothGatt);
//                    MyContext.deviceTable.notifyDeviceConnectedInSelectedList(holder.getAdapterPosition());
//                } else {
//                    connectionBtn.setEnabled(true);
//                }
//            });
//        }

        Button removeBtn = holder.getRemoveBtn();
        removeBtn.setOnClickListener(v ->
                MyContext.deviceTable.removeSelectDevice(device.getAddress()));
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

        private final TextView deviceNameTv;
        private final ConstraintLayout layout;
//        private final Button connectionBtn;
        private final Button removeBtn;
//        private final TextView spendTimeTv;
//        private final TextView connectionStateTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Define click listener for the ViewHolder's View
            deviceNameTv = itemView.findViewById(R.id.device_name_tv);
            layout = itemView.findViewById(R.id.root);
//            connectionBtn = itemView.findViewById(R.id.connection_btn);
            removeBtn = itemView.findViewById(R.id.remove_btn);
//            spendTimeTv = itemView.findViewById(R.id.spend_time_tv);
//            connectionStateTv = itemView.findViewById(R.id.connection_state_tv);
        }

        public TextView getDeviceNameTv() {
            return deviceNameTv;
        }

        public ConstraintLayout getLayout() {
            return layout;
        }

//        public Button getConnectionBtn() {
//            return connectionBtn;
//        }

        public Button getRemoveBtn() {
            return removeBtn;
        }
//
//        public TextView getSpendTimeTv() {
//            return spendTimeTv;
//        }
//
//        public TextView getConnectionStateTv() {
//            return connectionStateTv;
//        }
    }

}
