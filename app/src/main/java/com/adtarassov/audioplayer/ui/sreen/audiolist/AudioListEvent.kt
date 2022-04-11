package com.adtarassov.audioplayer.ui.sreen.audiolist

import com.adtarassov.audioplayer.data.AudioModel

sealed class AudioListEvent {
  object ViewCreated : AudioListEvent()
  data class OnAudioClick(val model: AudioModel) : AudioListEvent()
}