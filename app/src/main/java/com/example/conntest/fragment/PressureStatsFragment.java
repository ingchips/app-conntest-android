package com.example.conntest.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.conntest.R;
import com.example.conntest.adapter.PressureLogAdapter;
import com.example.conntest.adapter.PressureStatusAdapter;
import com.example.conntest.common.MyContext;
import com.example.conntest.databinding.FragmentPressureStatsBinding;
import com.example.conntest.observer.PressureDataObserver;
import com.example.conntest.observer.PressureStatsObserver;

import java.util.Vector;

public class PressureStatsFragment extends Fragment {

    private static final String TAG = PressureStatsFragment.class.getSimpleName();

    private FragmentPressureStatsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPressureStatsBinding.inflate(getLayoutInflater());

        binding.pressureStatsRv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        PressureStatusAdapter pressureStatusAdapter = new PressureStatusAdapter(new Vector<>());
        binding.pressureStatsRv.setAdapter(pressureStatusAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.pressureStatsRv.setLayoutManager(manager);

        PressureStatsObserver pressureStatsObserver = new PressureStatsObserver(this);
        MyContext.pressureContext.addObserver(pressureStatsObserver);


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

    public FragmentPressureStatsBinding getBinding() {
        return binding;
    }
}
