package com.adtarassov.audioplayer.ui.sreen.audiolist

import com.adtarassov.audioplayer.data.AudioModel

sealed interface AudioListViewState {
  object Loading : AudioListViewState
  data class AudioLoaded(val list: List<AudioModel>): AudioListViewState
  data class AudioLoadFailure(val errorMessage: String): AudioListViewState
}