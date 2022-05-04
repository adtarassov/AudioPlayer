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
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setOutputFile(fileName)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
          prepare()
        } catch (e: IOException) {
          Log.e(LOG_TAG, "prepare() failed")
        }

        start()
      }
    } else {
      recorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setOutputFile(fileName)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

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
