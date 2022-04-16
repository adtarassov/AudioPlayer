package com.adtarassov.audioplayer.ui.sreen.player.fullscreen

sealed interface FullScreenPlayerEvent {
  object OnPlayButtonClick : FullScreenPlayerEvent
  data class OnSeekChange(val progress: Int) : FullScreenPlayerEvent
}