package com.flightinfo.app.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
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
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        localeManager = LocaleManager(this)
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        // Apply saved language
        localeManager.setLanguage(this, localeManager.getLanguage())

        // Apply saved theme
        applyTheme()

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

        findViewById<Button>(R.id.offline_schedule_button).setOnClickListener {
            navController.navigate(R.id.offlineScheduleFragment)
        }

        findViewById<Button>(R.id.language_switch_button).setOnClickListener {
            showLanguageDialog()
        }

        // Add theme switch button to the UI
        addThemeSwitchButton()
    }

    private fun applyTheme() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkMode) {
            setTheme(R.style.Theme_FlightInfoApp_Dark)
        }
    }

    private fun addThemeSwitchButton() {
        val button = Button(this).apply {
            text = getString(R.string.switch_theme)
            setOnClickListener {
                showThemeDialog()
            }
        }

        val linearLayout = findViewById<LinearLayout>(R.id.bottom_button_container)
        linearLayout.addView(button, 0) // Add at the beginning
    }

    private fun showThemeDialog() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)

        AlertDialog.Builder(this)
            .setTitle(R.string.theme_settings)
            .setSingleChoiceItems(
                arrayOf(
                    resources.getString(R.string.theme_light),
                    resources.getString(R.string.theme_dark),
                ),
                if (isDarkMode) 1 else 0,
                null,
            )
            .setPositiveButton(R.string.restart_app) { dialog, which ->
                val selectedMode = (dialog as? AlertDialog)?.listView?.checkedItemPosition == 1
                if (selectedMode != isDarkMode) {
                    sharedPreferences.edit().putBoolean("dark_mode", selectedMode).apply()

                    // Restart app to apply theme
                    AlertDialog.Builder(this)
                        .setTitle(R.string.theme_changed)
                        .setMessage(R.string.restart_for_theme)
                        .setPositiveButton(R.string.restart_app) { _, _ ->
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
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
