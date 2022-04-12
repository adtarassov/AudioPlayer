package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SmallPlayerViewModel @Inject constructor(

) : BaseFlowViewModel<SmallPlayerViewState, SmallPlayerAction, SmallPlayerEvent>() {

  override fun obtainEvent(viewEvent: SmallPlayerEvent) {
    TODO("Not yet implemented")
  }

}