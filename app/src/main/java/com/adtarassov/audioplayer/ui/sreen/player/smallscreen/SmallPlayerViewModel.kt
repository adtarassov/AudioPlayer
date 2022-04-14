package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.IsPlaying
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.UnActivePayer
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmallPlayerViewModel @Inject constructor(
  private val audioManager: AudioManager,
) : BaseFlowViewModel<SmallPlayerViewState, SmallPlayerAction, SmallPlayerEvent>() {

  init {
    viewModelScope.launch {
      audioManager.audioServiceFlow.collect { audioService ->
        if (audioService == null) {
          viewState = UnActivePayer
          return@collect
        }
        combine(
          audioService.currentAudioFlow,
          audioService.playerStateFlow,
          audioService.isPlayingFlow
        ) { currentAudio, playerState, isPlaying ->
          currentAudio
        }
          .mapNotNull { it }
          .collect {
            viewState = IsPlaying(it.title, it.artist ?: "")
          }
      }
    }
  }

  override fun obtainEvent(viewEvent: SmallPlayerEvent) {

  }

}