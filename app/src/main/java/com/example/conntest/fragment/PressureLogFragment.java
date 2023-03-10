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

import com.example.conntest.adapter.PressureLogAdapter;
import com.example.conntest.common.MyContext;
import com.example.conntest.databinding.FragmentPressureLogBinding;
import com.example.conntest.observer.PressureDataObserver;

import java.util.Vector;

public class PressureLogFragment extends Fragment {

    private static final String TAG = PressureLogFragment.class.getSimpleName();

    private FragmentPressureLogBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPressureLogBinding.inflate(getLayoutInflater());

        binding.pressureLogRv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        PressureLogAdapter pressureLogAdapter = new PressureLogAdapter(new Vector<>());
        binding.pressureLogRv.setAdapter(pressureLogAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.pressureLogRv.setLayoutManager(manager);

        PressureDataObserver pressureDataObserver = new PressureDataObserver(binding.pressureLogRv);
        MyContext.pressureContext.addObserver(pressureDataObserver);

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

    public FragmentPressureLogBinding getBinding() {
        return binding;
    }
}
