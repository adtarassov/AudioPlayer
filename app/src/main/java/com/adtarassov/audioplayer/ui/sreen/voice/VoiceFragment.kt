package com.adtarassov.audioplayer.ui.sreen.voice

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adtarassov.audioplayer.databinding.FragmentVoiceBinding
import com.adtarassov.audioplayer.ui.sreen.voice.VoiceEvent.RecordButtonState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

class VoiceFragment : Fragment() {

  private var _binding: FragmentVoiceBinding? = null
  private val binding get() = _binding!!

  private val viewModel: VoiceViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentVoiceBinding.inflate(inflater, container, false)
    setOnRecordButtonTouch()
    viewModel.obtainEvent(VoiceEvent.RefreshRecording)
    return binding.root
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun setOnRecordButtonTouch() {
    binding.recordButton.setOnTouchListener { v, event ->
      if (event?.action == MotionEvent.ACTION_DOWN) {
        viewModel.obtainEvent(RecordButtonState(press = true))
      }
      if (event?.action == MotionEvent.ACTION_UP) {
        viewModel.obtainEvent(RecordButtonState(press = false))
        v.performClick()
      }
      true
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
  }

  private fun bindViewAction(action: VoiceAction) {

  }

  private fun bindViewState(state: VoiceViewState) {
    when (state) {
      is VoiceViewState.EMPTY -> {
        binding.recordButton.progress = 0f
        binding.recordButton.pauseAnimation()
      }
      is VoiceViewState.RECORD -> {
        binding.recordButton.playAnimation()
      }
      is VoiceViewState.RECORDING_FINISH -> {
        binding.recordButton.pauseAnimation()
        binding.recordButton.progress = 0f
      }
      is VoiceViewState.PROCESSING -> {

      }
      is VoiceViewState.UPLOADED -> {

      }
    }
  }


}