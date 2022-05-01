package com.adtarassov.audioplayer.ui.sreen.authorization

sealed interface AuthorizationViewState {
  object Loading : AuthorizationViewState
  data class Error(val error: String) : AuthorizationViewState
}