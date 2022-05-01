package com.adtarassov.audioplayer.ui.sreen.player.fullscreen

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.databinding.FragmentFullScreenPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull


@AndroidEntryPoint
class FullScreenPlayerFragment : BottomSheetDialogFragment() {

  private val viewModel: FullScreenPlayerViewModel by viewModels()

  private var seekBarDragging = false
  private var seekBarAnimator: ValueAnimator? = null
  private var _binding: FragmentFullScreenPlayerBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentFullScreenPlayerBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
    binding.buttonPlay.setOnClickListener {
      viewModel.obtainEvent(FullScreenPlayerEvent.OnPlayButtonClick)
    }
    binding.audioProgressSeek.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
      private var currentProgressAfterChanged = 0
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
          currentProgressAfterChanged = progress
        }
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) {
        seekBarDragging = true
      }

      override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (seekBarDragging) {
          seekBarDragging = false
          viewModel.obtainEvent(FullScreenPlayerEvent.OnSeekChange(currentProgressAfterChanged))
        }
      }
    })
  }

  private fun bindViewAction(action: FullScreenPlayerAction) {
    when (action) {
      is FullScreenPlayerAction.Dismiss -> dismiss()
    }
  }

  private fun bindViewState(state: FullScreenPlayerViewState) {
    when (state) {
      is FullScreenPlayerViewState.HasTrack -> {
        binding.title.text = state.title
        binding.subtitle.text = state.subtitle
        val drawable = if (state.isPlaying) {
          ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_pause_24)
        } else {
          ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_play_arrow_24)
        }
        binding.buttonPlay.setImageDrawable(drawable)
        binding.audioProgressSeek.progress = state.audioProgress
        if (state.isPlaying) {
          binding.imageAlbum.resumeAnimation()
        } else {
          binding.imageAlbum.pauseAnimation()
        }
        seekBarAnimator?.removeAllListeners()
        seekBarAnimator?.cancel()
        seekBarAnimator = ValueAnimator.ofInt(state.audioProgress, binding.audioProgressSeek.max).apply {
          duration = if (state.timeToEndMs >= 0) state.timeToEndMs else 0
          interpolator = LinearInterpolator()
          addUpdateListener { animation ->
            val animProgress = animation.animatedValue as Int
            if (!seekBarDragging) {
              binding.audioProgressSeek.progress = animProgress
            }
          }
          if (state.isPlaying) {
            start()
          }
        }
      }
    }
  }
}