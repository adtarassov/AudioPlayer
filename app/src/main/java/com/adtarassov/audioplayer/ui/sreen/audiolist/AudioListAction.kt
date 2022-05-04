package com.adtarassov.audioplayer.ui.sreen.audiolist

import com.adtarassov.audioplayer.data.AudioModel

sealed interface AudioListAction {

  data class ProfileNavigate(
    val audioModel: AudioModel
  ): AudioListAction

  object Empty: AudioListAction
}