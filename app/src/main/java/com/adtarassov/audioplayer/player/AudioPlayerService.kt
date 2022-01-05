package com.adtarassov.audioplayer.player

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import dagger.hilt.android.AndroidEntryPoint

const val ACTION_PLAY: String = "com.example.action.PLAY"

@AndroidEntryPoint
class AudioPlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

  private var mediaPlayer: MediaPlayer? = null

  inner class AudioBinder : Binder() {
    fun getService(): AudioPlayerService = this@AudioPlayerService
  }

  private val binder = AudioBinder()

  override fun onBind(intent: Intent): IBinder {
    return binder
  }

  override fun onUnbind(intent: Intent): Boolean {
    return false
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    return START_STICKY
  }

//  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//    val action = intent.action
//    when (action) {
//      ACTION_PLAY -> {
////        val url = "http://........" // your URL here
//        mediaPlayer = MediaPlayer().apply {
//          setAudioAttributes(
//            AudioAttributes.Builder()
//              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//              .setUsage(AudioAttributes.USAGE_MEDIA)
//              .build()
//          )
//          setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
//          //          setDataSource(url)
//          setOnPreparedListener(this@AudioPlayerService)
//          setOnErrorListener(this@AudioPlayerService)
//          prepareAsync()
//        }
//      }
//    }
//  }

  override fun onDestroy() {
    stopSelf()
    releaseMP()
    super.onDestroy()
  }

  override fun onPrepared(mediaPlayer: MediaPlayer) {
    mediaPlayer.start()
  }

  override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
    mediaPlayer?.reset()
    return false
  }

  private fun releaseMP() {
    mediaPlayer?.release()
    mediaPlayer = null
  }
}
