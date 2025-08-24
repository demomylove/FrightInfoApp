package com.flightinfo.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.flightinfo.app.R
import com.flightinfo.app.utils.FlightTrackingManager
import com.flightinfo.app.utils.LocaleManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var localeManager: LocaleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        localeManager = LocaleManager(this)

        // Apply saved language
        localeManager.setLanguage(this, localeManager.getLanguage())

        setContentView(R.layout.activity_main)

        // Start the flight tracking service
        FlightTrackingManager.getInstance().startTrackingService(this)

        // Setup NavController after view is created
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        findViewById<Button>(R.id.airport_lookup_button).setOnClickListener {
            navController.navigate(R.id.airportLookupFragment)
        }

        findViewById<Button>(R.id.price_tracking_button).setOnClickListener {
            navController.navigate(R.id.priceTrackingFragment)
        }

        findViewById<Button>(R.id.language_switch_button).setOnClickListener {
            showLanguageDialog()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showLanguageDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_language_settings, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupLanguage)

        // Set current language selection
        val currentLanguage = localeManager.getLanguage()
        when (currentLanguage) {
            LocaleManager.LANGUAGE_CHINESE -> radioGroup.check(R.id.radioButtonChinese)
            else -> radioGroup.check(R.id.radioButtonEnglish)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.language_settings))
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonConfirm).setOnClickListener {
            val selectedLanguage = when (radioGroup.checkedRadioButtonId) {
                R.id.radioButtonChinese -> LocaleManager.LANGUAGE_CHINESE
                else -> LocaleManager.LANGUAGE_ENGLISH
            }

            if (selectedLanguage != currentLanguage) {
                localeManager.setLanguage(this, selectedLanguage)

                // Show restart message
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.language_changed))
                    .setMessage(getString(R.string.restart_app))
                    .setPositiveButton(getString(R.string.restart_app)) { _, _ ->
                        // Restart the app
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }

            dialog.dismiss()
        }

        dialog.show()
    }
}
