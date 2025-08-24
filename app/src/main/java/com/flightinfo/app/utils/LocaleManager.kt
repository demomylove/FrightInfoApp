package com.flightinfo.app.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class LocaleManager(context: Context) {
    
    companion object {
        private const val PREF_NAME = "locale_prefs"
        private const val KEY_LANGUAGE = "language"
        
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_CHINESE = "zh"
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    fun setLanguage(context: Context, language: String) {
        persistLanguage(language)
        updateLanguage(context, language)
    }
    
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
    }
    
    private fun persistLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }
    
    private fun updateLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    
    fun getCurrentLocale(context: Context): Locale {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }
}