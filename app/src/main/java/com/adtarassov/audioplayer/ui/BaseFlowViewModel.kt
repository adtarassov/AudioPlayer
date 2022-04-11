package com.adtarassov.audioplayer.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseFlowViewModel<S, A, E>: ViewModel() {

  private val _viewStates: MutableStateFlow<S?> = MutableStateFlow(null)
  fun viewStates(): StateFlow<S?> = _viewStates

  protected var viewState: S
    get() = _viewStates.value
      ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
    set(value) {
      if (_viewStates.value == value) {
        _viewStates.value = null
      }
      _viewStates.value = value
    }

  private val _viewActions: MutableStateFlow<A?> = MutableStateFlow(null)
  fun viewActions(): StateFlow<A?> = _viewActions

  protected var viewAction: A
    get() = _viewActions.value
      ?: throw UninitializedPropertyAccessException("\"viewAction\" was queried before being initialized")
    set(value) {
      /** StateFlow doesn't work with same values */
      if (_viewActions.value == value) {
        _viewActions.value = null
      }
      _viewActions.value = value
    }

  abstract fun obtainEvent(viewEvent: E)
}