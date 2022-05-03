package com.adtarassov.audioplayer.ui.sreen.audiolist

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioListRepository
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.OnAudioClick
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.OnAudioLikeClick
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
  private val repository: AudioListRepository,
  private val audioManager: AudioManager,
) : BaseFlowViewModel<AudioListViewState, AudioListAction, AudioListEvent>() {

  private val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
    AudioListViewState.AudioLoadFailure("Ошибка")
  }

  private fun onAudioClick(model: AudioModel) {
    audioManager.audioServiceFlow.value?.playerActionForcePlay(model)
  }

  private fun onAudioLikeClicked(model: AudioModel) {
    viewModelScope.launch(handler) {
    }
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
      is ShowAudioList -> when (viewEvent.audioListType) {
        RECOMMENDATION -> showAudioRecommendationList()
        PROFILE -> showAudioProfileList(viewEvent.accountName)
        LOCAL -> {
        }
      }
      is OnAudioLikeClick -> onAudioLikeClicked(viewEvent.model)
      is OnAudioClick -> onAudioClick(viewEvent.model)
    }
  }

}