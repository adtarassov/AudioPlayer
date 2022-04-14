package com.adtarassov.audioplayer.utils.player

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.MainThread
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.utils.player.AudioService.Companion.PlayerState.BUFFERING
import com.adtarassov.audioplayer.utils.player.AudioService.Companion.PlayerState.ENDED
import com.adtarassov.audioplayer.utils.player.AudioService.Companion.PlayerState.IDLE
import com.adtarassov.audioplayer.utils.player.AudioService.Companion.PlayerState.READY
import com.adtarassov.audioplayer.utils.player.AudioService.Companion.PlayerState.UNKNOWN
import com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.C.USAGE_MEDIA
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//https://github.com/google/ExoPlayer/blob/release-v2/RELEASENOTES.md
//https://medium.com/ideas-by-idean/implement-podcast-in-a-service-with-exoplayer-and-hilt-2-2dc07d4d6f65
//https://medium.com/@mgazar/playing-local-and-remote-media-files-on-android-using-exoplayer-d4ef53d7c369

@AndroidEntryPoint
class AudioService : LifecycleService() {

  private val binder = AudioBinder()

  private var audioPlayer: ExoPlayer? = null
  private var playerNotificationManager: PlayerNotificationManager? = null
  private lateinit var mediaSession: MediaSessionCompat

  private val currentAudio: MutableStateFlow<AudioModel?> = MutableStateFlow(null)
  val currentAudioFlow: StateFlow<AudioModel?> = currentAudio

  private val playerState: MutableStateFlow<PlayerState> = MutableStateFlow(UNKNOWN)
  val playerStateFlow: StateFlow<PlayerState> = playerState

  private val isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val isPlayingFlow: StateFlow<Boolean> = isPlaying

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

  fun playerActionForcePlay(audioModel: AudioModel?) {
    val player = audioPlayer ?: return
    player.stop()
    player.clearMediaItems()
    val item = MediaItem.fromUri(Uri.parse(audioModel?.filePath))
    player.addMediaItem(item)
    player.prepare()
    playerActionResume()
    lifecycleScope.launch {
      currentAudio.emit(audioModel)
    }
  }

  fun playerChangePlayState() {
    val prevState = audioPlayer?.isPlaying
    if (prevState == false && audioPlayer?.playbackState == Player.STATE_ENDED) {
      playerActionForcePlay(currentAudio.value)
    }
    val canPlay = prevState?.not() ?: false
    if (canPlay) {
      playerActionResume()
    } else {
      playerActionPause()
    }
  }

  private fun playerActionResume() {
    audioPlayer?.playWhenReady = true
    lifecycleScope.launch {
      this@AudioService.isPlaying.emit(true)
    }
  }

  private fun playerActionPause() {
    audioPlayer?.playWhenReady = false
    lifecycleScope.launch {
      this@AudioService.isPlaying.emit(false)
    }
  }

  private fun playerActionStop() {
    playerActionPause()
    audioPlayer?.stop()
  }

  private fun releasePlayer() {
    audioPlayer?.release()
    audioPlayer = null
  }

  inner class AudioBinder : Binder() {
    fun getService(): AudioService = this@AudioService
  }

  private inner class PlayerEventListener : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
//      lifecycleScope.launch {
//        this@AudioService.isPlaying.emit(isPlaying)
//      }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
      lifecycleScope.launch {
        when (playbackState) {
          Player.STATE_IDLE -> {
            playerState.emit(IDLE)
          }
          Player.STATE_BUFFERING -> {
            playerState.emit(BUFFERING)
          }
          Player.STATE_READY -> {
            playerState.emit(READY)
          }
          Player.STATE_ENDED -> {
            playerState.emit(ENDED)
            playerActionStop()
          }
          else -> {
            playerState.emit(UNKNOWN)
          }
        }
      }
    }
  }

  companion object {
    private const val PLAYBACK_CHANNEL_ID = "playback_channel"
    private const val PLAYBACK_NOTIFICATION_ID = 199
    private const val MEDIA_SESSION_TAG = "player_media_podcast"

    enum class PlayerState {
      IDLE, READY, BUFFERING, ENDED, UNKNOWN
    }
  }
}
