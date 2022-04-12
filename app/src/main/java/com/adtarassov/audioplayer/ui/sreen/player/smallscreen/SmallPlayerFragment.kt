package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adtarassov.audioplayer.databinding.FragmentSmallPlayerBinding
import com.adtarassov.audioplayer.ui.sreen.home.HomeFragmentDirections
import com.adtarassov.audioplayer.ui.sreen.player.fullscreen.FullScreenPlayerFragment
import com.adtarassov.audioplayer.utils.AudioListType
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmallPlayerFragment: Fragment() {

  private val viewModel: SmallPlayerViewModel by viewModels()

  private var _binding: FragmentSmallPlayerBinding? = null
  private val binding get() = _binding!!

  @Inject
  lateinit var audioManager: AudioManager

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentSmallPlayerBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.setOnClickListener {
      val fragment = FullScreenPlayerFragment()
      fragment.show(parentFragmentManager, FullScreenPlayerFragment::class.java.simpleName)
    }
    audioManager.getAudioService().observe(viewLifecycleOwner) { service ->
      val a = 1
    }
  }
}