package com.adtarassov.audioplayer.ui.sreen.player.fullscreen

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.player.fullscreen.FullScreenPlayerViewState.HasTrack
import com.adtarassov.audioplayer.utils.player.AudioManager
import com.adtarassov.audioplayer.utils.player.AudioService.Companion.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullScreenPlayerViewModel @Inject constructor(
  private val audioManager: AudioManager
) : BaseFlowViewModel<FullScreenPlayerViewState, FullScreenPlayerAction, FullScreenPlayerEvent>() {

  init {
    viewModelScope.launch {
      audioManager.audioServiceFlow.collect { audioService ->
        if (audioService == null) {
          viewAction = FullScreenPlayerAction.Dismiss
          return@collect
        }
        combine(
          audioService.currentAudioFlow,
          audioService.playerStateFlow,
          audioService.isPlayingFlow
        ) { currentAudio, playerState, isPlaying ->
          CombinedModel(currentAudio, playerState, isPlaying, audioService.getPlayerPosition())
        }
          .collect { model ->
            val currentAudio = model.currentAudio
            if (currentAudio == null) {
              viewAction = FullScreenPlayerAction.Dismiss
            } else {
              val audioProgress = (model.currentPositionMs.toFloat() / currentAudio.durationMs.toFloat() * 100).toInt()
              val timeToEndMs = currentAudio.durationMs - model.currentPositionMs
              viewState = HasTrack(
                currentAudio.title,
                currentAudio.subtitle ?: "",
                model.isPlaying,
                audioProgress,
                timeToEndMs
              )
            }
          }
      }
    }
  }

  override fun obtainEvent(viewEvent: FullScreenPlayerEvent) {
    when (viewEvent) {
      is FullScreenPlayerEvent.OnPlayButtonClick -> {
        audioManager.audioServiceValue()?.playerChangePlayState()
      }
      is FullScreenPlayerEvent.OnSeekChange -> {
        audioManager.audioServiceValue()?.playerSeekChange(viewEvent.progress)
      }
    }
  }

  private class CombinedModel(
    val currentAudio: AudioModel?,
    val playerState: PlayerState,
    val isPlaying: Boolean,
    val currentPositionMs: Long,
  )

}