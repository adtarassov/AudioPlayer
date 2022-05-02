package com.adtarassov.audioplayer.ui.sreen.profile

sealed interface ProfileEvent {
  object OnExitButtonClicked : ProfileEvent
}