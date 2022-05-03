package com.adtarassov.audioplayer.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import com.adtarassov.audioplayer.data.api.AudioBackendApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AudioListRepository @Inject constructor(
  @ApplicationContext
  private val context: Context,
  private val audioBackendApi: AudioBackendApi,
) {

  suspend fun getAudioRecommendationList(): List<AudioModel> {
    val response = audioBackendApi.getAudioRecommendation()
    val audioResponseModelList = response.body() ?: emptyList()
    return audioResponseModelList.map {
      AudioModel(
        author = it.accountName,
        title = it.name,
        subtitle = it.description,
        durationMs = 10000,
        filePath = it.audioUrl,
        isLiked = false
      )
    }
  }

  suspend fun setLikeAudio(audioModel: AudioModel): Boolean {
    delay(1000)
    val result = true
    return result
  }

  suspend fun getAudioProfileList(accountName: String): List<AudioModel> {
    val response = audioBackendApi.getAudioProfile(accountName)
    val audioResponseModelList = response.body() ?: emptyList()
    return audioResponseModelList.map {
      AudioModel(
        author = it.accountName,
        title = it.name,
        subtitle = it.description,
        durationMs = 10000,
        filePath = it.audioUrl,
        isLiked = false
      )
    }
  }

  @Deprecated("AudioListRepository::getLocalAudio deprecated")
  suspend fun getLocalAudio(): ArrayList<AudioModel> {
    delay(1000)
    val audioList = ArrayList<AudioModel>()
    val musicFolder = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    val mediaMetadataRetriever = MediaMetadataRetriever()
    musicFolder?.listFiles()?.forEach { file ->
      if (file.extension == "mp3") {
        val path = file.path
        mediaMetadataRetriever.setDataSource(path)
        val title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: ""
        val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
        if (title == null || duration == null) {
          Log.e(AudioListRepository::class.java.simpleName, "title-$title or duration-$duration is null")
        } else {
          audioList.add(
            AudioModel(
              author = artist,
              title = title,
              subtitle = null,
              durationMs = duration,
              filePath = path,
              isLiked = false
            )
          )
        }
      }
    }
    return audioList
  }
}