package com.adtarassov.audioplayer.data

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioListRepository @Inject constructor() {
  suspend fun getAllAudioList(): List<AudioModel> {
    delay(1000)
    return listOf(createMockAudioModel(), createMockAudioModel(), createMockAudioModel(), createMockAudioModel())
  }

  private fun createMockAudioModel() = AudioModel(
    title = "Name",
    subtitle = "Subtitle"
  )
}