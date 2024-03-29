package com.adtarassov.audioplayer.ui.sreen.audiolist

import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.utils.AudioListType
import com.adtarassov.audioplayer.utils.ProfilePageType

sealed interface AudioListEvent {

  data class ShowAudioList(
    val audioListType: AudioListType,
    val accountName: String,
  ) : AudioListEvent

  data class OnAudioClick(
    val model: AudioModel,
  ) : AudioListEvent

  data class OnAudioLikeClick(
    val model: AudioModel,
  ) : AudioListEvent

  data class OnAudioProfileClick(
    val model: AudioModel,
  ) : AudioListEvent
}