package com.adtarassov.audioplayer.utils.player

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.MainThread
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.adtarassov.audioplayer.R
import com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.C.USAGE_MEDIA
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint

//https://github.com/google/ExoPlayer/blob/release-v2/RELEASENOTES.md
//https://medium.com/ideas-by-idean/implement-podcast-in-a-service-with-exoplayer-and-hilt-2-2dc07d4d6f65
//https://medium.com/@mgazar/playing-local-and-remote-media-files-on-android-using-exoplayer-d4ef53d7c369

@AndroidEntryPoint
class AudioService : LifecycleService() {

  private val binder = AudioBinder()

  private var audioPlayer: ExoPlayer? = null
  private var playerNotificationManager: PlayerNotificationManager? = null
  private lateinit var mediaSession: MediaSessionCompat

  override fun onBind(intent: Intent): IBinder {
    super.onBind(intent)
    handleIntent(intent)
    return binder
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    handleIntent(intent)
    return START_STICKY
  }

  override fun onCreate() {
    super.onCreate()
    initialisePlayer()
    initialiseNotification()
  }

  override fun onDestroy() {
    releasePlayer()
    mediaSession.release()
    playerNotificationManager?.setPlayer(null)
    super.onDestroy()
  }

  private fun initialisePlayer() {
    if (audioPlayer == null) {
      val player = ExoPlayer.Builder(baseContext, DefaultRenderersFactory(baseContext)).build()
      val audioAttributes = AudioAttributes.Builder().run {
        setUsage(USAGE_MEDIA)
        setContentType(CONTENT_TYPE_MUSIC)
        build()
      }
      player.setAudioAttributes(audioAttributes, true)
      player.addListener(PlayerEventListener())
      audioPlayer = player
    }
  }

  private fun initialiseNotification() {
    mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG).apply {
      isActive = true
    }
    playerNotificationManager = PlayerNotificationManager.Builder(
      this,
      PLAYBACK_NOTIFICATION_ID,
      PLAYBACK_CHANNEL_ID,
    ).apply {
//        setMediaDescriptionAdapter(MediaDescriptionAdapter)
    }.build()


    playerNotificationManager?.apply {
      setMediaSessionToken(mediaSession.sessionToken)
      setPlayer(audioPlayer)
      setColorized(true)
      setUseChronometer(true)
      setSmallIcon(R.drawable.ic_baseline_home_24)
      setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
      setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
      setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
  }

  @MainThread
  private fun handleIntent(intent: Intent?) {
    // Play
    intent?.let {
      val url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
      //play(url)
    }
  }

  @MainThread
  fun play(url: String) {
    val player = audioPlayer ?: return
    player.stop()
    player.clearMediaItems()
    val item = MediaItem.fromUri(Uri.parse(url))
    player.addMediaItem(item)
    player.prepare()
    player.playWhenReady = true
  }

  @MainThread
  fun playerActionResume() {
    audioPlayer?.playWhenReady = true
  }

  @MainThread
  fun playerActionPause() {
    audioPlayer?.playWhenReady = false
  }

  private fun releasePlayer() {
    audioPlayer?.release()
    audioPlayer = null
  }

  inner class AudioBinder : Binder() {
    fun getService(): AudioService = this@AudioService
  }

  private inner class PlayerEventListener : Player.Listener {

  }

  companion object {
    private const val PLAYBACK_CHANNEL_ID = "playback_channel"
    private const val PLAYBACK_NOTIFICATION_ID = 199
    private const val MEDIA_SESSION_TAG = "player_media_podcast"
  }
}
