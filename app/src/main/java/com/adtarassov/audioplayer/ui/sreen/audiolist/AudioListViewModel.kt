package com.adtarassov.audioplayer.ui.sreen.audiolist

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioListHelper
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.OnAudioClick
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListEvent.ViewCreated
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(
  private val repository: AudioListHelper,
  private val audioManager: AudioManager,
) : BaseFlowViewModel<AudioListViewState, AudioListAction, AudioListEvent>() {

  private fun onAudioClick(model: AudioModel) {
    audioManager.getAudioService().value?.play(model.filePath)
  }

  private fun getAllAudio() {
    viewState = AudioListViewState.Loading
    viewModelScope.launch {
      val audioList = repository.getAllAudioList()
      viewState = if (audioList.isNotEmpty()) {
        AudioListViewState.AudioLoaded(audioList)
      } else {
        AudioListViewState.AudioLoadFailure("Ошибка")
      }
    }
  }

  override fun obtainEvent(viewEvent: AudioListEvent) {
    when (viewEvent) {
      is ViewCreated -> getAllAudio()
      is OnAudioClick -> onAudioClick(viewEvent.model)
    }
  }

}