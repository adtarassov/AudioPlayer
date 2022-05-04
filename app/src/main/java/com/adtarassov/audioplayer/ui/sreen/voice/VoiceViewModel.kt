package com.adtarassov.audioplayer.ui.sreen.voice

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(

) : BaseFlowViewModel<VoiceViewState, VoiceAction, VoiceEvent>() {

  private fun refreshRecording() {
    viewState = VoiceViewState.EMPTY
  }

  private fun startRecording() {
    viewState = VoiceViewState.RECORD
  }

  private fun endRecording() {
    viewState = VoiceViewState.RECORDING_FINISH
  }

  private fun uploadRecording() {
    viewModelScope.launch {
      viewState = VoiceViewState.PROCESSING
      delay(1000)
      viewState = VoiceViewState.UPLOADED
    }
  }

  override fun obtainEvent(viewEvent: VoiceEvent) {
    when (viewEvent) {
      is VoiceEvent.RecordButtonState -> {
        if (viewEvent.press) {
          startRecording()
        } else {
          endRecording()
        }
      }
      is VoiceEvent.OnSendButtonClick -> {
        uploadRecording()
      }
      is VoiceEvent.RefreshRecording -> {
        refreshRecording()
      }
    }
  }
}