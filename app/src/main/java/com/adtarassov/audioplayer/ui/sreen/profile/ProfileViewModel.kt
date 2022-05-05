package com.adtarassov.audioplayer.ui.sreen.profile

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.SharedPreferences
import com.adtarassov.audioplayer.data.SharedPreferences.Companion.UserAuthModel
import com.adtarassov.audioplayer.data.api.UserModel
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileEvent.OnExitButtonClicked
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileViewState.Loaded
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileViewState.Loading
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileViewState.Unauthorized
import com.adtarassov.audioplayer.utils.ProfilePageType.MAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val preferences: SharedPreferences,
) : BaseFlowViewModel<ProfileViewState, ProfileAction, ProfileEvent>() {

  private fun observeMainProfilePage() {
    viewModelScope.launch {
      preferences.userAuthModelFlow.collect { userAuthModel ->
        if (userAuthModel.token == null || userAuthModel.accountName == null) {
          viewState = Unauthorized
        } else {
          getUserData(userAuthModel.accountName)
        }
      }
    }
  }

  private fun getOtherProfile(accountName: String) {
    viewModelScope.launch {
      getUserData(accountName)
    }
  }

  private suspend fun getUserData(accountName: String) {
    viewState = Loading
    delay(1000)
    viewState = Loaded(UserModel(accountName, "Лучший профиль\nЛюблю прогать\nЛюблю кодить"))
  }

  override fun obtainEvent(viewEvent: ProfileEvent) {
    when (viewEvent) {
      is OnExitButtonClicked -> {
        preferences.setToken(null)
        preferences.setAccountName(null)
      }
      is ProfileEvent.ViewCreate -> {
        if (viewEvent.profilePageType == MAIN) {
          observeMainProfilePage()
        } else {
          getOtherProfile(viewEvent.accountName)
        }
      }
    }
  }
}