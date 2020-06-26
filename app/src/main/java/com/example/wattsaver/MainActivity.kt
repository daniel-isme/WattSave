package com.example.wattsaver

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = mBluetoothAdapter.bondedDevices

        val s: MutableList<String> = ArrayList()
        for (bt in pairedDevices) s.add(bt.name)

        devicesSpinner.adapter = ArrayAdapter(this, R.layout.list_item,
            R.id.deviceNameTextView, s)
    }
}
