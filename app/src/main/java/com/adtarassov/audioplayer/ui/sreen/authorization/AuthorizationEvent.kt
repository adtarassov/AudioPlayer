package com.adtarassov.audioplayer.ui.sreen.authorization

sealed interface AuthorizationEvent {
  data class OnLoginButtonClicked(val username: String, val password: String) : AuthorizationEvent
}