package com.adtarassov.audioplayer.ui.sreen.profile

import com.adtarassov.audioplayer.data.AudioModel

sealed interface ProfileEvent {
  object OnAuthButtonClicked : ProfileEvent
}