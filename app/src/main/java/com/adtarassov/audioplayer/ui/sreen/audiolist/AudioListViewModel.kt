package com.adtarassov.audioplayer.ui.sreen.audiolist

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioRepository
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.data.SharedPreferences
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListAction.Empty
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.OnAudioClick
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.OnAudioLikeClick
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.OnAudioProfileClick
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.ShowAudioList
import com.adtarassov.audioplayer.utils.AudioListType.LOCAL
import com.adtarassov.audioplayer.utils.AudioListType.PROFILE
import com.adtarassov.audioplayer.utils.AudioListType.RECOMMENDATION
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(
  private val repository: AudioRepository,
  private val audioManager: AudioManager,
  private val sharedPreferences: SharedPreferences,
  ) : BaseFlowViewModel<AudioListViewState, AudioListAction, AudioListEvent>() {

  private val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
    AudioListViewState.AudioLoadFailure("Ошибка")
  }

  private fun onAudioClick(model: AudioModel) {
    audioManager.audioServiceFlow.value?.playerActionForcePlay(model)
  }

  private fun onAudioLikeClick(model: AudioModel) {
    model.isLiked = !model.isLiked
    val userToken = sharedPreferences.userAuthModelFlow.value.token
    viewModelScope.launch(handler) {
      if (!userToken.isNullOrBlank()) {
        val currentViewState = viewStates().value
        if (currentViewState is AudioListViewState.AudioLoaded) {
          viewState = AudioListViewState.AudioLoaded(currentViewState.list)
        }
        val response = repository.setLikeAudio("Bearer $userToken", model)
      }
    }
  }

  private fun onAudioProfileClick(model: AudioModel) {
    viewAction = AudioListAction.ProfileNavigate(model)
  }

  private fun showAudioRecommendationList() {
    viewState = AudioListViewState.Loading
    viewModelScope.launch(handler) {
      val audioList = repository.getAudioRecommendationList()
      viewState = if (audioList.isNotEmpty()) {
        AudioListViewState.AudioLoaded(audioList)
      } else {
        AudioListViewState.AudioLoadFailure("Список аудиозаписей рекомендаций пуст.")
      }
    }
  }

  private fun showAudioProfileList(accountName: String) {
    viewState = AudioListViewState.Loading
    viewModelScope.launch(handler) {
      val audioList = repository.getAudioProfileList(accountName)
      viewState = if (audioList.isNotEmpty()) {
        AudioListViewState.AudioLoaded(audioList)
      } else {
        AudioListViewState.AudioLoadFailure("Список аудиозаписей профиля пуст.")
      }
    }
  }

  override fun obtainEvent(viewEvent: AudioListEvent) {
    when (viewEvent) {
      is ShowAudioList -> {
        viewAction = Empty
        when (viewEvent.audioListType) {
          RECOMMENDATION -> showAudioRecommendationList()
          PROFILE -> showAudioProfileList(viewEvent.accountName)
          LOCAL -> {
          }
        }
      }
      is OnAudioLikeClick -> onAudioLikeClick(viewEvent.model)
      is OnAudioProfileClick -> onAudioProfileClick(viewEvent.model)
      is OnAudioClick -> onAudioClick(viewEvent.model)
    }
  }


}