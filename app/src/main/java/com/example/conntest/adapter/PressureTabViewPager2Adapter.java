package com.example.conntest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class PressureTabViewPager2Adapter extends FragmentStateAdapter {

    private List<Fragment> localFragmentList;

    public PressureTabViewPager2Adapter(List<Fragment> fragmentList, FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

        this.localFragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return localFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return localFragmentList.size();
    }
}
