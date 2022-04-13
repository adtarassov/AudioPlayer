package com.adtarassov.audioplayer.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AudioListHelper @Inject constructor(
  @ApplicationContext
  private val context: Context,
) {

  suspend fun getAllAudioList(): List<AudioModel> {
    delay(1000)
    return getAllLocalAudio()
  }

  private fun getAllLocalAudio(): ArrayList<AudioModel> {
    val audioList = ArrayList<AudioModel>()
    val musicFolder = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    val mediaMetadataRetriever = MediaMetadataRetriever()
    musicFolder?.listFiles()?.forEach { file ->
      if (file.extension == "mp3") {
        val path = file.path
        mediaMetadataRetriever.setDataSource(path)
        val title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
        val album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        if (title == null || duration == null) {
          Log.e(AudioListHelper::class.java.simpleName, "title-$title or duration-$duration is null")
        } else {
          audioList.add(
            AudioModel(
              title = title,
              artist = artist,
              durationMs = duration,
              album = album,
              filePath = path
            )
          )
        }
      }
    }
    return audioList
  }
}