package com.adtarassov.audioplayer.ui.sreen.authorization

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.UserRepository
import com.adtarassov.audioplayer.data.SharedPreferences
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationAction.RegistrationSuccess
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationEvent.OnLoginButtonClicked
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationFragment.Companion.AuthType.AUTHORIZATION
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationFragment.Companion.AuthType.REGISTRATION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
  private val userRepository: UserRepository,
  private val sharedPreferences: SharedPreferences,
) : BaseFlowViewModel<AuthorizationViewState, AuthorizationAction, AuthorizationEvent>() {

  private val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
    viewState = AuthorizationViewState.Error("Ошибка загрузки")
  }

  private fun authorizeUser(username: String, password: String) {
    viewModelScope.launch(handler) {
      viewState = AuthorizationViewState.Loading
      val response = userRepository.authorizeUser(username, password)
      val tokenAccess = response.body()?.tokenAccess
      if (response.isSuccessful && tokenAccess != null) {
        viewAction = AuthorizationAction.AuthorizationSuccess
        sharedPreferences.setToken(tokenAccess)
        sharedPreferences.setAccountName(username)
      } else {
        viewState = AuthorizationViewState.Error("Ошибка авторизации")
      }
    }
  }

  private fun registerUser(username: String, password: String) {
    viewModelScope.launch(handler) {
      viewState = AuthorizationViewState.Loading
      val response = userRepository.registerUser(username, password)
      if (response.isSuccessful) {
        viewAction = RegistrationSuccess
        authorizeUser(username, password)
      } else {
        viewState = AuthorizationViewState.Error("Ошибка регистрации")
      }
    }
  }


  override fun obtainEvent(viewEvent: AuthorizationEvent) {
    when (viewEvent) {
      is OnLoginButtonClicked -> {
        when (viewEvent.authType) {
          REGISTRATION -> registerUser(viewEvent.username, viewEvent.password)
          AUTHORIZATION -> authorizeUser(viewEvent.username, viewEvent.password)
        }
      }
    }
  }
}