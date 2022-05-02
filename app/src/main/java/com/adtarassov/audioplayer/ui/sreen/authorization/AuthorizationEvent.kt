package com.adtarassov.audioplayer.ui.sreen.authorization

import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationFragment.Companion.AuthType

sealed interface AuthorizationEvent {
  data class OnLoginButtonClicked(
    val username: String,
    val password: String,
    val authType: AuthType
  ) : AuthorizationEvent
}