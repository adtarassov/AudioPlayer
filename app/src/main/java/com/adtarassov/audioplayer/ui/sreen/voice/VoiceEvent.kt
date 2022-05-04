package com.adtarassov.audioplayer.ui.sreen.voice

sealed interface VoiceEvent {
  data class RecordStart(val fileName: String, val title: String, val subtitle: String): VoiceEvent
  data class RecordEnd(val title: String, val subtitle: String): VoiceEvent
  object RefreshRecording: VoiceEvent
  data class OnSendButtonClick(val title: String, val subtitle: String): VoiceEvent
}