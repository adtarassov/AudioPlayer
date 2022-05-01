package com.adtarassov.audioplayer.ui.sreen.profile

import com.adtarassov.audioplayer.data.AudioModel

sealed interface ProfileViewState {
  object Unauthorized : ProfileViewState
  object Loading : ProfileViewState
}