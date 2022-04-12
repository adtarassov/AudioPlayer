package com.adtarassov.audioplayer.ui.sreen.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.utils.AudioListType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(

) : BaseFlowViewModel<MainActivityViewState, MainActivityAction, MainActivityEvent>() {

  val onFragmentChangedListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
    val a = 1
    //fixme destination.parent не красится икнока при навигации из профиля в другой фрагмент // костыль через enum
  }

  override fun obtainEvent(viewEvent: MainActivityEvent) {
    TODO("Not yet implemented")
  }

}