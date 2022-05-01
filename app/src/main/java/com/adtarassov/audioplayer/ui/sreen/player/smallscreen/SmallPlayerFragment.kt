package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.databinding.FragmentSmallPlayerBinding
import com.adtarassov.audioplayer.ui.sreen.player.fullscreen.FullScreenPlayerFragment
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.HasTrack
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.UnActivePayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class SmallPlayerFragment : Fragment() {

  private val viewModel: SmallPlayerViewModel by viewModels()

  private var _binding: FragmentSmallPlayerBinding? = null
  private val binding get() = _binding!!

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
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
    binding.buttonPlay.setOnClickListener {
      viewModel.obtainEvent(SmallPlayerEvent.OnPlayButtonClick)
    }
  }

  private fun bindViewAction(action: SmallPlayerAction) {

  }

  private fun bindViewState(state: SmallPlayerViewState) {
    when (state) {
      is HasTrack -> {
        binding.root.isVisible = true
        binding.title.text = state.title
        binding.subtitle.text = state.subtitle
        val drawable = if (state.isPlaying) {
          ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_pause_24)
        } else {
          ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_play_arrow_24)
        }
          binding.buttonPlay.setImageDrawable(drawable)
      }
      is UnActivePayer -> {
        binding.root.isVisible = false
      }
    }
  }
}