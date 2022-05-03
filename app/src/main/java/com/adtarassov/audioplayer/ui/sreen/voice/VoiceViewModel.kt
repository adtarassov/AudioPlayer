package com.adtarassov.audioplayer.ui.sreen.voice

import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(

) : BaseFlowViewModel<VoiceViewState, VoiceAction, VoiceEvent>() {

  override fun obtainEvent(viewEvent: VoiceEvent) {
  }
}