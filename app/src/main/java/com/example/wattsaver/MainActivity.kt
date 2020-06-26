package com.example.wattsaver

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBTDevicesToSpinner()

        devicesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "Test_connection") {
                    Toast.makeText(applicationContext, "Connected successfully!", Toast.LENGTH_SHORT).show()


                }
            }

        }
    }

    private fun addBTDevicesToSpinner() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = mBluetoothAdapter.bondedDevices

        val s: MutableList<String> = ArrayList()
        s.add("<-Select device->")
        s.add("Test_connection")
        for (bt in pairedDevices) s.add(bt.name)

        devicesSpinner.adapter = ArrayAdapter(this, R.layout.list_item,
            R.id.deviceNameTextView, s)
    }
}
