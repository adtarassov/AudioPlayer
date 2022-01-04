package com.adtarassov.audioplayer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Response

open class BaseViewModel : ViewModel() {

  fun <T> requestWithLiveData(
    liveData: MutableLiveData<Event<T>>,
    request: suspend () -> Response<T>
  ) {
    liveData.postValue(Event.loading())
    viewModelScope.launch {
      try {
        val result = request.invoke()
        val body = result.body()
        if (result.isSuccessful) liveData.postValue(Event.success(body))
        else liveData.postValue(Event.error(result.message()))
      } catch (e: Exception) {
        e.printStackTrace()
        liveData.postValue(Event.error(e.message.toString()))
      }
    }
  }

  fun <T> requestWithCallback(
    request: suspend () -> Response<T>,
    response: (Event<T>) -> Unit
  ) {
    response(Event.loading())
    viewModelScope.launch {
      try {
        val result = request.invoke()
        val body = result.body()
        if (result.isSuccessful) response(Event.success(body))
        else response(Event.error(result.message()))
      } catch (e: Exception) {
        e.printStackTrace()
        response(Event.error(e.message.toString()))
      }
    }
  }
}