package com.mvproject.tvprogramguide.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mvproject.tvprogramguide.databinding.FragmentProgramsBinding
import com.mvproject.tvprogramguide.sticky.StickyHeadersLinearLayoutManager
import com.mvproject.tvprogramguide.utils.collectFlow
import com.mvproject.tvprogramguide.utils.createSelectDialog
import com.mvproject.tvprogramguide.utils.routeTo
import dagger.hilt.android.AndroidEntryPoint

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

        with(binding) {
            programsAdapter = ProgramsAdapter()
            channelList.apply {
                layoutManager = StickyHeadersLinearLayoutManager<ProgramsAdapter>(requireContext())
                adapter = programsAdapter
            }

            collectFlow(programsViewModel.selected) {
                settingsLanguagesTitle.text = it
            }

            collectFlow(programsViewModel.channels) {
                programsAdapter.items = it
            }

            collectFlow(programsViewModel.loading) { loading ->
                channelList.isVisible = !loading
                progressBar.isVisible = loading
            }

            selectListIcon.setOnClickListener {
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

            settings.setOnClickListener {
                routeTo(destination = ProgramsFragmentDirections.toSettingsFragment())
            }
        }

        programsViewModel.reloadChannels()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
