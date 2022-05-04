package com.adtarassov.audioplayer.ui.sreen.voice

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adtarassov.audioplayer.databinding.FragmentVoiceBinding
import com.adtarassov.audioplayer.utils.extentions.setInvisible
import com.adtarassov.audioplayer.utils.extentions.setVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import java.util.UUID

@AndroidEntryPoint
class VoiceFragment : Fragment() {

  private var _binding: FragmentVoiceBinding? = null
  private val binding get() = _binding!!

  private var canMakeRecord = false

  private val viewModel: VoiceViewModel by viewModels()

  private val REQUEST_RECORD_AUDIO_PERMISSION = 200
  private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
  private var audioRecordingPermissionGranted = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentVoiceBinding.inflate(inflater, container, false)
    setOnRecordButtonTouch()
    viewModel.obtainEvent(VoiceEvent.RefreshRecording)
    return binding.root
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun setOnRecordButtonTouch() {
    binding.recordButton.setOnTouchListener { v, event ->
      if (event?.action == MotionEvent.ACTION_DOWN && canMakeRecord) {
        startRecordEvent()
      }
      if (event?.action == MotionEvent.ACTION_UP && canMakeRecord) {
        endRecordEvent()
      }
      true
    }
  }

  private fun startRecordEvent() {
//    if (!audioRecordingPermissionGranted) {
//      ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)
//      return
//    }
    val uuid: String = UUID.randomUUID().toString()
    val dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    val fileName = "$dir/$uuid.mp3"
    viewModel.obtainEvent(VoiceEvent.RecordStart(fileName = fileName))
  }

  private fun endRecordEvent() {
//    if (!audioRecordingPermissionGranted) {
//      ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)
//      return
//    }
    val title = binding.recordName.text.toString()
    val subtitle = binding.recordDescription.text.toString()
    viewModel.obtainEvent(VoiceEvent.RecordEnd(title, subtitle))
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray,
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
      audioRecordingPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
      setOnRecordButtonTouch()
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
  }

  private fun bindViewAction(action: VoiceAction) {

  }

  private fun bindViewState(state: VoiceViewState) {
    when (state) {
      is VoiceViewState.EMPTY -> {
        canMakeRecord = true
        binding.recordButton.progress = 0f
        binding.recordButton.pauseAnimation()
        binding.actionButton.setInvisible()
        enableButtonsEditting(true)
        binding.recordName.setText("")
        binding.recordDescription.setText("")
      }
      is VoiceViewState.RECORD -> {
        canMakeRecord = true
        binding.recordButton.playAnimation()
        binding.actionButton.setInvisible()
        enableButtonsEditting(false)
      }
      is VoiceViewState.RECORDING_FINISH -> {
        canMakeRecord = true
        binding.recordButton.pauseAnimation()
        binding.recordButton.progress = 0f
        binding.actionButton.isClickable = true
        binding.actionButton.setVisible()
        binding.actionButton.text = "Отправить"
        binding.actionButton.setOnClickListener {
          viewModel.obtainEvent(VoiceEvent.OnSendButtonClick)
          binding.recordName.isClickable = true
          binding.recordButton.isClickable = true
        }
        enableButtonsEditting(true)
      }
      is VoiceViewState.PROCESSING -> {
        canMakeRecord = false
        binding.actionButton.isClickable = false
        binding.actionButton.setVisible()
        binding.actionButton.text = "Отправка"
        enableButtonsEditting(false)
      }
      is VoiceViewState.UPLOADED -> {
        canMakeRecord = false
        binding.actionButton.setVisible()
        binding.actionButton.text = "Записать новое аудио"
        binding.actionButton.isClickable = true
        binding.actionButton.setOnClickListener {
          viewModel.obtainEvent(VoiceEvent.RefreshRecording)
        }
        enableButtonsEditting(false)
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    viewModel.obtainEvent(VoiceEvent.RefreshRecording)
  }

  private fun enableButtonsEditting(enable: Boolean) {
    binding.recordName.isEnabled = enable
    binding.recordDescription.isEnabled = enable
  }


}