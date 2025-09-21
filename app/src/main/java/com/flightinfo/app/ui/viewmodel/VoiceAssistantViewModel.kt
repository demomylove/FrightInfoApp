package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.VoiceIntent
import com.flightinfo.app.data.model.VoiceRecognitionResult
import com.flightinfo.app.data.model.VoiceSettings
import com.flightinfo.app.data.repository.FlightRepository
import com.flightinfo.app.utils.VoiceRecognitionManager
import com.flightinfo.app.utils.VoiceRecognitionManager.RecognitionState
import com.flightinfo.app.utils.VoiceSynthesisManager
import com.flightinfo.app.utils.VoiceSynthesisManager.SynthesisState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceAssistantViewModel @Inject constructor(
    private val voiceRecognitionManager: VoiceRecognitionManager,
    private val voiceSynthesisManager: VoiceSynthesisManager,
    private val flightRepository: FlightRepository,
) : ViewModel() {

    private val _voiceSettings = MutableStateFlow(VoiceSettings())
    val voiceSettings = _voiceSettings.asStateFlow()

    private val _recognitionState = MutableStateFlow<RecognitionState>(RecognitionState.IDLE)
    val recognitionState = _recognitionState.asStateFlow()

    private val _synthesisState = MutableStateFlow<SynthesisState>(SynthesisState.IDLE)
    val synthesisState = _synthesisState.asStateFlow()

    private val _currentCommand = MutableStateFlow<String>("")
    val currentCommand = _currentCommand.asStateFlow()

    private val _commandHistory = MutableStateFlow<List<String>>(emptyList())
    val commandHistory = _commandHistory.asStateFlow()

    private val _assistantResponse = MutableStateFlow<String>("")
    val assistantResponse = _assistantResponse.asStateFlow()

    init {
        observeRecognitionManager()
        observeSynthesisManager()
    }

    private fun observeRecognitionManager() {
        viewModelScope.launch {
            voiceRecognitionManager.recognitionState.collect { state ->
                _recognitionState.value = state
            }
        }

        viewModelScope.launch {
            voiceRecognitionManager.recognitionResult.collect { result ->
                result?.let { handleVoiceCommand(it) }
            }
        }
    }

    private fun observeSynthesisManager() {
        viewModelScope.launch {
            voiceSynthesisManager.synthesisState.collect { state ->
                _synthesisState.value = state
            }
        }
    }

    fun startVoiceRecognition() {
        if (_voiceSettings.value.isEnabled) {
            voiceRecognitionManager.startListening(_voiceSettings.value.language)
        }
    }

    fun stopVoiceRecognition() {
        voiceRecognitionManager.stopListening()
    }

    fun cancelVoiceRecognition() {
        voiceRecognitionManager.cancelListening()
    }

    private fun handleVoiceCommand(result: VoiceRecognitionResult) {
        _currentCommand.value = result.text

        // 添加到历史记录
        _commandHistory.value = listOf(result.text) + _commandHistory.value.take(9)

        viewModelScope.launch {
            when (result.intent) {
                VoiceIntent.FLIGHT_SEARCH -> handleFlightSearch(result.parameters)
                VoiceIntent.FLIGHT_STATUS -> handleFlightStatus(result.parameters)
                VoiceIntent.AIRPORT_INFO -> handleAirportInfo(result.parameters)
                VoiceIntent.SET_REMINDER -> handleSetReminder(result.parameters)
                VoiceIntent.WEATHER_INFO -> handleWeatherInfo(result.parameters)
                VoiceIntent.BOOKING_INFO -> handleBookingInfo(result.parameters)
                VoiceIntent.GENERAL_HELP -> handleHelp()
                VoiceIntent.UNKNOWN -> handleUnknownCommand(result.text)
            }
        }
    }

    private suspend fun handleFlightSearch(parameters: Map<String, String>) {
        val flightNumber = parameters["flightNumber"]
        if (flightNumber.isNullOrEmpty()) {
            val response = "请提供航班号进行查询。"
            _assistantResponse.value = response
            if (_voiceSettings.value.voiceFeedbackEnabled) {
                voiceSynthesisManager.speakError(response)
            }
            return
        }

        try {
            flightRepository.getFlightDetails(flightNumber).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val flightInfo = resource.data
                        val response = "找到航班${flightInfo.flightNumber}，从${flightInfo.departureAirport}飞往${flightInfo.arrivalAirport}。"
                        _assistantResponse.value = response

                        if (_voiceSettings.value.voiceFeedbackEnabled) {
                            voiceSynthesisManager.speakFlightInfo(
                                flightNumber = flightInfo.flightNumber,
                                status = flightInfo.status,
                                departure = flightInfo.departureAirport,
                                arrival = flightInfo.arrivalAirport,
                                scheduledTime = flightInfo.scheduledDeparture,
                                actualTime = flightInfo.actualDeparture,
                            )
                        }
                    }
                    is Resource.Error -> {
                        val response = "查询航班信息失败：${resource.message}"
                        _assistantResponse.value = response
                        if (_voiceSettings.value.voiceFeedbackEnabled) {
                            voiceSynthesisManager.speakError("查询失败")
                        }
                    }
                    is Resource.Loading -> {
                        _assistantResponse.value = "正在查询航班信息..."
                    }
                }
            }
        } catch (e: Exception) {
            val response = "查询航班信息时出错：${e.message}"
            _assistantResponse.value = response
            if (_voiceSettings.value.voiceFeedbackEnabled) {
                voiceSynthesisManager.speakError("查询失败")
            }
        }
    }

    private suspend fun handleFlightStatus(parameters: Map<String, String>) {
        val flightNumber = parameters["flightNumber"]
        if (flightNumber.isNullOrEmpty()) {
            val response = "请提供航班号查询状态。"
            _assistantResponse.value = response
            if (_voiceSettings.value.voiceFeedbackEnabled) {
                voiceSynthesisManager.speakError(response)
            }
            return
        }

        try {
            flightRepository.getFlightDetails(flightNumber).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val flightInfo = resource.data
                        val response = "航班$flightNumber 的状态是：${flightInfo.status}"
                        _assistantResponse.value = response

                        if (_voiceSettings.value.voiceFeedbackEnabled) {
                            voiceSynthesisManager.speakSuccess("航班状态已查询")
                        }
                    }
                    is Resource.Error -> {
                        val response = "查询航班状态失败：${resource.message}"
                        _assistantResponse.value = response
                        if (_voiceSettings.value.voiceFeedbackEnabled) {
                            voiceSynthesisManager.speakError("查询失败")
                        }
                    }
                    is Resource.Loading -> {
                        _assistantResponse.value = "正在查询航班状态..."
                    }
                }
            }
        } catch (e: Exception) {
            val response = "查询航班状态时出错：${e.message}"
            _assistantResponse.value = response
            if (_voiceSettings.value.voiceFeedbackEnabled) {
                voiceSynthesisManager.speakError("查询失败")
            }
        }
    }

    private suspend fun handleAirportInfo(parameters: Map<String, String>) {
        val airportName = parameters["airportName"]
        if (airportName.isNullOrEmpty()) {
            val response = "请提供机场名称查询信息。"
            _assistantResponse.value = response
            if (_voiceSettings.value.voiceFeedbackEnabled) {
                voiceSynthesisManager.speakError(response)
            }
            return
        }

        // 暂时使用模拟数据，因为FlightRepository没有机场查询方法
        val response = "正在开发机场信息查询功能。您可以查询$airportName 相关的航班信息。"
        _assistantResponse.value = response

        if (_voiceSettings.value.voiceFeedbackEnabled) {
            voiceSynthesisManager.speakAirportInfo(
                airportName = airportName,
                city = "待开发",
                status = "正常",
            )
        }
    }

    private fun handleSetReminder(parameters: Map<String, String>) {
        val time = parameters["time"]
        val flightNumber = parameters["flightNumber"]

        if (time.isNullOrEmpty()) {
            val response = "请提供提醒时间。"
            _assistantResponse.value = response
            if (_voiceSettings.value.voiceFeedbackEnabled) {
                voiceSynthesisManager.speakError(response)
            }
            return
        }

        val description = if (flightNumber.isNullOrEmpty()) {
            "航班提醒"
        } else {
            "航班$flightNumber 提醒"
        }

        // 这里应该调用提醒设置服务
        val response = "已设置$time的提醒：$description"
        _assistantResponse.value = response

        if (_voiceSettings.value.voiceFeedbackEnabled) {
            voiceSynthesisManager.speakReminderSet(time, description)
        }
    }

    private fun handleWeatherInfo(parameters: Map<String, String>) {
        val location = parameters["location"] ?: "当前城市"
        val response = "正在查询$location的天气信息..."
        _assistantResponse.value = response

        if (_voiceSettings.value.voiceFeedbackEnabled) {
            voiceSynthesisManager.speakWeatherInfo(location, "晴朗", "25度")
        }
    }

    fun handleHelp() {
        val response = "欢迎使用语音助手！您可以说：查询航班、航班状态、机场信息、设置提醒、天气查询等。"
        _assistantResponse.value = response

        if (_voiceSettings.value.voiceFeedbackEnabled) {
            voiceSynthesisManager.speakHelpMessage()
        }
    }

    private fun handleBookingInfo(parameters: Map<String, String>) {
        val response = "预订功能正在开发中，请使用手动预订功能。"
        _assistantResponse.value = response

        if (_voiceSettings.value.voiceFeedbackEnabled) {
            voiceSynthesisManager.speakError(response)
        }
    }

    private fun handleUnknownCommand(command: String) {
        val response = "抱歉，我不理解您的命令：$command。请说'帮助'查看可用命令。"
        _assistantResponse.value = response

        if (_voiceSettings.value.voiceFeedbackEnabled) {
            voiceSynthesisManager.speakError("无法理解您的命令")
        }
    }

    fun updateVoiceSettings(settings: VoiceSettings) {
        _voiceSettings.value = settings
    }

    fun toggleVoiceEnabled() {
        _voiceSettings.value = _voiceSettings.value.copy(isEnabled = !_voiceSettings.value.isEnabled)
    }

    fun toggleVoiceFeedback() {
        _voiceSettings.value = _voiceSettings.value.copy(voiceFeedbackEnabled = !_voiceSettings.value.voiceFeedbackEnabled)
    }

    fun setLanguage(language: String) {
        _voiceSettings.value = _voiceSettings.value.copy(language = language)
    }

    fun clearHistory() {
        _commandHistory.value = emptyList()
    }

    fun clearCurrentCommand() {
        _currentCommand.value = ""
        _assistantResponse.value = ""
        voiceRecognitionManager.reset()
        voiceSynthesisManager.reset()
    }

    override fun onCleared() {
        super.onCleared()
        voiceRecognitionManager.cancelListening()
        voiceSynthesisManager.stopSpeaking()
    }
}
