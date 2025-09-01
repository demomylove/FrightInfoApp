package com.flightinfo.app.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DarkModeTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    @Test
    fun testThemeModeSaving() {
        // Test saving light mode
        sharedPreferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO).apply()
        val savedMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, savedMode)

        // Test saving dark mode
        sharedPreferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_YES).apply()
        val savedDarkMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, savedDarkMode)

        // Test saving system mode
        sharedPreferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply()
        val savedSystemMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO)
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, savedSystemMode)
    }

    @Test
    fun testThemeModeDefault() {
        // Clear preferences to test default value
        sharedPreferences.edit().clear().apply()
        val defaultMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, defaultMode)
    }

    @Test
    fun testThemeModeConversion() {
        // Test that theme modes are correctly converted
        val modes = arrayOf(
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        )

        modes.forEach { mode ->
            sharedPreferences.edit().putInt("theme_mode", mode).apply()
            val retrievedMode = sharedPreferences.getInt("theme_mode", -1)
            assertEquals(mode, retrievedMode)
        }
    }
}
