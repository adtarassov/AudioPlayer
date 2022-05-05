package com.adtarassov.audioplayer.ui.sreen.audiolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adtarassov.audioplayer.databinding.FragmentAudioListBinding
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListAction.Empty
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListViewState.AudioLoadFailure
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListViewState.AudioLoaded
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListViewState.Loading
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileFragmentDirections
import com.adtarassov.audioplayer.utils.AudioListType
import com.adtarassov.audioplayer.utils.ProfilePageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class AudioListFragment : Fragment() {

  private val viewModel: AudioListViewModel by viewModels()
  private val adapter = AudioListAdapter(
    { viewModel.obtainEvent(AudioListEvent.OnAudioClick(it)) },
    { viewModel.obtainEvent(AudioListEvent.OnAudioLikeClick(it)) }
  )

  private lateinit var audioListType: AudioListType
  private lateinit var profilePageType: ProfilePageType
  private lateinit var accountName: String

  private var _binding: FragmentAudioListBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentAudioListBinding.inflate(inflater, container, false)
    audioListType =
      AudioListType.typeById(arguments?.getInt(AudioListType.BUNDLE_KEY) ?: AudioListType.RECOMMENDATION.id)
    profilePageType = ProfilePageType.typeById(arguments?.getInt(ProfilePageType.BUNDLE_KEY) ?: ProfilePageType.MAIN.id)
    accountName = arguments?.getString(ProfilePageType.USER_ACCOUNT_NAME_KEY) ?: ""
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.audioListRecyclerView.adapter = adapter
    binding.audioListRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.audioListRecyclerView.addItemDecoration(MarginItemDecoration(12))
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
    viewModel.obtainEvent(AudioListEvent.ShowAudioList(audioListType, accountName))
  }

  private fun bindViewState(state: AudioListViewState) {
    when (state) {
      is AudioLoadFailure -> {
        binding.audioListRecyclerView.isVisible = false
        binding.progressView.isVisible = false
        binding.errorTv.isVisible = true
        binding.errorTv.text = state.errorMessage
      }
      is AudioLoaded -> {
        binding.progressView.isVisible = false
        binding.audioListRecyclerView.isVisible = true
        binding.errorTv.isVisible = false
        adapter.refreshAudioList(state.list)
      }
      is Loading -> {
        binding.audioListRecyclerView.isVisible = false
        binding.progressView.isVisible = true
        binding.errorTv.isVisible = false
      }
    }
  }

  private fun bindViewAction(action: AudioListAction) {
    when (action) {
      is AudioListAction.ProfileNavigate -> {
        val navAction = AudioListFragmentDirections.actionAudioListFragmentToProfileFragment()
        findNavController().navigate(navAction)
      }
      is Empty -> {

      }
    }
  }

}