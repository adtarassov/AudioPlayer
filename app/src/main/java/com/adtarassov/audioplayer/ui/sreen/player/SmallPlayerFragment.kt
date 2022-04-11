package com.adtarassov.audioplayer.ui.sreen.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adtarassov.audioplayer.databinding.SmallPlayerFragmentBinding
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmallPlayerFragment: Fragment() {

  private var _binding: SmallPlayerFragmentBinding? = null
  private val binding get() = _binding!!

  @Inject
  lateinit var audioManager: AudioManager

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = SmallPlayerFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    audioManager.getAudioService().observe(this) { service ->
      val a = 1
    }
  }
}