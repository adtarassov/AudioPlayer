package com.adtarassov.audioplayer.utils.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adtarassov.audioplayer.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
  @ApplicationContext
  private val context: Context,
  @ApplicationScope
  private val externalScope: CoroutineScope
) {
  private val audioService: MutableStateFlow<AudioService?> = MutableStateFlow(null)
  val audioServiceFlow: StateFlow<AudioService?> = audioService
  fun audioServiceValue() = audioServiceFlow.value

  private val connection = object : ServiceConnection {
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
      val binder = service as AudioService.AudioBinder
      externalScope.launch {
        audioService.emit(binder.getService())
      }
    }

    override fun onServiceDisconnected(className: ComponentName) {
      externalScope.launch {
        audioService.emit(null)
      }
    }
  }

  init {
    Intent(context, AudioService::class.java).also { intent ->
      context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
  }

}