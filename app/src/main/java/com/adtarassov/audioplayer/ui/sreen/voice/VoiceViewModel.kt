package com.adtarassov.audioplayer.ui.sreen.voice

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.data.AudioRepository
import com.adtarassov.audioplayer.data.SharedPreferences
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.voice.VoiceViewState.RECORD
import com.adtarassov.audioplayer.utils.RecordingManager
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class VoiceViewModel @Inject constructor(
  private val recordingManager: RecordingManager,
  private val audioManager: AudioManager,
  private val sharedPreferences: SharedPreferences,
  private val audioRepository: AudioRepository,
) : BaseFlowViewModel<VoiceViewState, VoiceAction, VoiceEvent>() {

  private val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
  }

  private var currentAudioPath = ""
  private var currentAudioModel: AudioModel? = null

  private fun refreshRecording() {
    viewAction = VoiceAction.Empty
    viewState = VoiceViewState.EMPTY
  }

  private fun startRecording(filePath: String, title: String, subtitle: String) {
    if (title.isBlank() || subtitle.isBlank()) {
      viewState = VoiceViewState.EMPTY
      viewAction = VoiceAction.Message("Пустые поля")
      return
    }
    viewState = RECORD
    currentAudioPath = filePath
    recordingManager.startRecording(filePath)
  }

  private fun endRecording(title: String, subtitle: String) {
    val prevViewState = viewStates().value ?: return
    if (prevViewState != RECORD) return
    viewState = VoiceViewState.RECORDING_FINISH
    recordingManager.stopRecording()
    currentAudioModel = audioRepository.audioFromFilePath(
      currentAudioPath = currentAudioPath,
      author = sharedPreferences.userAuthModelFlow.value.accountName ?: "",
      title = title,
      subtitle = subtitle
    )
    if (currentAudioModel != null) {
      audioManager.audioServiceValue()?.playerActionForcePlay(currentAudioModel)
    } else {
      viewAction = VoiceAction.Message("Ошибка воспроизведения")
    }
  }

  private fun uploadRecording(title: String, subtitle: String) {
    val userToken = sharedPreferences.userAuthModelFlow.value.token
    val audioModel = currentAudioModel
    if (userToken == null) {
      viewState = VoiceViewState.RECORDING_FINISH
      viewAction = VoiceAction.Message("Требуется авторизация")
      return
    }
    if (title.isBlank() || subtitle.isBlank()) {
      viewState = VoiceViewState.RECORDING_FINISH
      viewAction = VoiceAction.Message("Пустые поля")
      return
    }
    viewModelScope.launch(handler) {
      viewState = VoiceViewState.PROCESSING
      if (audioModel != null) {
        val file = File(audioModel.filePath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val title: RequestBody = RequestBody.create(MediaType.parse("text/plain"), title)
        val subtitle: RequestBody = RequestBody.create(MediaType.parse("text/plain"), subtitle)
        val body = Part.createFormData("file", file.name, requestFile)
        val response = audioRepository.uploadAudioFile(
          "Bearer $userToken",
          title,
          subtitle,
          body
        )
        viewState = if (response.isSuccessful) {
          viewAction = VoiceAction.Message("Загрузка прошла успешно")
          VoiceViewState.UPLOADED
        } else {
          viewAction = VoiceAction.Message("Ошибка загрузки на сервер")
          VoiceViewState.RECORDING_FINISH
        }
      }
    }
  }

  override fun obtainEvent(viewEvent: VoiceEvent) {
    when (viewEvent) {
      is VoiceEvent.RecordStart -> {
        startRecording(viewEvent.fileName, viewEvent.title, viewEvent.subtitle)
      }
      is VoiceEvent.RecordEnd -> {
        endRecording(viewEvent.title, viewEvent.subtitle)
      }
      is VoiceEvent.OnSendButtonClick -> {
        uploadRecording(viewEvent.title, viewEvent.subtitle)
      }
      is VoiceEvent.RefreshRecording -> {
        refreshRecording()
      }
    }
  }
}