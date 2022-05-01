package com.adtarassov.audioplayer.ui.sreen.profile

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.SharedPreferences
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileEvent.OnAuthButtonClicked
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileViewState.Loading
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileViewState.Unauthorized
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val preferences: SharedPreferences
) : BaseFlowViewModel<ProfileViewState, ProfileAction, ProfileEvent>() {

  init {
    viewModelScope.launch {
      preferences.userTokenFlow.collect { userToken ->
        if (userToken == null) {
          viewState = Unauthorized
        }
      }
    }
  }

  override fun obtainEvent(viewEvent: ProfileEvent) {

  }
}