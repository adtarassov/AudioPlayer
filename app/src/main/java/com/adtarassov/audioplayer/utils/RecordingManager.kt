package com.adtarassov.audioplayer.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RecordingManager @Inject constructor(
  @ApplicationContext
  private val context: Context,
) {

  private val LOG_TAG = RecordingManager::class.simpleName
  private var recorder: MediaRecorder? = null

  fun startRecording(fileName: String) {
    stopRecording()
    if (VERSION.SDK_INT >= VERSION_CODES.S) {
      recorder = MediaRecorder(context).apply {
        setOutputFile(fileName)
        setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
        setAudioEncodingBitRate(16 * 44100)
        setAudioSamplingRate(44100)
        setAudioChannels(2)
        try {
          prepare()
        } catch (e: IOException) {
          Log.e(LOG_TAG, "prepare() failed")
        }

        start()
      }
    } else {
      recorder = MediaRecorder().apply {
        setOutputFile(fileName)
        setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
        setAudioEncodingBitRate(16 * 44100)
        setAudioSamplingRate(44100)
        setAudioChannels(2)
        try {
          prepare()
        } catch (e: IOException) {
          Log.e(LOG_TAG, "prepare() failed")
        }

        start()
      }
    }
  }

  fun stopRecording() {
    recorder?.apply {
      stop()
      release()
    }
    recorder = null
  }

}
