package com.adtarassov.audioplayer.ui.sreen.audiolist

import com.adtarassov.audioplayer.data.AudioListRepository
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(
  private val repository: AudioListRepository,
  private val audioManager: AudioManager
) : BaseFlowViewModel<AudioListViewState, AudioListAction, AudioListEvent>() {

  init {
    getAllAudio()
  }

  fun onAudioClick(model: AudioModel) {

  }

  fun getAllAudio() {
//    viewModelScope.launch {
//      mutableAudioList.postValue(Event.loading())
//      val audioList = repository.getAllAudioList()
//      if (audioList.isNotEmpty()) {
//        mutableAudioList.postValue(Event.success(audioList))
//      } else {
//        mutableAudioList.postValue(Event.error("Список пуст :("))
//      }
//    }
  }

  override fun obtainEvent(viewEvent: AudioListEvent) {
  }

}