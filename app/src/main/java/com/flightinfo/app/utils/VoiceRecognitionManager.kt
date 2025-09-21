package com.flightinfo.app.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.flightinfo.app.data.model.VoiceIntent
import com.flightinfo.app.data.model.VoiceRecognitionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceRecognitionManager @Inject constructor(
    private val context: Context,
) {

    private var speechRecognizer: SpeechRecognizer? = null
    private val _recognitionState = MutableStateFlow<RecognitionState>(RecognitionState.IDLE)
    val recognitionState: StateFlow<RecognitionState> = _recognitionState

    private val _recognitionResult = MutableStateFlow<VoiceRecognitionResult?>(null)
    val recognitionResult: StateFlow<VoiceRecognitionResult?> = _recognitionResult

    enum class RecognitionState {
        IDLE,
        LISTENING,
        PROCESSING,
        ERROR,
        SUCCESS,
    }

    init {
        initializeSpeechRecognizer()
    }

    private fun initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _recognitionState.value = RecognitionState.LISTENING
                    }

                    override fun onBeginningOfSpeech() {
                        // 语音开始
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        // 音量变化
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {
                        // 缓冲区接收
                    }

                    override fun onEndOfSpeech() {
                        _recognitionState.value = RecognitionState.PROCESSING
                    }

                    override fun onError(error: Int) {
                        _recognitionState.value = RecognitionState.ERROR
                        Log.e("VoiceRecognition", "Speech recognition error: $error")
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            val recognizedText = matches[0]
                            val confidence = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)?.getOrNull(0) ?: 0f

                            val processedResult = processVoiceCommand(recognizedText, confidence)
                            _recognitionResult.value = processedResult
                            _recognitionState.value = RecognitionState.SUCCESS
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        // 部分结果
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {
                        // 事件处理
                    }
                })
            }
        } else {
            Log.e("VoiceRecognition", "Speech recognition not available")
        }
    }

    fun startListening(language: String = "zh-CN") {
        if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            _recognitionState.value = RecognitionState.ERROR
            return
        }

        speechRecognizer?.let { recognizer ->
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }

            recognizer.startListening(intent)
        }
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
    }

    fun cancelListening() {
        speechRecognizer?.cancel()
        _recognitionState.value = RecognitionState.IDLE
    }

    private fun processVoiceCommand(text: String, confidence: Float): VoiceRecognitionResult {
        val lowerText = text.lowercase(Locale.getDefault())

        return when {
            lowerText.contains("航班") && lowerText.contains("查询") -> {
                val flightNumber = extractFlightNumber(lowerText)
                VoiceRecognitionResult(
                    text = text,
                    confidence = confidence,
                    intent = VoiceIntent.FLIGHT_SEARCH,
                    parameters = mapOf("flightNumber" to flightNumber),
                )
            }

            lowerText.contains("航班") && lowerText.contains("状态") -> {
                val flightNumber = extractFlightNumber(lowerText)
                VoiceRecognitionResult(
                    text = text,
                    confidence = confidence,
                    intent = VoiceIntent.FLIGHT_STATUS,
                    parameters = mapOf("flightNumber" to flightNumber),
                )
            }

            lowerText.contains("机场") || lowerText.contains("机场信息") -> {
                val airportName = extractAirportName(lowerText)
                VoiceRecognitionResult(
                    text = text,
                    confidence = confidence,
                    intent = VoiceIntent.AIRPORT_INFO,
                    parameters = mapOf("airportName" to airportName),
                )
            }

            lowerText.contains("提醒") || lowerText.contains("设置提醒") -> {
                VoiceRecognitionResult(
                    text = text,
                    confidence = confidence,
                    intent = VoiceIntent.SET_REMINDER,
                    parameters = extractReminderParams(lowerText),
                )
            }

            lowerText.contains("天气") || lowerText.contains("天气预报") -> {
                VoiceRecognitionResult(
                    text = text,
                    confidence = confidence,
                    intent = VoiceIntent.WEATHER_INFO,
                    parameters = mapOf("location" to extractLocation(lowerText)),
                )
            }

            lowerText.contains("帮助") || lowerText.contains("怎么用") -> {
                VoiceRecognitionResult(
                    text = text,
                    confidence = confidence,
                    intent = VoiceIntent.GENERAL_HELP,
                )
            }

            else -> {
                VoiceRecognitionResult(
                    text = text,
                    confidence = confidence,
                    intent = VoiceIntent.UNKNOWN,
                )
            }
        }
    }

    private fun extractFlightNumber(text: String): String {
        val regex = Regex("""[A-Z]{2}\d{1,4}""")
        val match = regex.find(text.uppercase())
        return match?.value ?: ""
    }

    private fun extractAirportName(text: String): String {
        val keywords = listOf("北京", "上海", "广州", "深圳", "成都", "杭州", "西安", "重庆", "武汉", "南京")
        return keywords.find { text.contains(it) } ?: ""
    }

    private fun extractReminderParams(text: String): Map<String, String> {
        val params = mutableMapOf<String, String>()

        // 提取时间信息
        val timeRegex = Regex("""(\d{1,2}):(\d{2})""")
        val timeMatch = timeRegex.find(text)
        if (timeMatch != null) {
            params["time"] = timeMatch.value
        }

        // 提取航班信息
        val flightNumber = extractFlightNumber(text)
        if (flightNumber.isNotEmpty()) {
            params["flightNumber"] = flightNumber
        }

        return params
    }

    private fun extractLocation(text: String): String {
        val keywords = listOf("北京", "上海", "广州", "深圳", "成都", "杭州", "西安", "重庆", "武汉", "南京")
        return keywords.find { text.contains(it) } ?: ""
    }

    fun reset() {
        _recognitionState.value = RecognitionState.IDLE
        _recognitionResult.value = null
    }
}
