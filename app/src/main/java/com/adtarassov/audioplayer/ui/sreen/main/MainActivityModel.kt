package com.adtarassov.audioplayer.ui.sreen.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.utils.AudioListType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityModel @Inject constructor(

) :
  BaseFlowViewModel<MainActivityViewState, MainActivityAction, MainActivityEvent>() {

  val onFragmentChangedListener = NavController.OnDestinationChangedListener { _, _, arguments ->
    val args = arguments ?: return@OnDestinationChangedListener
//    val listType = AudioListType.typeById(args[AudioListType.BUNDLE_KEY] as Int)
//    binding.toolbar.subtitle = AudioListType.getToolbarSubtitleByType(listType)
  }

  override fun obtainEvent(viewEvent: MainActivityEvent) {
    TODO("Not yet implemented")
  }

}