package com.flightinfo.app.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.HashMap
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceSynthesisManager @Inject constructor(
    private val context: Context,
) {

    private var textToSpeech: TextToSpeech? = null
    private val _synthesisState = MutableStateFlow<SynthesisState>(SynthesisState.IDLE)
    val synthesisState: StateFlow<SynthesisState> = _synthesisState

    enum class SynthesisState {
        IDLE,
        SPEAKING,
        ERROR,
        COMPLETED,
    }

    init {
        initializeTextToSpeech()
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.CHINA)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("VoiceSynthesis", "Chinese language not supported")
                    _synthesisState.value = SynthesisState.ERROR
                } else {
                    Log.d("VoiceSynthesis", "Text-to-Speech initialized successfully")
                    _synthesisState.value = SynthesisState.IDLE
                }
            } else {
                Log.e("VoiceSynthesis", "Text-to-Speech initialization failed")
                _synthesisState.value = SynthesisState.ERROR
            }
        }

        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _synthesisState.value = SynthesisState.SPEAKING
            }

            override fun onDone(utteranceId: String?) {
                _synthesisState.value = SynthesisState.COMPLETED
            }

            override fun onError(utteranceId: String?) {
                _synthesisState.value = SynthesisState.ERROR
                Log.e("VoiceSynthesis", "Speech synthesis error for utterance: $utteranceId")
            }
        })

        // 设置音频属性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build(),
            )
        }
    }

    fun speak(text: String, language: String = "zh-CN", utteranceId: String? = null) {
        textToSpeech?.let { tts ->
            if (tts.language == Locale.CHINA || setLanguageForTTS(tts, language)) {
                val params = Bundle()
                if (utteranceId != null) {
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(text, TextToSpeech.QUEUE_ADD, params, utteranceId)
                } else {
                    @Suppress("DEPRECATION")
                    val hashMap = HashMap<String, String>()
                    if (utteranceId != null) {
                        hashMap[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId
                    }
                    tts.speak(text, TextToSpeech.QUEUE_ADD, hashMap)
                }
            } else {
                Log.e("VoiceSynthesis", "Language not supported: $language")
                _synthesisState.value = SynthesisState.ERROR
            }
        }
    }

    fun speakFlightInfo(
        flightNumber: String,
        status: String,
        departure: String,
        arrival: String,
        scheduledTime: String,
        actualTime: String? = null,
    ) {
        val message = buildString {
            append("航班")
            append(flightNumber)
            append("的状态是")
            append(status)
            append("。")
            append("从")
            append(departure)
            append("飞往")
            append(arrival)
            append("。")
            append("计划起飞时间是")
            append(scheduledTime)
            append("。")
            if (actualTime != null && actualTime != scheduledTime) {
                append("实际起飞时间是")
                append(actualTime)
                append("。")
            }
        }
        speak(message)
    }

    fun speakAirportInfo(airportName: String, city: String, status: String) {
        val message = buildString {
            append(city)
            append(airportName)
            append("机场的状态是")
            append(status)
            append("。")
        }
        speak(message)
    }

    fun speakReminderSet(time: String, description: String) {
        val message = buildString {
            append("已为您设置")
            append(time)
            append("的提醒：")
            append(description)
            append("。")
        }
        speak(message)
    }

    fun speakWeatherInfo(location: String, weather: String, temperature: String) {
        val message = buildString {
            append(location)
            append("的天气是")
            append(weather)
            append("，温度")
            append(temperature)
            append("。")
        }
        speak(message)
    }

    fun speakHelpMessage() {
        val message = """
            您可以使用以下语音命令：
            查询航班状态，例如：查询航班CA1234的状态
            搜索机场信息，例如：北京机场信息
            设置提醒，例如：设置航班CA1234的提醒
            查询天气，例如：北京天气
            需要帮助时，请说：帮助
        """.trimIndent()
        speak(message)
    }

    fun speakError(message: String) {
        val errorMessage = "抱歉，$message。请重试。"
        speak(errorMessage)
    }

    fun speakSuccess(message: String) {
        val successMessage = "成功，$message。"
        speak(successMessage)
    }

    private fun setLanguageForTTS(tts: TextToSpeech, language: String): Boolean {
        val locale = when (language.lowercase()) {
            "zh-cn", "zh" -> Locale.CHINA
            "en-us", "en" -> Locale.US
            "ja-jp", "ja" -> Locale.JAPAN
            "ko-kr", "ko" -> Locale.KOREA
            else -> Locale.CHINA
        }
        return tts.setLanguage(locale) != TextToSpeech.LANG_NOT_SUPPORTED
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
        _synthesisState.value = SynthesisState.IDLE
    }

    fun isSpeaking(): Boolean {
        return textToSpeech?.isSpeaking == true
    }

    fun setSpeechRate(rate: Float) {
        textToSpeech?.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        textToSpeech?.setPitch(pitch)
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    fun reset() {
        stopSpeaking()
        _synthesisState.value = SynthesisState.IDLE
    }

    companion object {
        const val UTTERANCE_ID_FLIGHT_INFO = "flight_info"
        const val UTTERANCE_ID_AIRPORT_INFO = "airport_info"
        const val UTTERANCE_ID_REMINDER = "reminder"
        const val UTTERANCE_ID_WEATHER = "weather"
        const val UTTERANCE_ID_HELP = "help"
        const val UTTERANCE_ID_ERROR = "error"
        const val UTTERANCE_ID_SUCCESS = "success"
    }
}
