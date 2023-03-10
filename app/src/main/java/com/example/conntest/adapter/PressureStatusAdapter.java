package com.example.conntest.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conntest.R;
import com.example.conntest.pojo.RoundInfo;
import com.example.conntest.pojo.StatusCodeInfo;

import java.util.List;
import java.util.Vector;

public class PressureStatusAdapter extends RecyclerView.Adapter<PressureStatusAdapter.ViewHolder> {

    private Vector<StatusCodeInfo> statusCodeInfoList;

    public PressureStatusAdapter(Vector<StatusCodeInfo> statusCodeInfoList) {
        this.statusCodeInfoList = statusCodeInfoList;
    }

    public void changeDataSet(Vector<StatusCodeInfo> newBeanList) {
        this.statusCodeInfoList = newBeanList;
        Log.i("Press adp", "changeDataSet: ");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pressure_status_code_item, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView pressureStatsStatusCodeTv = holder.getPressureStatsStatusCodeTv();
        TextView pressureStatsStatusCountTv = holder.getPressureStatsStatusCountTv();

        Log.i("Press log adp", "onBindViewHolder: position" + position);

        StatusCodeInfo statusCodeInfo = statusCodeInfoList.get(position);

        pressureStatsStatusCodeTv.setText(statusCodeInfo.getStatusCode() + "");
        pressureStatsStatusCountTv.setText(statusCodeInfo.getCount() + "");
    }

    @Override
    public int getItemCount() {
        return statusCodeInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView pressureStatsStatusCodeTv;
        TextView pressureStatsStatusCountTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pressureStatsStatusCodeTv = itemView.findViewById(R.id.pressure_stats_status_code_tv);
            pressureStatsStatusCountTv = itemView.findViewById(R.id.pressure_stats_status_count_tv);
        }

        public TextView getPressureStatsStatusCodeTv() {
            return pressureStatsStatusCodeTv;
        }

        public TextView getPressureStatsStatusCountTv() {
            return pressureStatsStatusCountTv;
        }
    }
}
