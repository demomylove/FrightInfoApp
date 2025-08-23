package com.flightinfo.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.flightinfo.app.R
import com.flightinfo.app.ui.fragment.WeatherFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // 获取传递的参数
        val departureAirport = intent.getStringExtra("departureAirport")
        val arrivalAirport = intent.getStringExtra("arrivalAirport")
        val flightNumber = intent.getStringExtra("flightNumber")
        val flightDate = intent.getStringExtra("flightDate")

        // 创建WeatherFragment并传递参数
        if (savedInstanceState == null) {
            val fragment = WeatherFragment().apply {
                arguments = Bundle().apply {
                    putString("departureAirport", departureAirport)
                    putString("arrivalAirport", arrivalAirport)
                    putString("flightNumber", flightNumber)
                    putString("flightDate", flightDate)
                }
            }

            supportFragmentManager.commit {
                replace(R.id.weatherFragmentContainer, fragment)
            }
        }

        // 设置标题
        title = "航班天气信息"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
