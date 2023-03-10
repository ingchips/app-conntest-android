package com.example.conntest.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.conntest.BuildConfig;
import com.example.conntest.adapter.PressureTabViewPager2Adapter;
import com.example.conntest.common.MyContext;
import com.example.conntest.databinding.FragmentPressurePanelBinding;
import com.example.conntest.pojo.Device;
import com.example.conntest.pojo.PressureTaskContext;
import com.example.conntest.utils.CsvUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PressureFragment extends Fragment {

    private static final String TAG = PressureFragment.class.getSimpleName();

    private FragmentPressurePanelBinding binding;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();

    PressureTaskContext pressureContext;

    private Device device;

    public PressureLogFragment getLogFragment() {
        return (fragmentList.size() == 2) ? (PressureLogFragment) fragmentList.get(0) : null;
    }
    public PressureStatsFragment getStatsFragment() {
        return (fragmentList.size() == 2) ? (PressureStatsFragment) fragmentList.get(1) : null;
    }



    private ActivityResultLauncher<Intent> requestPermission =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            }) ;
    private void checkStoragePermission() {
        if (!Environment.isExternalStorageManager()) {
            Snackbar.make(binding.getRoot(), "This app need Storage to record log", Snackbar.LENGTH_LONG)
                .setAction("OK", v -> {
                    Intent intent  = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));

                    requestPermission.launch(intent);
                }).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPressurePanelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        PressureLogFragment logFragment = new PressureLogFragment();
        PressureStatsFragment statsFragment = new PressureStatsFragment();

        fragmentList.add(logFragment);
        fragmentList.add(statsFragment);

        tabNames.add("Log");
        tabNames.add("Stats");

        binding.pressureVp2.setOffscreenPageLimit(1);
        PressureTabViewPager2Adapter viewPager2Adapter = new PressureTabViewPager2Adapter(
                fragmentList, getActivity().getSupportFragmentManager(), getLifecycle());
        binding.pressureVp2.setAdapter(viewPager2Adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.pressureTl, binding.pressureVp2,
                (tab, position) -> {
                    tab.setText(tabNames.get(position));
                });
        tabLayoutMediator.attach();


        binding.pressureStartBtn.setOnClickListener(v -> {
            //if (device == null) return;
            pressureContext = MyContext.pressureContext.getPressureContext(device.getAddress());
            pressureContext.getTaskHandle().start();
        });

        binding.pressureStopBtn.setOnClickListener(v -> {
            if (pressureContext != null)
                pressureContext.getTaskHandle().terminate();
        });

        binding.pressureExportBtn.setOnClickListener(v -> {
            checkStoragePermission();
            if (!Environment.isExternalStorageManager())
                return;

            if (pressureContext != null) {

                checkStoragePermission();

                boolean b = CsvUtil.exportCsv(pressureContext.getRoundInfoList());
                if (b) {
                    Snackbar.make(binding.getRoot(), "csv?????????????????????", Snackbar.LENGTH_LONG)
                    .setAction("?????????", v1 -> {
                        File file = new File(MyContext.csvFilePath);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        Uri uri = FileProvider.getUriForFile(getContext(),
                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                file);

                        intent.setType("text/csv");
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        intent.setDataAndType(uri, getContext().getContentResolver().getType(uri));

                        getActivity().startActivity(Intent.createChooser(intent, "Share"));

                    }).show();
                } else {
                    Toast.makeText(getContext(), "???????????????", Toast.LENGTH_SHORT);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");

        int i = MyContext.currentSelectedDevicePosition.get();

        Vector<Device> selectedDeviceList = MyContext.deviceTable.getSelectedDeviceList();

        if (i == -1 || selectedDeviceList.size() == 0) {
            this.device = null;
        } else {
            this.device = selectedDeviceList.get(i);

            Log.i(TAG, "onResume: " + device.getAddress());

            MyContext.pressureContext.getPressureContext(device.getAddress());
            MyContext.pressureContext.switchPressureContext(device.getAddress());
        }
    }

    /*
    private void updatePagerHeightForChild(View view, ViewPager2 pager) {
        int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
        int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(wMeasureSpec, hMeasureSpec);
        if (pager.getLayoutParams().height != view.getMeasuredHeight()) {
            pager.getLayoutParams().height = view.getMeasuredHeight();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");

        binding.pressureVp2.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> updatePagerHeightForChild(
                fragmentList.get(binding.pressureVp2.getCurrentItem()).getView(),
                binding.pressureVp2));
    }
    */
}
