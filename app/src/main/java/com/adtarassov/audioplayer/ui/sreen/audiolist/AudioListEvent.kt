package com.adtarassov.audioplayer.ui.sreen.audiolist

import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.utils.AudioListType
import com.adtarassov.audioplayer.utils.ProfilePageType

sealed interface AudioListEvent {

  data class ViewCreated(
    val audioListType: AudioListType,
    val accountName: String,
  ) : AudioListEvent

  data class OnAudioClick(
    val model: AudioModel,
  ) : AudioListEvent
}