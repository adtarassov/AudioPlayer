package com.adtarassov.audioplayer.utils.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
  @ApplicationContext private val context: Context
) {
  private val audioService: MutableLiveData<AudioService> = MutableLiveData()
  fun getAudioService(): LiveData<AudioService> = audioService
  private var bound: Boolean = false

  private val connection = object : ServiceConnection {
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
      val binder = service as AudioService.AudioBinder
      audioService.postValue(binder.getService())
      bound = true
    }

    override fun onServiceDisconnected(className: ComponentName) {
      bound = false
    }
  }

  init {
    Intent(context, AudioService::class.java).also { intent ->
      context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
  }

}