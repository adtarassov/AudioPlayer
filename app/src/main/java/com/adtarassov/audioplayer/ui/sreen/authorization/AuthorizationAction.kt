package com.adtarassov.audioplayer.ui.sreen.authorization

import com.adtarassov.audioplayer.ui.sreen.profile.ProfileAction

sealed interface AuthorizationAction {
  object AuthorizationSuccess : AuthorizationAction
  object RegistrationSuccess: AuthorizationAction
}