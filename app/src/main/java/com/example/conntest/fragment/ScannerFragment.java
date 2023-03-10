package com.example.conntest.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.conntest.observer.DeviceScanObserver;
import com.example.conntest.adapter.DeviceListAdapter;
import com.example.conntest.common.MyContext;
import com.example.conntest.databinding.FragmentDiscoveredPanelBinding;

public class ScannerFragment extends Fragment {

    private static final String TAG = ScannerFragment.class.getSimpleName();

    private FragmentDiscoveredPanelBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiscoveredPanelBinding.inflate(getLayoutInflater());

        binding.deviceList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        MyContext.deviceTable.addObserver(new DeviceScanObserver(binding.deviceList));

        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(MyContext.deviceTable.getScanDeviceList());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.deviceList.setLayoutManager(manager);
        binding.deviceList.setAdapter(deviceListAdapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }
}
