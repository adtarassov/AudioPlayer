package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

sealed interface SmallPlayerEvent {
  object OnPlayButtonClick : SmallPlayerEvent
}