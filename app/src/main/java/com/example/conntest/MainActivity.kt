package com.example.conntest

import android.Manifest
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.conntest.adapter.SelectedDeviceListAdapter
import com.example.conntest.common.MyContext
import com.example.conntest.databinding.ActivityMainBinding
import com.example.conntest.fragment.PressureFragment
import com.example.conntest.fragment.SelectedPanelFragment
import com.example.conntest.fragment.ScannerFragment
import com.example.conntest.pojo.Device
import com.example.conntest.utils.BLEUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = MainActivity.javaClass.simpleName
    }

    private var binding: ActivityMainBinding? = null

    private var scanCallback: BLEUtil.OnScanResult = object : BLEUtil.OnScanResult {
        override fun onScanResult(result: ScanResult?) {
            if (result == null) return

            val device = result.device

            if (MyContext.deviceTable.containsDevice(device.address)) {

            } else {
                    MyContext.deviceTable.putScanDevice(
                        device.address,
                        Device(device.name, device.address, result.isConnectable, device)
                    )
            }
        }
    }

    private val tabFragmentList: MutableList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        BLEUtil.init(application)

        val tabLayout = binding!!.tabLayout
        val viewPager = binding!!.viewPager

        // Scanner Tab
        tabFragmentList.add(ScannerFragment())
        tabFragmentList.add(SelectedPanelFragment())
        tabFragmentList.add(PressureFragment())

        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = tabFragmentList.size
        viewPager.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return tabFragmentList.size
            }
            override fun createFragment(position: Int): Fragment {
                return tabFragmentList[position]
            }
        }

        val defaultTabName = arrayOf("Scanner", "Selected", "Pressure");
        TabLayoutMediator(tabLayout, viewPager) {
            tab, position ->
            tab.text = defaultTabName[position]
        }.attach()

        //BLEUtil.startScan(scanCallback, 5000)
    }

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
    )

    private fun isPermissionsOk(ctx: Context): Boolean {
        val items = PERMISSIONS_STORAGE;
        for (item in items) if (ContextCompat.checkSelfPermission(
                ctx, item
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) return false
        return true
    }

    private fun checkBluetoothPermission() {
        if (!BLEUtil.permissionsOK(this))
            Snackbar.make(binding!!.root, "This app need Location to use BLE", Snackbar.LENGTH_LONG)
                .setAction("OK") {
                    BLEUtil.requestBlePermissions(this, 1)
                }.show()

    }

    private fun triggerScan(v: MenuItem) {

        if (BLEUtil.isScanning) {

            BLEUtil.stopScan()
            v.title = "SCAN"
        } else {
            checkBluetoothPermission()

            MyContext.deviceTable.clearScanDeviceTable()

            BLEUtil.startScan(scanCallback, Long.MAX_VALUE / 2)
            v.title = "STOP SCANNING"
        }
    }

    private fun addTabTest() {
        tabFragmentList.add(PressureFragment())
        var tabLayout = binding!!.tabLayout

        tabLayout.addTab(tabLayout.newTab().setText("Hello"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.tool, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.scan -> triggerScan(item)
            R.id.settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            R.id.bluetooth -> checkBluetoothPermission()
            R.id.refresh -> addTabTest()
        }

        return super.onOptionsItemSelected(item)
    }

    private var recyclerView: RecyclerView? = null

    override fun onStart() {
        super.onStart()

        recyclerView = findViewById(R.id.device_list)
    }

}