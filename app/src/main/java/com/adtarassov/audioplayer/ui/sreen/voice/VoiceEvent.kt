package com.adtarassov.audioplayer.ui.sreen.voice

sealed interface VoiceEvent {
  data class RecordButtonState(val press: Boolean): VoiceEvent
  object RefreshRecording: VoiceEvent
  object OnSendButtonClick: VoiceEvent
}