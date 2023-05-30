package com.example.bletestapp

import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    private var btPermission = false
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnScanBt = findViewById<Button>(R.id.connectBtn)
        btnScanBt.setOnClickListener {
            Log.d("BT BUTTON CLICKED", "Scan Button Clicked")
            scanBluetooth(btnScanBt)
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun scanBluetooth(view: View) {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device Doesn't Support Bluetooth", Toast.LENGTH_LONG).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bluetoothPermissionLauncher.launch(BLUETOOTH_CONNECT)
            } else {
                bluetoothPermissionLauncher.launch(BLUETOOTH_ADMIN)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private  val bluetoothPermissionLauncher = registerForActivityResult(
        RequestPermission()
    ){isGranted: Boolean ->
        if (isGranted) {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            btPermission = true
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            } else {
                btScan()
            }
        } else {
            btPermission = false
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private  val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
        btScan()
        }
    }

    private fun btScan() {
        Toast.makeText(this, "BlueTooth Connected Successfully", Toast.LENGTH_LONG).show()
        Log.d("BT CONNECTED", "Bluetooth Connected")
    }
//    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
//    private var scanning = false
//    private val handler = Handler()
//
//    // Stops scanning after 10 seconds.
//    private val SCAN_PERIOD: Long = 10000
//
//    private fun scanLeDevice() {
//        if (!scanning) { // Stops scanning after a pre-defined scan period.
//            handler.postDelayed({
//                scanning = false
//                bluetoothLeScanner.stopScan(leScanCallback)
//            }, SCAN_PERIOD)
//            scanning = true
//            bluetoothLeScanner.startScan(leScanCallback)
//        } else {
//            scanning = false
//            bluetoothLeScanner.stopScan(leScanCallback)
//        }
//    }
}