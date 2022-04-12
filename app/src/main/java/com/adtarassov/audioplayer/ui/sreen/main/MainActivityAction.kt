package com.adtarassov.audioplayer.ui.sreen.main

sealed class MainActivityAction {
  data class OnNavigationChange(val destination: Int): MainActivityAction()
}