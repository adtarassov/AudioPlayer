package com.adtarassov.audioplayer.ui.sreen.audiolist

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioListRepository
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.OnAudioClick
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.ViewCreated
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

  private fun showAudioRecommendationList() {
    viewState = AudioListViewState.Loading
    viewModelScope.launch(handler) {
      val audioList = repository.getAudioRecommendationList()
      viewState = if (audioList.isNotEmpty()) {
        AudioListViewState.AudioLoaded(audioList)
      } else {
        AudioListViewState.AudioLoadFailure("Ошибка")
      }
    }
  }

  private fun showAudioProfileList() {
    viewState = AudioListViewState.Loading
    viewModelScope.launch(handler) {
      val audioList = repository.getLocalAudio()
      viewState = if (audioList.isNotEmpty()) {
        AudioListViewState.AudioLoaded(audioList)
      } else {
        AudioListViewState.AudioLoadFailure("Ошибка")
      }
    }
  }

  override fun obtainEvent(viewEvent: AudioListEvent) {
    when (viewEvent) {
      is ViewCreated -> when (viewEvent.audioListType) {
        RECOMMENDATION -> showAudioRecommendationList()
        PROFILE -> showAudioProfileList()
        LOCAL -> {
        }
      }
      is OnAudioClick -> onAudioClick(viewEvent.model)
    }
  }

}