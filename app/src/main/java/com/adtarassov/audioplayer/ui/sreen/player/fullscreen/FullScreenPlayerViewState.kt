package com.adtarassov.audioplayer.ui.sreen.player.fullscreen

import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState

sealed interface FullScreenPlayerViewState {
  data class HasTrack(
    val title: String,
    val subtitle: String,
    val isPlaying: Boolean,
    val audioProgress: Int,
    val timeToEndMs: Long
  ) : FullScreenPlayerViewState
}