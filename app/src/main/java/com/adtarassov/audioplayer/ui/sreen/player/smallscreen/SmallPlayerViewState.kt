package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

sealed class SmallPlayerViewState {
  data class IsPlaying(val title: String, val subtitle: String) : SmallPlayerViewState()
  object UnActivePayer : SmallPlayerViewState()
}