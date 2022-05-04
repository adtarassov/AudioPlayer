package com.adtarassov.audioplayer.ui.sreen.voice

sealed interface VoiceViewState {
  object EMPTY : VoiceViewState
  object RECORD : VoiceViewState
  object RECORDING_FINISH : VoiceViewState
  object PROCESSING : VoiceViewState
  object UPLOADED : VoiceViewState
}