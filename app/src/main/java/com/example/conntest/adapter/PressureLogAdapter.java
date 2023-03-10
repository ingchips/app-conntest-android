package com.example.conntest.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conntest.R;
import com.example.conntest.pojo.RoundInfo;
import com.example.conntest.pressure.ConnectResult;
import com.example.conntest.pressure.DisconnectResult;
import com.example.conntest.pressure.ScanResult;

import java.util.Vector;

public class PressureLogAdapter extends RecyclerView.Adapter<PressureLogAdapter.ViewHolder> {

    private Vector<RoundInfo> localBeanList;

    public PressureLogAdapter(Vector<RoundInfo> beanList) {
        this.localBeanList = beanList;
    }

    public void changeDataSet(Vector<RoundInfo> newBeanList) {
        this.localBeanList = newBeanList;
        Log.i("Press adp", "changeDataSet: ");
        notifyDataSetChanged();
    }

    public void notifyAppend() {
        notifyItemInserted(getItemCount());
    }
    public void notifyLastUpdate() {
        notifyItemChanged(getItemCount() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pressure_log_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoundInfo roundInfo = localBeanList.get(position);

        holder.pressureLogRoundTv.setText("第" + roundInfo.getRoundNum() + "轮");
        if (roundInfo.getScanResult() != null) {
            ScanResult scanResult = roundInfo.getScanResult();
            if (scanResult.isDiscovered()) {
                holder.pressureLogScanTv.setText(Html.fromHtml("<font color='#66FF66'>成功</font><font color='#6666FF'>" + scanResult.getSpendTime() + "ms</font>", 0));
            } else {
                holder.pressureLogScanTv.setText(Html.fromHtml("<font color='#FFFF66'>超时</font><font color='#6666FF'>" + scanResult.getSpendTime() + "ms</font>", 0));
            }
        } else {
            holder.pressureLogScanTv.setText("--");
        }
        if (roundInfo.getConnectResult() != null) {
            ConnectResult connectResult = roundInfo.getConnectResult();
            if (connectResult.isConnected()) {
                holder.pressureLogConnectTv.setText(Html.fromHtml("<font color='#66FF66'>成功</font><font color='#6666FF'>" + connectResult.getSpendTime() + "ms</font>", 0));
            } else {
                holder.pressureLogConnectTv.setText(Html.fromHtml("<font color='#FF6666'>失败</font><font color='#6666FF'>status:0x" + Integer.toHexString(connectResult.getStatus()) + "</font>", 0));
            }
        } else {
            holder.pressureLogConnectTv.setText("--");
        }
        if (roundInfo.getDisconnectResult() != null) {
            DisconnectResult disconnectResult = roundInfo.getDisconnectResult();
            if (disconnectResult.isDisconnected()) {
                holder.pressureLogDisconnectTv.setText(Html.fromHtml("<font color='#66FF66'>成功</font><font color='#6666FF'>" + disconnectResult.getSpendTime() + "ms</font>", 0));
            } else {
                holder.pressureLogDisconnectTv.setText(Html.fromHtml("<font color='#66FF66'>失败</font><font color='#6666FF'>status:0x" + Integer.toHexString(disconnectResult.getStatus()) + "</font>", 0));
            }
        } else {
            holder.pressureLogDisconnectTv.setText("--");
        }
    }

    @Override
    public int getItemCount() {
        return localBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView pressureLogRoundTv;
        TextView pressureLogScanTv;
        TextView pressureLogConnectTv;
        TextView pressureLogDisconnectTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pressureLogRoundTv = itemView.findViewById(R.id.pressure_log_round_tv);
            pressureLogScanTv = itemView.findViewById(R.id.pressure_log_scan_tv);
            pressureLogConnectTv = itemView.findViewById(R.id.pressure_log_connect_tv);
            pressureLogDisconnectTv = itemView.findViewById(R.id.pressure_log_disconnect_tv);
        }
    }
}
