package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

sealed interface SmallPlayerViewState {
  data class HasTrack(val title: String, val subtitle: String, val isPlaying: Boolean) : SmallPlayerViewState
  object UnActivePayer : SmallPlayerViewState
}