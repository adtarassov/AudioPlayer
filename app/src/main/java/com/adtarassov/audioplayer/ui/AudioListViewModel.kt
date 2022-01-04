package com.adtarassov.audioplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioListRepository
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.utils.Event
import com.adtarassov.audioplayer.utils.Event.Companion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(
  private val repository: AudioListRepository
) : ViewModel() {

  private val mutableAudioList: MutableLiveData<Event<List<AudioModel>>> = MutableLiveData<Event<List<AudioModel>>>()
  val audioList: LiveData<Event<List<AudioModel>>> = mutableAudioList

  init {
    getAllAudio()
  }

  fun onAudioClick(model: AudioModel) {

  }

  fun getAllAudio() {
    viewModelScope.launch {
      mutableAudioList.postValue(Event.loading())
      val audioList = repository.getAllAudioList()
      if (audioList.isNotEmpty()) {
        mutableAudioList.postValue(Event.success(audioList))
      } else {
        mutableAudioList.postValue(Event.error("Список пуст :("))
      }
    }
  }

}