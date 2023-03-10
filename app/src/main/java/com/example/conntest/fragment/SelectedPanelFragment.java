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

import com.example.conntest.observer.DeviceSelectedObserver;
import com.example.conntest.adapter.SelectedDeviceListAdapter;
import com.example.conntest.common.MyContext;
import com.example.conntest.databinding.FragmentSelectedPanelBinding;

public class SelectedPanelFragment extends Fragment {
    private static final String TAG = SelectedPanelFragment.class.getSimpleName();

    private FragmentSelectedPanelBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectedPanelBinding.inflate(getLayoutInflater());


        binding.selectedDeviceList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        DeviceSelectedObserver deviceConnectedObserver = new DeviceSelectedObserver(binding.selectedDeviceList);
        MyContext.deviceTable.addObserver(deviceConnectedObserver);

        SelectedDeviceListAdapter deviceListAdapter = new SelectedDeviceListAdapter(binding.selectedDeviceList, MyContext.deviceTable.getSelectedDeviceList());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.selectedDeviceList.setLayoutManager(manager);
        binding.selectedDeviceList.setAdapter(deviceListAdapter);

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
