package com.adtarassov.audioplayer.ui.sreen.player.fullscreen

import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FullScreenPlayerViewModel @Inject constructor(

) : BaseFlowViewModel<FullScreenPlayerViewState, FullScreenPlayerAction, FullScreenPlayerEvent>() {

  override fun obtainEvent(viewEvent: FullScreenPlayerEvent) {
    TODO("Not yet implemented")
  }

}