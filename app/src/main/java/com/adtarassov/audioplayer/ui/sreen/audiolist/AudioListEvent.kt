package com.adtarassov.audioplayer.ui.sreen.audiolist

import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.utils.AudioListType

sealed interface AudioListEvent {
  data class ViewCreated(val audioListType: AudioListType) : AudioListEvent
  data class OnAudioClick(val model: AudioModel) : AudioListEvent
}