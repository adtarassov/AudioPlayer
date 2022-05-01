package com.adtarassov.audioplayer.ui.sreen.authorization

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AuthorizationRepository
import com.adtarassov.audioplayer.data.SharedPreferences
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationEvent.OnLoginButtonClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
  private val authorizationRepository: AuthorizationRepository,
  private val sharedPreferences: SharedPreferences
) : BaseFlowViewModel<AuthorizationViewState, AuthorizationAction, AuthorizationEvent>() {

  private val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
    viewState = AuthorizationViewState.Error("Ошибка загрузки")
  }

  private fun authorizeUser(username: String, password: String) {
    viewModelScope.launch(handler) {
      viewState = AuthorizationViewState.Loading
      val response = authorizationRepository.authUser()
      if (response.isSuccessful) {
        viewAction = AuthorizationAction.AuthorizationSuccess
//        sharedPreferences.setToken(null)
      } else {
        viewState = AuthorizationViewState.Error("Ошибка авторизации")
      }
    }
  }


  override fun obtainEvent(viewEvent: AuthorizationEvent) {
    when(viewEvent) {
      is OnLoginButtonClicked -> authorizeUser(viewEvent.username, viewEvent.password)
    }
  }
}