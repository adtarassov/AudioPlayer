package com.adtarassov.audioplayer.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import com.adtarassov.audioplayer.data.api.AudioBackendApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AudioRepository @Inject constructor(
  @ApplicationContext
  private val context: Context,
  private val audioBackendApi: AudioBackendApi,
) {

  suspend fun getAudioRecommendationList(): List<AudioModel> {
    val response = audioBackendApi.getAudioRecommendation()
    val audioResponseModelList = response.body() ?: emptyList()
    return audioResponseModelList.map {
      AudioModel(
        audioId = it.audioId,
        author = it.accountName,
        title = it.name,
        subtitle = it.description,
        durationMs = it.duration,
        filePath = it.audioUrl,
        isLiked = it.isLicked
      )
    }
  }

  suspend fun uploadAudioFile(
    token: String,
    audioName: RequestBody,
    audioDescription: RequestBody,
    file: Part?,
  ) = audioBackendApi.uploadAudioFile(token, audioName, audioDescription, file)

  suspend fun setLikeAudio(token: String, audioModel: AudioModel): Response<Any> {
    val audioId = audioModel.audioId
    return if (audioModel.isLiked) {
      audioBackendApi.postLikeForAudio(token, audioId.toString())
    } else {
      audioBackendApi.deleteLikeForAudio(token, audioId.toString())
    }
  }

  suspend fun getAudioProfileList(accountName: String): List<AudioModel> {
    val response = audioBackendApi.getAudioProfile(accountName)
    val audioResponseModelList = response.body() ?: emptyList()
    return audioResponseModelList.map {
      AudioModel(
        audioId = it.audioId,
        author = it.accountName,
        title = it.name,
        subtitle = it.description,
        durationMs = it.duration,
        filePath = it.audioUrl,
        isLiked = it.isLicked
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
          Log.e(AudioRepository::class.java.simpleName, "title-$title or duration-$duration is null")
        } else {
          audioList.add(
            AudioModel(
              audioId = 0,
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

  fun audioFromFilePath(currentAudioPath: String, author: String, title: String, subtitle: String): AudioModel? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    val file = File(currentAudioPath)
    if (file.extension == "mp3") {
      val path = file.path
      mediaMetadataRetriever.setDataSource(path)
      val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
      if (duration == null) {
        Log.e(AudioRepository::class.java.simpleName, "title-$title or duration-$duration is null")
      } else {
        return AudioModel(
          audioId = 0,
          author = author,
          title = title,
          subtitle = subtitle,
          durationMs = duration,
          filePath = path,
          isLiked = false
        )
      }
    }
    return null
  }
}