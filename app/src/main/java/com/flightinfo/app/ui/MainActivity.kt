package com.flightinfo.app.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.flightinfo.app.R
import com.flightinfo.app.utils.FlightTrackingManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Start the flight tracking service
        FlightTrackingManager.getInstance().startTrackingService(this)

        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))

        findViewById<Button>(R.id.price_tracking_button).setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.priceTrackingFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
