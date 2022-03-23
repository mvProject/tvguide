package com.mvproject.tvprogramguide.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mvproject.tvprogramguide.databinding.FragmentProgramsSingleBinding
import com.mvproject.tvprogramguide.sticky.StickyHeadersLinearLayoutManager
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName
import com.mvproject.tvprogramguide.utils.collectFlow
import com.mvproject.tvprogramguide.utils.routeToBack
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class SingleChannelProgramsFragment : Fragment() {
    private var _binding: FragmentProgramsSingleBinding? = null
    private val binding get() = _binding!!

    private val singleChannelProgramsViewModel: SingleChannelProgramsViewModel by viewModels()
    private val arg: SingleChannelProgramsFragmentArgs by navArgs()

    private lateinit var singleProgramsAdapter: ProgramsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramsSingleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            arg.channelId?.let { id ->
                val title = arg.channelName?.parseChannelName() ?: id
                singleChannelToolbar.toolbarTitle.text = title
                singleChannelProgramsViewModel.loadPrograms(id)
            }

            singleProgramsAdapter = ProgramsAdapter()

            singleChannelList.apply {
                layoutManager = StickyHeadersLinearLayoutManager<ProgramsAdapter>(requireContext())
                adapter = singleProgramsAdapter
            }

            collectFlow(singleChannelProgramsViewModel.selectedPrograms) {
                singleProgramsAdapter.items = it
            }

            singleChannelToolbar.btnBack.setOnClickListener {
                routeToBack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
