package com.adtarassov.audioplayer.ui.sreen.audiolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.databinding.FragmentAudioListBinding
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListViewState.AudioLoadFailure
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListViewState.AudioLoaded
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListViewState.Loading
import com.adtarassov.audioplayer.utils.AudioListType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class AudioListFragment : Fragment() {

  private val viewModel: AudioListViewModel by viewModels()
  private val args:  AudioListFragmentArgs by navArgs()
  private val adapter = AudioListAdapter {
    viewModel.obtainEvent(AudioListEvent.OnAudioClick(it))
  }

  private lateinit var audioListType: AudioListType

  private var _binding: FragmentAudioListBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAudioListBinding.inflate(inflater, container, false)
    audioListType = AudioListType.typeById(args.listType)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.audioListRecyclerView.adapter = adapter
    binding.audioListRecyclerView.layoutManager = LinearLayoutManager(context)
    AppCompatResources.getDrawable(view.context, R.drawable.divider_drawable)?.let {
      val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
      dividerItemDecoration.setDrawable(it)
      binding.audioListRecyclerView.addItemDecoration(dividerItemDecoration)
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
    viewModel.obtainEvent(AudioListEvent.ViewCreated)
  }

  private fun bindViewState(state: AudioListViewState) {
    when (state) {
      is AudioLoadFailure -> {

      }
      is AudioLoaded -> {
        adapter.refreshAudioList(state.list)
      }
      is Loading -> {
      }
    }
  }

  private fun bindViewAction(action: AudioListAction) {

  }

}