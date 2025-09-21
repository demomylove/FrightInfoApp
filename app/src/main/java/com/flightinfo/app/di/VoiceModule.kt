package com.flightinfo.app.di

import android.content.Context
import com.flightinfo.app.utils.VoiceRecognitionManager
import com.flightinfo.app.utils.VoiceSynthesisManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VoiceModule {

    @Provides
    @Singleton
    fun provideVoiceRecognitionManager(
        @ApplicationContext context: Context,
    ): VoiceRecognitionManager {
        return VoiceRecognitionManager(context)
    }

    @Provides
    @Singleton
    fun provideVoiceSynthesisManager(
        @ApplicationContext context: Context,
    ): VoiceSynthesisManager {
        return VoiceSynthesisManager(context)
    }
}
