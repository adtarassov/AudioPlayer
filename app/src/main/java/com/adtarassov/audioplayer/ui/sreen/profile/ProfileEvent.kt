package com.adtarassov.audioplayer.ui.sreen.profile

import com.adtarassov.audioplayer.utils.ProfilePageType

sealed interface ProfileEvent {
  object OnExitButtonClicked : ProfileEvent
  data class ViewCreate(val profilePageType: ProfilePageType, val accountName: String) : ProfileEvent
}