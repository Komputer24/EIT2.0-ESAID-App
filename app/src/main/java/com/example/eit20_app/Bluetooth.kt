package com.example.eit20_app

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission

class BluetoothHelper(private val activity: Activity) {

    companion object {
        const val REQUEST_ENABLE_BT = 1001
    }

    // Get BluetoothAdapter from BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    // Check if Bluetooth is enabled
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    // Request user to enable Bluetooth if disabled
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun requestEnableBluetooth() {
        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            // You might want to notify user here or handle gracefully
            return
        }

        if (!isBluetoothEnabled()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}
