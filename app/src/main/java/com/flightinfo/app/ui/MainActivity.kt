package com.flightinfo.app.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.flightinfo.app.R
import com.flightinfo.app.utils.AuthManager
import com.flightinfo.app.utils.FlightTrackingManager
import com.flightinfo.app.utils.LocaleManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authManager: AuthManager

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

        // 添加认证状态检查
        checkAuthAndNavigate(navController)

        findViewById<Button>(R.id.airport_lookup_button).setOnClickListener {
            navController.navigate(R.id.airportLookupFragment)
        }

        findViewById<Button>(R.id.price_tracking_button).setOnClickListener {
            navController.navigate(R.id.priceTrackingFragment)
        }

        findViewById<Button>(R.id.offline_schedule_button).setOnClickListener {
            navController.navigate(R.id.offlineScheduleFragment)
        }

        findViewById<Button>(R.id.recommendation_button).setOnClickListener {
            navController.navigate(R.id.recommendationFragment)
        }

        findViewById<Button>(R.id.language_switch_button).setOnClickListener {
            showLanguageDialog()
        }

        // Add theme switch button to the UI
        addThemeSwitchButton()

        // 添加个人资料按钮
        addProfileButton()
    }

    private fun checkAuthAndNavigate(navController: NavController) {
        // 检查用户是否已登录
        val isLoggedIn = authManager.getCurrentUserId() != null

        if (!isLoggedIn) {
            // 如果当前不在登录页面，导航到登录页面
            if (navController.currentDestination?.id != R.id.loginFragment &&
                navController.currentDestination?.id != R.id.registerFragment
            ) {
                navController.navigate(R.id.loginFragment)
            }
        } else {
            // 如果用户已登录但在登录页面，导航到主页面
            if (navController.currentDestination?.id == R.id.loginFragment ||
                navController.currentDestination?.id == R.id.registerFragment
            ) {
                navController.navigate(R.id.flightListFragment)
            }
        }
    }

    private fun addProfileButton() {
        val button = Button(this).apply {
            text = "个人资料"
            setOnClickListener {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController

                if (authManager.getCurrentUserId() != null) {
                    navController.navigate(R.id.profileFragment)
                } else {
                    navController.navigate(R.id.loginFragment)
                }
            }
        }

        val linearLayout = findViewById<LinearLayout>(R.id.bottom_button_container)
        linearLayout.addView(button)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flight_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return when (item.itemId) {
            R.id.action_history -> {
                navController.navigate(R.id.history_nav_graph)
                true
            }
            R.id.action_profile -> {
                if (authManager.getCurrentUserId() != null) {
                    navController.navigate(R.id.profileFragment)
                } else {
                    navController.navigate(R.id.loginFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyTheme() {
        val themeMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)

        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES) {
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
        val currentMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        AlertDialog.Builder(this)
            .setTitle(R.string.theme_settings)
            .setSingleChoiceItems(
                arrayOf(
                    resources.getString(R.string.theme_light),
                    resources.getString(R.string.theme_dark),
                    resources.getString(R.string.theme_system),
                ),
                when (currentMode) {
                    AppCompatDelegate.MODE_NIGHT_NO -> 0
                    AppCompatDelegate.MODE_NIGHT_YES -> 1
                    else -> 2
                },
                null,
            )
            .setPositiveButton(R.string.restart_app) { dialog, which ->
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                val newMode = when (selectedPosition) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }

                if (newMode != currentMode) {
                    sharedPreferences.edit().putInt("theme_mode", newMode).apply()
                    AppCompatDelegate.setDefaultNightMode(newMode)

                    // Show confirmation message
                    android.widget.Toast.makeText(
                        this,
                        R.string.theme_applied_immediately,
                        android.widget.Toast.LENGTH_SHORT,
                    ).show()
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
