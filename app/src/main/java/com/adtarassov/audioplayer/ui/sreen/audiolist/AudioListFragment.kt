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

@AndroidEntryPoint
class AudioListFragment : Fragment() {

  private val viewModel: AudioListViewModel by viewModels()
  private val args:  AudioListFragmentArgs by navArgs()
  private lateinit var audioListType: AudioListType
  private lateinit var adapter: AudioListAdapter
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
    adapter = AudioListAdapter(viewModel::onAudioClick)
    binding.audioListRecyclerView.adapter = adapter
    AppCompatResources.getDrawable(view.context, R.drawable.divider_drawable)?.let {
      val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
      dividerItemDecoration.setDrawable(it)
      binding.audioListRecyclerView.addItemDecoration(dividerItemDecoration)
    }
    viewModel.getAllAudio()
    binding.audioListRecyclerView.layoutManager = LinearLayoutManager(context)
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().collect { state -> state?.let { bindViewState(it) } }
      viewModel.viewActions().collect { action -> action?.let { bindViewAction(it) } }
    }
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