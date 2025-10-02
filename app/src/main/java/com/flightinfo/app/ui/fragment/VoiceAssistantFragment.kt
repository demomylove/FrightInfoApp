package com.flightinfo.app.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flightinfo.app.databinding.FragmentVoiceAssistantBinding
import com.flightinfo.app.ui.viewmodel.VoiceAssistantViewModel
import com.flightinfo.app.utils.VoiceRecognitionManager.RecognitionState
import com.flightinfo.app.utils.VoiceSynthesisManager.SynthesisState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VoiceAssistantFragment : Fragment() {

    private var _binding: FragmentVoiceAssistantBinding? = null
    val binding get() = _binding!!

    private val viewModel: VoiceAssistantViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var isRecording = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            startVoiceRecognition()
        } else {
            showPermissionDeniedMessage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVoiceAssistantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupViews() {
        binding.tvStatus.text = getString(com.flightinfo.app.R.string.voice_assistant_ready)
        binding.tvLanguage.text = "语言：中文（简体）"
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recognitionState.collect { state ->
                updateRecognitionState(state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.synthesisState.collect { state ->
                updateSynthesisState(state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentCommand.collect { command ->
                if (command.isNotEmpty()) {
                    binding.tvCurrentCommand.text = "正在识别：$command"
                    binding.tvCurrentCommand.visibility = View.VISIBLE
                } else {
                    binding.tvCurrentCommand.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.assistantResponse.collect { response ->
                if (response.isNotEmpty()) {
                    binding.tvResponse.text = response
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabVoiceButton.setOnClickListener {
            if (isRecording) {
                stopVoiceRecognition()
            } else {
                checkPermissionAndStartRecording()
            }
        }

        binding.ivSettings.setOnClickListener {
            showSettingsDialog()
        }

        binding.ivHistory.setOnClickListener {
            showHistoryDialog()
        }

        binding.ivHelp.setOnClickListener {
            viewModel.handleHelp()
        }
    }

    private fun checkPermissionAndStartRecording() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO,
            ) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceRecognition()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startVoiceRecognition() {
        viewModel.startVoiceRecognition()
        isRecording = true
        updateVoiceButtonUI(true)
    }

    private fun stopVoiceRecognition() {
        viewModel.stopVoiceRecognition()
        isRecording = false
        updateVoiceButtonUI(false)
    }

    private fun updateRecognitionState(state: RecognitionState) {
        when (state) {
            RecognitionState.IDLE -> {
                binding.tvStatus.text = getString(com.flightinfo.app.R.string.voice_assistant_ready)
                hideWaveAnimation()
                isRecording = false
                updateVoiceButtonUI(false)
            }
            RecognitionState.LISTENING -> {
                binding.tvStatus.text = "正在听取您的语音..."
                showWaveAnimation()
                isRecording = true
                updateVoiceButtonUI(true)
            }
            RecognitionState.PROCESSING -> {
                binding.tvStatus.text = "正在处理语音..."
                hideWaveAnimation()
                isRecording = false
                updateVoiceButtonUI(false)
            }
            RecognitionState.ERROR -> {
                binding.tvStatus.text = "语音识别失败，请重试"
                hideWaveAnimation()
                isRecording = false
                updateVoiceButtonUI(false)
            }
            RecognitionState.SUCCESS -> {
                binding.tvStatus.text = "语音识别成功"
                hideWaveAnimation()
                isRecording = false
                updateVoiceButtonUI(false)

                // 2秒后重置状态
                handler.postDelayed({
                    binding.tvStatus.text = getString(com.flightinfo.app.R.string.voice_assistant_ready)
                }, 2000)
            }
        }
    }

    private fun updateSynthesisState(state: SynthesisState) {
        when (state) {
            SynthesisState.SPEAKING -> {
                binding.tvStatus.text = "正在语音回复..."
            }
            SynthesisState.COMPLETED -> {
                binding.tvStatus.text = getString(com.flightinfo.app.R.string.voice_assistant_ready)
            }
            SynthesisState.ERROR -> {
                binding.tvStatus.text = "语音合成失败"
            }
            SynthesisState.IDLE -> {
                // 空状态，不做处理
            }
        }
    }

    private fun updateVoiceButtonUI(isRecording: Boolean) {
        if (isRecording) {
            binding.fabVoiceButton.setImageResource(com.flightinfo.app.R.drawable.ic_stop)
            binding.fabVoiceButton.backgroundTintList =
                android.content.res.ColorStateList.valueOf(Color.RED)

            // 添加脉冲动画
            val scaleAnimation = ScaleAnimation(
                1f,
                1.1f,
                1f,
                1.1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
            ).apply {
                duration = 500
                repeatCount = Animation.INFINITE
                repeatMode = Animation.REVERSE
            }
            binding.fabVoiceButton.startAnimation(scaleAnimation)
        } else {
            binding.fabVoiceButton.setImageResource(com.flightinfo.app.R.drawable.ic_mic)
            binding.fabVoiceButton.backgroundTintList =
                android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), com.flightinfo.app.R.color.primary),
                )
            binding.fabVoiceButton.clearAnimation()
        }
    }

    private fun showWaveAnimation() {
        binding.llWaveAnimation.visibility = View.VISIBLE

        val bars = listOf(
            binding.bar1,
            binding.bar2,
            binding.bar3,
            binding.bar4,
            binding.bar5,
        )

        bars.forEachIndexed { index, bar ->
            bar.visibility = View.VISIBLE
            val delay = index * 100L
            handler.postDelayed({
                bar.post {
                    val height = (20 + (40 - 20) * Math.random()).toInt()
                    bar.layoutParams.height = height
                    bar.requestLayout()
                }
            }, delay)
        }

        // 开始波形动画
        startWaveAnimation()
    }

    private fun hideWaveAnimation() {
        binding.llWaveAnimation.visibility = View.GONE
        waveAnimationRunning = false
    }

    private var waveAnimationRunning = false
    private fun startWaveAnimation() {
        if (waveAnimationRunning) return
        waveAnimationRunning = true

        val bars = listOf(binding.bar1, binding.bar2, binding.bar3, binding.bar4, binding.bar5)

        val runnable = object : Runnable {
            override fun run() {
                if (!waveAnimationRunning || !isRecording) return

                bars.forEach { bar ->
                    bar.post {
                        val height = (20 + (40 - 20) * Math.random()).toInt()
                        bar.layoutParams.height = height
                        bar.requestLayout()
                    }
                }

                handler.postDelayed(this, 200)
            }
        }

        handler.post(runnable)
    }

    private fun showPermissionDeniedMessage() {
        binding.tvResponse.text = "需要录音权限才能使用语音识别功能。请在设置中授予权限。"
    }

    private fun showSettingsDialog() {
        // TODO: 实现设置对话框
        binding.tvResponse.text = "设置功能正在开发中..."
    }

    private fun showHistoryDialog() {
        // TODO: 实现历史记录对话框
        binding.tvResponse.text = "历史记录功能正在开发中..."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        waveAnimationRunning = false
        _binding = null
    }
}
