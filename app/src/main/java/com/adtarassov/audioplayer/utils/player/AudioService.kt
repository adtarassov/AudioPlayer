package com.adtarassov.audioplayer.utils.player

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaDescription
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.adtarassov.audioplayer.R
import com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.C.USAGE_MEDIA
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class AudioService : Service() {

  private val binder = AudioBinder()
  private var mediaPlayer: ExoPlayer? = null

//  private val controlDispatcher: ControlDispatcher = DefaultControlDispatcher()
  private var notificationManager: PlayerNotificationManager? = null
  private lateinit var mediaSession: MediaSessionCompat

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

  override fun onCreate() {
    super.onCreate()
    initialisePlayer()
  }

  override fun onDestroy() {
    stopSelf()
    releaseMP()
    super.onDestroy()
  }

  private fun createPlayer(): ExoPlayer {
    val player = ExoPlayer.Builder(baseContext, DefaultRenderersFactory(baseContext)).build()
    val audioAttributes = AudioAttributes.Builder().run {
      setUsage(USAGE_MEDIA)
      setContentType(CONTENT_TYPE_MUSIC)
      build()
    }
    player.setAudioAttributes(audioAttributes, true)
    return player
  }

  private fun initialisePlayer() {
//    if (mediaPlayer == null) {
//      mediaPlayer = createPlayer()
//    }
//    try {
//      val url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
//      mediaPlayer?.clearMediaItems()
//      mediaPlayer?.addMediaItem(MediaItem.fromUri(Uri.parse(url)))
//      mediaPlayer?.prepare()
//      mediaPlayer?.play()
//      mediaPlayer?.playbackParameters = PlaybackParameters(1f, 1f)
//    } catch (e: Exception) {
//      e.printStackTrace()
//    }
  }
//
//  private fun initialiseNotification() {
//    this.mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG).apply {
//      isActive = true
//    }
//    this.notificationManager = PlayerNotificationManager.Builder(
//      this,
//      PLAYBACK_CHANNEL_ID,
//      R.string.notification_player_channel_name,
//      R.string.notification_player_channel_description,
//      PLAYBACK_NOTIFICATION_ID,
//      MediaDescription()
//    )
//
//    this.notificationManager?.apply {
//      setMediaSessionToken(mediaSession.sessionToken)
//      setPlayer(this@AudioService.player)
//      setColorized(true)
//      setUseChronometer(true)
//      setSmallIcon(R.drawable.ic_notif)
//      setBadgeIconType(Notification.BADGE_ICON_SMALL)
//      setVisibility(Notification.VISIBILITY_PRIVATE)
//      setPriority(Notification.PRIORITY_LOW)
//      setControlDispatcher(controlDispatcher)
//    }
//  }

  private fun releaseMP() {
    mediaPlayer?.release()
    mediaPlayer = null
  }

  inner class AudioBinder : Binder() {
    fun getService(): AudioService = this@AudioService
  }

  companion object {
    private const val PLAYBACK_CHANNEL_ID = "playback_channel"
    private const val PLAYBACK_NOTIFICATION_ID = 199
    private const val MEDIA_SESSION_TAG = "player_media_podcast"
  }
}