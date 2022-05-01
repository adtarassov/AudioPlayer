package com.adtarassov.audioplayer.ui.sreen.authorization

sealed interface AuthorizationAction {
  object AuthorizationSuccess : AuthorizationAction
}