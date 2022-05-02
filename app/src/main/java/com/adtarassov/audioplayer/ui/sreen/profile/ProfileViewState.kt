package com.adtarassov.audioplayer.ui.sreen.profile

import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.data.api.UserModel

sealed interface ProfileViewState {
  object Unauthorized : ProfileViewState
  object Loading : ProfileViewState
  data class Loaded(val user: UserModel) : ProfileViewState
}