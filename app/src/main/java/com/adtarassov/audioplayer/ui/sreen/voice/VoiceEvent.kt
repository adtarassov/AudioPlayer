package com.adtarassov.audioplayer.ui.sreen.voice

sealed interface VoiceEvent {
  data class RecordStart(val fileName: String): VoiceEvent
  data class RecordEnd(val title: String, val subtitle: String): VoiceEvent
  object RefreshRecording: VoiceEvent
  object OnSendButtonClick: VoiceEvent
}