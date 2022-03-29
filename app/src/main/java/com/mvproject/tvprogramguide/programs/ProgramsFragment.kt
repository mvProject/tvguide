package com.mvproject.tvprogramguide.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mvproject.tvprogramguide.components.ChannelScreen
import com.mvproject.tvprogramguide.databinding.FragmentProgramsComposeBinding
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class ProgramsFragment : Fragment() {
    private var _binding: FragmentProgramsComposeBinding? = null
    private val binding get() = _binding!!

    private val programsViewModel: ProgramsViewModel by viewModels()

    private lateinit var programsAdapter: ProgramsAdapter

    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramsComposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setContent {
            TvGuideTheme {
                ChannelScreen()
            }
        }
        // todo restore workmanager update observing

        // todo selecting dialog after navigation
        /*

        programsViewModel.checkSavedList()
        programsViewModel.reloadChannels()


        with(binding) {
            programsAdapter = ProgramsAdapter()

            programsAdapter.setupHeaderListener { item ->
                Timber.d("selected channel id is $item")
                routeTo(
                    destination = ProgramsFragmentDirections.toSingleChannelProgramsFragment(
                        item.channelId,
                        item.channelName
                    )
                )
            }
            channelList.apply {
                layoutManager = StickyHeadersLinearLayoutManager<ProgramsAdapter>(requireContext())
                adapter = programsAdapter
            }

            collectFlow(programsViewModel.selectedList) { name ->
                programsToolbar.toolbarTitle.text = name


                if (name.isNotEmpty()) {
                    programsViewModel.partiallyUpdateWorkInfo.observe(
                        viewLifecycleOwner
                    ) { listOfWorkInfo: List<WorkInfo>? ->
                        if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                            Timber.d("testing worker partiallyUpdateWorkInfo null")
                        } else {
                            val workInfo = listOfWorkInfo[0]
                            if (workInfo.state == WorkInfo.State.RUNNING) {
                                val progress = workInfo.progress
                                val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
                                val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
                                showUpdateProgress(current, count)
                            }
                            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                                showUpdateComplete()
                            }
                        }
                    }
                    programsViewModel.fullUpdateWorkInfo.observe(
                        viewLifecycleOwner
                    ) { listOfWorkInfo: List<WorkInfo>? ->
                        if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                            Timber.d("testing worker fullUpdateWorkInfo null")
                        } else {
                            val workInfo = listOfWorkInfo[0]
                            if (workInfo.state == WorkInfo.State.RUNNING) {
                                val progress = workInfo.progress
                                val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
                                val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
                                showUpdateProgress(current, count)
                            }
                            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                                showUpdateComplete()
                            }
                        }
                    }
                }
            }

            collectFlow(programsViewModel.selectedPrograms) {
                programsAdapter.items = it
            }

            programsToolbar.apply {
                selectMenu.setOnClickListener {
                    alertDialog?.cancel()
                    alertDialog = createSelectDialog(
                        activity = requireActivity(),
                        initialValue = programsViewModel.selectedList.value,
                        options = programsViewModel.availableLists
                    ) { result ->
                        programsViewModel.saveSelectedList(result)
                        alertDialog?.cancel()
                    }.apply {
                        show()
                    }
                }
                optionsMenu.setOnClickListener {
                    routeTo(destination = ProgramsFragmentDirections.toSettingsFragment())
                }
            }
        }

         */
    }

    //private fun showUpdateProgress(current: Int, count: Int) {
    //    Timber.d("testing showUpdateProgress")
    //    with(binding) {
    //        progressBarLinear.progress = current + 1
    //        progressBarLinear.max = count
    //        progressBarLinear.visibility = View.VISIBLE
    //    }
    //}

    // private fun showUpdateComplete() {
    //     Timber.d("testing showUpdateComplete")
    //     binding.progressBarLinear.visibility = View.GONE
    //     programsViewModel.reloadChannels()
    // }

    override fun onResume() {
        super.onResume()
        programsViewModel.checkForUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
