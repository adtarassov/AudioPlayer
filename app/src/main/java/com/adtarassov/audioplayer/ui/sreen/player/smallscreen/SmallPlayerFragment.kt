package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adtarassov.audioplayer.databinding.FragmentSmallPlayerBinding
import com.adtarassov.audioplayer.ui.sreen.player.fullscreen.FullScreenPlayerFragment
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.IsPlaying
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.UnActivePayer
import com.adtarassov.audioplayer.utils.player.AudioManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

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
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
  }

  private fun bindViewAction(action: SmallPlayerAction) {

  }

  private fun bindViewState(state: SmallPlayerViewState) {
    when(state) {
      is IsPlaying -> {
        binding.root.isVisible = true
        Log.d("adtarassov", "${state.title}, ${state.subtitle}")
      }
      is UnActivePayer -> {
        binding.root.isVisible = false
      }
    }
  }
}