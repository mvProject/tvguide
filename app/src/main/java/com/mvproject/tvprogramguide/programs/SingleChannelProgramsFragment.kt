package com.mvproject.tvprogramguide.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.work.WorkInfo
import com.mvproject.tvprogramguide.databinding.FragmentProgramsSingleBinding
import com.mvproject.tvprogramguide.sticky.StickyHeadersLinearLayoutManager
import com.mvproject.tvprogramguide.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SingleChannelProgramsFragment : Fragment() {
    private var _binding: FragmentProgramsSingleBinding? = null
    private val binding get() = _binding!!

    // private val programsViewModel: ProgramsViewModel by viewModels()
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
                singleChannelToolbar.toolbarTitle.text = id
            }
            //    singleProgramsAdapter = ProgramsAdapter()

            //    singleChannelList.apply {
            //        layoutManager = StickyHeadersLinearLayoutManager<ProgramsAdapter>(requireContext())
            //        adapter = singleProgramsAdapter
            //    }


            //  collectFlow(programsViewModel.selectedPrograms) {
            //      programsAdapter.items = it
            //  }


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
