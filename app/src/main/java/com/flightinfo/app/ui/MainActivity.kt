package com.flightinfo.app.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.flightinfo.app.R
import com.flightinfo.app.utils.AuthManager
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

        // Request notification permission on Android 13+
        maybeRequestNotificationPermission()

        // Setup NavController after view is created
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        // Bottom navigation
        findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
            .setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.flightListFragment -> navController.navigate(R.id.flightListFragment)
                    R.id.exploreFragment -> navController.navigate(R.id.exploreFragment)
                    R.id.itineraryFragment -> navController.navigate(R.id.itineraryFragment)
                    R.id.profileFragment -> {
                        if (authManager.getCurrentUserId() != null) navController.navigate(R.id.profileFragment)
                        else navController.navigate(R.id.loginFragment)
                    }
                }
                true
            }

        // language/theme actions moved to toolbar menu

        // no dynamic buttons
    }

    private fun maybeRequestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val granted = checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!granted) {
                requestPermissions(arrayOf(permission), 1001)
            }
        }
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

    // removed addProfileButton()

    // Menu is provided by fragments (e.g., FlightListFragment) to avoid duplicate items

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
            R.id.action_theme -> {
                showThemeDialog(); true
            }
            R.id.action_language -> {
                showLanguageDialog(); true
            }
            R.id.action_recommendations -> {
                navController.navigate(R.id.recommendationFragment)
                true
            }
            R.id.action_price_tracking -> {
                navController.navigate(R.id.priceTrackingFragment)
                true
            }
            R.id.action_offline_schedule -> {
                navController.navigate(R.id.offlineScheduleFragment)
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

    private fun showThemeDialog() {
        val currentMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_theme_settings, null)
        val chipGroup = dialogView.findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGroupTheme)
        val chipLight = dialogView.findViewById<com.google.android.material.chip.Chip>(R.id.chipLight)
        val chipDark = dialogView.findViewById<com.google.android.material.chip.Chip>(R.id.chipDark)
        val chipSystem = dialogView.findViewById<com.google.android.material.chip.Chip>(R.id.chipSystem)

        when (currentMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> chipLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> chipDark.isChecked = true
            else -> chipSystem.isChecked = true
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<android.view.View>(R.id.buttonCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<android.view.View>(R.id.buttonApply).setOnClickListener {
            val selectedId = chipGroup.checkedChipId
            val newMode = when (selectedId) {
                chipLight.id -> AppCompatDelegate.MODE_NIGHT_NO
                chipDark.id -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            if (newMode != currentMode) {
                sharedPreferences.edit().putInt("theme_mode", newMode).apply()
                AppCompatDelegate.setDefaultNightMode(newMode)
                android.widget.Toast.makeText(this, R.string.theme_applied_immediately, android.widget.Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showLanguageDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_language_settings, null)
        val chipGroup = dialogView.findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGroupLanguage)
        val chipEnglish = dialogView.findViewById<com.google.android.material.chip.Chip>(R.id.chipEnglish)
        val chipChinese = dialogView.findViewById<com.google.android.material.chip.Chip>(R.id.chipChinese)

        val currentLanguage = localeManager.getLanguage()
        when (currentLanguage) {
            LocaleManager.LANGUAGE_CHINESE -> chipChinese.isChecked = true
            else -> chipEnglish.isChecked = true
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonConfirm).setOnClickListener {
            val selectedLanguage = when (chipGroup.checkedChipId) {
                chipChinese.id -> LocaleManager.LANGUAGE_CHINESE
                else -> LocaleManager.LANGUAGE_ENGLISH
            }

            if (selectedLanguage != currentLanguage) {
                localeManager.setLanguage(this, selectedLanguage)
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.language_changed))
                    .setMessage(getString(R.string.restart_app))
                    .setPositiveButton(getString(R.string.restart_app)) { _, _ ->
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
