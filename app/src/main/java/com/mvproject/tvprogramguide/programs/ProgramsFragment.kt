package com.mvproject.tvprogramguide.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.WorkInfo
import com.mvproject.tvprogramguide.databinding.FragmentProgramsBinding
import com.mvproject.tvprogramguide.sticky.StickyHeadersLinearLayoutManager
import com.mvproject.tvprogramguide.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProgramsFragment : Fragment() {
    private var _binding: FragmentProgramsBinding? = null
    private val binding get() = _binding!!

    private val programsViewModel: ProgramsViewModel by viewModels()

    private lateinit var programsAdapter: ProgramsAdapter

    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        programsViewModel.checkSavedList()

        with(binding) {
            programsAdapter = ProgramsAdapter()

            channelList.apply {
                layoutManager = StickyHeadersLinearLayoutManager<ProgramsAdapter>(requireContext())
                adapter = programsAdapter
            }

            collectFlow(programsViewModel.selectedList) { name ->
                programsToolbar.toolbarTitle.text = name

                Timber.d("testing selectedList is set")

                if (name.isNotEmpty()) {
                    programsViewModel.outputWorkInfo.observe(
                        viewLifecycleOwner,
                        { listOfWorkInfo: List<WorkInfo>? ->
                            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                                Timber.d("testing worker listOfWorkInfo null")
                            } else {
                                val workInfo = listOfWorkInfo[0]
                                if (workInfo.state == WorkInfo.State.RUNNING) {
                                    Timber.d("testing RUNNING")
                                    val progress = workInfo.progress
                                    val current = progress.getInt(CHANNEL_INDEX, COUNT_ZERO)
                                    val count = progress.getInt(CHANNEL_COUNT, COUNT_ZERO)
                                    progressBarLinear.progress = current + 1
                                    progressBarLinear.max = count
                                    progressBarLinear.visibility = View.VISIBLE
                                }
                                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                                    progressBarLinear.visibility = View.GONE
                                    programsViewModel.reloadChannels()
                                    Timber.d("testing SUCCEEDED")
                                }
                            }
                        }
                    )
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

        programsViewModel.reloadChannels()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("testing resume")
        programsViewModel.checkForUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
