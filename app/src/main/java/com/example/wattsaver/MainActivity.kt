package com.example.wattsaver

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var pulseCountFor60sec = 0f

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

                    pulseGenerator()

                    initChartSettings()

                    updateChart()
                }
            }

        }
    }

    private fun updateChart() {
        var i = 0f
        val entries = ArrayList<Entry>()
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val rand = (0 + Math.random() * 40).toFloat()
                entries.add(Entry(i, pulseCountFor60sec / 60))

                if (entries.count() > 60) {
                    pulseCountFor60sec -= entries[0].y
                    entries.removeAt(0)
                }
                val vl = LineDataSet(entries, "My Type")

                vl.setDrawValues(false)
                vl.setDrawFilled(true)
                vl.lineWidth = 3f

                lineChart.data = LineData(vl)
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()

                i += 1f
                handler.postDelayed(this, 1000) // set time here to refresh textView
            }
        })
    }

    private fun pulseGenerator() {
        val handler = Handler()
        var randMillis = (100..7000).random().toLong()
        handler.post(object : Runnable {
            override fun run() {
                pulsarRadio.isChecked = true
                pulseCountFor60sec += 1
                when {
                    randMillis in 100..3000 -> {
                        randMillis += (-100..100).random()
                    }
                    randMillis > 3000 -> {
                        randMillis += (-100..0).random()
                    }
                    randMillis < 100 -> {
                        randMillis += (0..100).random()
                    }
                }
                pulsarRadio.isChecked = false
                handler.postDelayed(this, randMillis)
            }
        })
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

    private fun initChartSettings() {
        lineChart.xAxis.labelRotationAngle = 0f

        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")
    }
}
