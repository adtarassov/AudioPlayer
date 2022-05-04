package com.adtarassov.audioplayer.ui.sreen.voice

sealed interface VoiceAction {
  object Empty: VoiceAction
  data class Message(val message: String): VoiceAction
}